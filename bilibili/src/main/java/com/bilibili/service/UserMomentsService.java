package com.bilibili.service;

import com.bilibili.model.domain.UserMoments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bilibili.model.result.ScrollResult;


/**
 * @author sgh
 * @description 针对表【t_user_moments(用户动态表)】的数据库操作Service
 * @createDate 2022-08-03 20:46:12
 */
public interface UserMomentsService extends IService<UserMoments> {

    /**
     * 发布动态，推送消息给粉丝
     *
     * @param userMoments 动态
     */
    void addUserMoments(UserMoments userMoments);

    /**
     * 获取用户订阅的动态 滚动分页查询
     *
     * @param userId 用户id
     * @param max    最大score 即时间戳
     * @param offset 偏移量
     * @param size   页面大小
     * @return 动态结果集
     */
    ScrollResult getUserSubscribedMoments(Long userId, Long max, Long offset, Long size);
}
