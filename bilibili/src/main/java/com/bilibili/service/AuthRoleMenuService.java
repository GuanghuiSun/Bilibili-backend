package com.bilibili.service;

import com.bilibili.model.domain.AuthRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @author sgh
 * @description 针对表【t_auth_role_menu(权限控制-角色页面菜单关联表)】的数据库操作Service
 * @createDate 2022-08-04 15:11:43
 */
public interface AuthRoleMenuService extends IService<AuthRoleMenu> {

    /**
     * 根据角色id查询权限
     *
     * @param roleIdSet 角色id
     * @return 页面访问权限
     */
    List<AuthRoleMenu> getByRoleIds(Set<Long> roleIdSet);
}
