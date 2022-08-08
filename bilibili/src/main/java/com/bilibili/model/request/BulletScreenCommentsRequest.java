package com.bilibili.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 弹幕获取请求体
 *
 * @author sgh
 * @date 2022-8-8
 */
@Data
public class BulletScreenCommentsRequest implements Serializable {
    private static final long serialVersionUID = 587436185928908896L;
    private Long videoId;
    private String startTime;
    private String endTime;
}
