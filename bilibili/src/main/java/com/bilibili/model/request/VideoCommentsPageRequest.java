package com.bilibili.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 视频评论分页请求体
 *
 * @author sgh
 * @date 2022-8-7
 */
@Data
public class VideoCommentsPageRequest implements Serializable {
    private static final long serialVersionUID = 5100893334662031941L;
    private Integer currentPage;
    private Integer pageSize;
    private Long videoId;
}
