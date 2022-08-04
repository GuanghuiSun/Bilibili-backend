package com.bilibili.service;

import com.bilibili.model.domain.AuthRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author sgh
 * @description 针对表【t_auth_role(权限控制-角色表)】的数据库操作Service
 * @createDate 2022-08-04 14:51:21
 */
public interface AuthRoleService extends IService<AuthRole> {

    /**
     * 根据code获取角色
     *
     * @param roleCode code
     * @return 角色
     */
    AuthRole getRoleByCode(String roleCode);
}
