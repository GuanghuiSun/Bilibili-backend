package com.bilibili.service;

import com.bilibili.model.domain.AuthRoleElementOperation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @author sgh
 * @description 针对表【t_auth_role_element_operation(权限控制-角色与元素操作关联表)】的数据库操作Service
 * @createDate 2022-08-04 15:11:28
 */
public interface AuthRoleElementOperationService extends IService<AuthRoleElementOperation> {

    /**
     * 根据角色id查询权限
     *
     * @param roleIdSet 角色id
     * @return 操作按钮权限
     */
    List<AuthRoleElementOperation> getByRoleIds(Set<Long> roleIdSet);
}
