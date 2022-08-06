package com.bilibili.service;

import com.bilibili.model.domain.File;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author sgh
 * @description 针对表【t_file(上传文件相关信息表)】的数据库操作Service
 * @createDate 2022-08-06 15:27:46
 */
public interface FileService extends IService<File> {

    /**
     * 添加文件到数据库
     *
     * @param file 文件
     */
    void addFile(File file);

    /**
     * 通过md5获取文件
     *
     * @param fileMd5 md5
     * @return 文件路径
     */
    String getFileByMd5(String fileMd5);

    /**
     * 获取文件的md5
     *
     * @param multipartFile 文件
     * @return 文件md5
     */
    String getFileMd5(MultipartFile multipartFile);
}
