package com.bilibili.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.upload.UploadFileMessage;
import com.bilibili.service.FileService;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.exception.FdfsIOException;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.bilibili.base.ErrorCode.FILE_SERVICE_ERROR;
import static com.bilibili.constant.MessageConstant.*;
import static com.bilibili.constant.RedisConstant.FILE_URL_STORAGE;
import static com.bilibili.constant.RedisConstant.UPLOAD_FILE_PATH;

/**
 * fastDFS工具类 文件的上传 删除
 *
 * @author sgh
 * @date 2022-8-5
 */
@Component
@Slf4j
public class FastDFSUtil {

    @Resource
    private FastFileStorageClient fastFileStorageClient;

    @Resource
    private AppendFileStorageClient appendFileStorageClient;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private FileService fileService;

    public static final String DEFAULT_GROUP = "group1";

    public static final int SLICE_SIZE = 1024 * 1024 * 2;

    /**
     * 获取文件类型
     *
     * @param file 文件
     * @return 文件类型
     */
    public String getFileType(MultipartFile file) {
        if (file == null) {
            throw new BusinessException(FILE_SERVICE_ERROR, FILE_TYPE_ERROR);
        }
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new BusinessException(FILE_SERVICE_ERROR, FILE_NAME_ERROR);
        }
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1);
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件存储路径
     */
    public String uploadCommonFile(MultipartFile file) {
        try {
            Set<MetaData> metaDataSet = new HashSet<>();
            String fileType = getFileType(file);
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileType, metaDataSet);
            return storePath.getPath();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 断点续传文件
     *
     * @param file  文件分片
     * @param param 存放文件唯一标识符，分片总数
     * @return 文件路径
     */
    public String uploadFileBySlices(MultipartFile file, UploadFileMessage param) {
        if (param == null || file == null) {
            throw new BusinessException(FILE_SERVICE_ERROR);
        }
        //检查文件是否上传过
        if(param.getFilePath() != null) {
            String url = (String) stringRedisTemplate.opsForHash().get(FILE_URL_STORAGE, param.getFilePath());
            if(url != null) {
                return url;
            }
        }
        String fileMd5 = param.getFileMd5();
        Integer total = param.getTotalSliceNumber();
        Integer sliceNumber = param.getSliceNumber();
        Long sliceSize = param.getSliceSize();
        if (StringUtils.isBlank(fileMd5) || total == null || total < 0 || sliceNumber == null
                || sliceNumber < 0 || sliceSize == null || sliceSize < 0) {
            throw new BusinessException(FILE_SERVICE_ERROR, REQUEST_PARAM_ERROR);
        }
        String fileName = file.getOriginalFilename();
        String key = UPLOAD_FILE_PATH + ":" + fileMd5;
        String path = null;
        try {
            //第一次上传
            if (sliceNumber == 1) {
                StorePath storePath = appendFileStorageClient.uploadAppenderFile(DEFAULT_GROUP,
                        file.getInputStream(), file.getSize(), fileName);
                if (storePath == null) {
                    throw new BusinessException(FILE_SERVICE_ERROR, FILE_UPLOAD_ERROR);
                } else {
                    path = storePath.getPath();
                    UploadFileMessage uploadFileMessage = new UploadFileMessage();
                    uploadFileMessage.setFileMd5(fileMd5);
                    uploadFileMessage.setFilePath(path);
                    uploadFileMessage.setSliceNumber(1);
                    uploadFileMessage.setTotalSliceNumber(total);
                    uploadFileMessage.setSliceSize(sliceSize);
                    stringRedisTemplate.opsForHash().putAll(key, BeanUtil.beanToMap(uploadFileMessage, new HashMap<>(),
                            CopyOptions.create().setIgnoreNullValue(true)
                                    .setFieldValueEditor((fieldName, fieldValue) -> String.valueOf(fieldValue))));
                }
            } else {
                //续传
                Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(key);
                UploadFileMessage uploadFile = BeanUtil.fillBeanWithMap(map, new UploadFileMessage(), false);
                path = uploadFile.getFilePath();
                long offset = (sliceNumber - 1) * sliceSize;
                appendFileStorageClient.modifyFile(DEFAULT_GROUP, path, file.getInputStream(), file.getSize(), offset);
                stringRedisTemplate.opsForHash().put(key, "sliceNumber", String.valueOf(uploadFile.getSliceNumber() + 1));
            }
            //检查是否上传完毕
            Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
            UploadFileMessage uploadFile = BeanUtil.fillBeanWithMap(entries, new UploadFileMessage(), false);
            Integer uploadCompleted = uploadFile.getSliceNumber();
            //上传完毕
            if (total.equals(uploadCompleted)) {
                stringRedisTemplate.delete(key);
                //保存到数据库中
                com.bilibili.model.domain.File dbFile = new com.bilibili.model.domain.File();
                dbFile.setUrl(path);
                dbFile.setType(getFileType(file));
                dbFile.setMd5(fileMd5);
                fileService.addFile(dbFile);
                //保存url到redis中
                stringRedisTemplate.opsForHash().putIfAbsent(FILE_URL_STORAGE, fileMd5, path);
            }
        } catch (FdfsIOException | SocketTimeoutException e) {
            throw new BusinessException(UPLOAD_TIMEOUT_ERROR_CODE, FILE_ERROR, FILE_UPLOAD_TIMEOUT_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 将文件分片存储 -用于测试
     *
     * @param multipartFile 文件
     */
    public void convertFileToSlices(MultipartFile multipartFile) {
        String fileType = this.getFileType(multipartFile);
        File file = this.multipartFileToFile(multipartFile);
        long length = file.length();
        int count = 1;
        for (int i = 0; i < length; i += SLICE_SIZE) {
            RandomAccessFile randomAccessFile = null;
            FileOutputStream fos = null;
            try {
                randomAccessFile = new RandomAccessFile(file, "r");
                randomAccessFile.seek(i);
                byte[] bytes = new byte[SLICE_SIZE];
                int len = randomAccessFile.read(bytes);
                String path = "C:\\Users\\huawei\\Desktop\\" + count + "." + fileType;
                fos = new FileOutputStream(new File(path));
                fos.write(bytes, 0, len);
                count++;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        file.delete();
    }

    /**
     * 文件类型转换 用于测试
     *
     * @param multipartFile 文件
     * @return 转换类型后的文件
     */
    public File multipartFileToFile(MultipartFile multipartFile) {
        File file = null;
        try {
            String fileName = multipartFile.getOriginalFilename();
            log.debug("filename:{}", fileName);
            int index = 0;
            if (fileName != null) {
                index = fileName.lastIndexOf(".");
                file = File.createTempFile(fileName.substring(0, index), fileName.substring(index));
                multipartFile.transferTo(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     */
    public void deleteFile(String filePath) {
        fastFileStorageClient.deleteFile(filePath);
    }
}
