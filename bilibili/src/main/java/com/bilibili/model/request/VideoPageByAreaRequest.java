package com.bilibili.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 分区分页查询请求体
 *
 * @author sgh
 * @date 2022-8-6
 */
@Data
public class VideoPageByAreaRequest implements Serializable {
    private static final long serialVersionUID = 4954019760705709293L;
    private Integer currentPage;
    private Integer pageSize;
    private String area;
}
