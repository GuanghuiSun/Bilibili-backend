package com.bilibili.service;

import com.bilibili.model.domain.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author sgh
 * @description 针对表【t_user_role(用户角色关联表)】的数据库操作Service
 * @createDate 2022-08-04 15:12:02
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 获取用户所有角色
     *
     * @param userId 用户id
     * @return 角色
     */
    List<UserRole> getUserRoles(Long userId);

    /**
     * 添加用户角色
     *
     * @param userRole 用户角色关系
     */
    void addUserRole(UserRole userRole);
}
