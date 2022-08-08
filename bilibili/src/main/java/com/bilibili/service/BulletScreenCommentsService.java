package com.bilibili.service;

import com.bilibili.model.domain.BulletScreenComments;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author sgh
 * @description 针对表【t_bullet_screen_comments(弹幕记录表)】的数据库操作Service
 * @createDate 2022-08-08 16:04:57
 */
public interface BulletScreenCommentsService extends IService<BulletScreenComments> {

    /**
     * 存储弹幕到redis
     * 异步存储到数据库
     *
     * @param bulletScreenComments 弹幕
     */
    void saveBulletScreenComments(BulletScreenComments bulletScreenComments);

    /**
     * 获取指定时间范围内的弹幕
     *
     * @return 弹幕
     */
    List<BulletScreenComments> getBulletScreenComments(Long videoId, String startTime, String endTime);
}
