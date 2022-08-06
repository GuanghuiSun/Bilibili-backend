package com.bilibili.controller;

import cn.hutool.json.JSONUtil;
import com.bilibili.base.BaseResponse;
import com.bilibili.base.ResultUtils;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.upload.UploadFileMessage;
import com.bilibili.service.FileService;
import com.bilibili.utils.FastDFSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import static com.bilibili.base.ErrorCode.FILE_SERVICE_ERROR;
import static com.bilibili.constant.MessageConstant.REQUEST_PARAM_ERROR;

/**
 * 上传文件 controller
 *
 * @author sgh
 * @date 2022-8-6
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private FastDFSUtil fastDFSUtil;

    @Resource
    private FileService fileService;

    /**
     * 文件分片 这里用来测试，实际分片操作交由前端
     *
     * @param multipartFile 文件
     * @return 是否成功
     */
    @PostMapping("/slices")
    public BaseResponse<Boolean> fileToSlices(@RequestParam("file") MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new BusinessException(FILE_SERVICE_ERROR, REQUEST_PARAM_ERROR);
        }
        fastDFSUtil.convertFileToSlices(multipartFile);
        return ResultUtils.success(Boolean.TRUE);
    }

    /**
     * 获取文件的md5
     *
     * @param multipartFile 文件
     * @return 文件md5
     */
    @PostMapping("/fileMd5")
    public BaseResponse<String> getFileMd5(@RequestParam("file")MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new BusinessException(FILE_SERVICE_ERROR, REQUEST_PARAM_ERROR);
        }
        String fileMd5 = fileService.getFileMd5(multipartFile);
        return ResultUtils.success(fileMd5);
    }

    /**
     * 文件分片上传
     *
     * @param multipartFile 文件分片
     * @param json          存储文件相关信息
     * @return 文件存储路径
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFileSlice(@RequestParam("file") MultipartFile multipartFile, @RequestParam("dataJson") String json) {
        if (multipartFile == null || json == null) {
            throw new BusinessException(FILE_SERVICE_ERROR, REQUEST_PARAM_ERROR);
        }
        UploadFileMessage message = JSONUtil.toBean(json, UploadFileMessage.class);
        String path = fastDFSUtil.uploadFileBySlices(multipartFile, message);
        return ResultUtils.success(path);
    }

    /**
     * 下载文件
     *
     * @param fileMd5 文件db5
     * @return url
     */
    @GetMapping("/downLoad")
    public BaseResponse<String> downLoadFile(String fileMd5) {
        if (fileMd5 == null) {
            throw new BusinessException(FILE_SERVICE_ERROR, REQUEST_PARAM_ERROR);
        }
        String path = fileService.getFileByMd5(fileMd5);
        return ResultUtils.success(path);
    }
}
