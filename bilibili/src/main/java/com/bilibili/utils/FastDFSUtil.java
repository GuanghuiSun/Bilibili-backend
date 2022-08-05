package com.bilibili.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.upload.UploadFileMessage;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.exception.FdfsIOException;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.bilibili.base.ErrorCode.FILE_SERVICE_ERROR;
import static com.bilibili.constant.MessageConstant.*;
import static com.bilibili.constant.RedisConstant.UPLOAD_FILE_PATH;

/**
 * fastDFS工具类 文件的上传 删除
 *
 * @author sgh
 * @date 2022-8-5
 */
@Slf4j
public class FastDFSUtil {

    @Resource
    private FastFileStorageClient fastFileStorageClient;

    @Resource
    private AppendFileStorageClient appendFileStorageClient;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final String DEFAULT_GROUP = "group1";

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
                    stringRedisTemplate.opsForHash().putAll(key, BeanUtil.beanToMap(uploadFileMessage, new HashMap<>(),
                            CopyOptions.create().setIgnoreNullValue(true)
                                    .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString())));
                }
            } else {
                Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(key);
                UploadFileMessage uploadFile = BeanUtil.fillBeanWithMap(map, new UploadFileMessage(), false);
                path = uploadFile.getFilePath();
                long offset = (sliceNumber - 1) * sliceSize;
                appendFileStorageClient.modifyFile(DEFAULT_GROUP, path, file.getInputStream(), file.getSize(), offset);
                stringRedisTemplate.opsForHash().put(key, "sliceNumber", uploadFile.getSliceNumber() + 1);
            }
            //检查是否上传完毕
            Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
            UploadFileMessage uploadFile = BeanUtil.fillBeanWithMap(entries, new UploadFileMessage(), false);
            Integer uploadCompleted = uploadFile.getSliceNumber();
            if (total.equals(uploadCompleted)) {
                stringRedisTemplate.delete(key);
            }
        } catch (FdfsIOException | SocketTimeoutException e) {
            throw new BusinessException(UPLOAD_TIMEOUT_ERROR_CODE, FILE_ERROR, FILE_UPLOAD_TIMEOUT_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage());

        }
        return path;
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
