package com.bilibili.model.upload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 断点续传文件信息
 *
 * @author sgh
 * @date 2022-8-5
 */
@Data
public class UploadFileMessage implements Serializable {

    private static final long serialVersionUID = -30122290930779558L;
    /**
     * 文件唯一标识
     */
    private String fileMd5;

    /**
     * 文件存放位置
     */
    private String filePath;

    /**
     * 成功上传的分片数量/正在上传的分片数
     */
    private Integer sliceNumber;

    /**
     * 总共分片数量
     */
    private Integer totalSliceNumber;

    /**
     * 分片大小 用来计算偏移量
     */
    private Long sliceSize;
}
