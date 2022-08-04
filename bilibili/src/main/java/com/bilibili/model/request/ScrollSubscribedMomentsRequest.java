package com.bilibili.model.request;

import lombok.Data;


/**
 * 滚动查询请求体
 *
 * @author sgh
 * @date 2022-8-4
 */
@Data
public class ScrollSubscribedMomentsRequest {
    /**
     * 偏移量
     */
    private Long offset;

    /**
     * 每次查询大小
     */
    private Long size;

    /**
     * 时间戳
     * 最大查询score位置，即上次查询到的最小时间/当前时间
     */
    private Long max;
}
