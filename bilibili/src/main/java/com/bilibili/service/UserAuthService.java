package com.bilibili.service;

import com.bilibili.model.auth.UserAuthorities;

/**
 * 用户权限service
 *
 * @author sgh
 * @date 2022-8-4
 */
public interface UserAuthService {

    /**
     * 获取用户所有权限
     *
     * @param userId 用户id
     * @return 所拥有的权限
     */
    UserAuthorities getUserAuthorities(Long userId);

    /**
     * 添加用户默认角色
     *
     * @param userId 用户id
     */
    void addUserDefaultRole(Long userId);
}
