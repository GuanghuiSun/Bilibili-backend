package com.bilibili.service;

import com.bilibili.model.domain.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @author sgh
 * @description 针对表【t_user_info(用户信息表)】的数据库操作Service
 * @createDate 2022-08-01 23:28:35
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 更新个人信息
     * @param userInfo 用户信息
     */
    void updateUserInfo(UserInfo userInfo);

    /**
     * 根据用户id集合查询用户
     *
     * @param userIds 用户id集合
     * @return 查询结果
     */
    List<UserInfo> getByUserIds(Set<Long> userIds);
}
