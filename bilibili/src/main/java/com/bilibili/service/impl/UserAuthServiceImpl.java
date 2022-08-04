package com.bilibili.service.impl;

import java.util.ArrayList;

import com.bilibili.model.auth.UserAuthorities;
import com.bilibili.model.domain.AuthRole;
import com.bilibili.model.domain.AuthRoleElementOperation;
import com.bilibili.model.domain.AuthRoleMenu;
import com.bilibili.model.domain.UserRole;
import com.bilibili.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bilibili.constant.user.AuthRoleConstant.ROLE_CODE_LV0;

/**
 * 用户权限
 *
 * @author sgh
 * @date 2022-8-4
 */
@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private AuthRoleElementOperationService authRoleElementOperationService;

    @Resource
    private AuthRoleMenuService authRoleMenuService;

    @Resource
    private AuthRoleService authRoleService;

    @Override
    public UserAuthorities getUserAuthorities(Long userId) {
        UserAuthorities result = new UserAuthorities();
        //获取用户的所有角色
        List<UserRole> userRoleList = userRoleService.getUserRoles(userId);
        Set<Long> roleIdSet = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        if (roleIdSet.isEmpty()) {
            result.setRoleMenuList(Collections.emptyList());
            result.setRoleElementOperationList(Collections.emptyList());
            return result;
        }
        //获取所有权限
        List<AuthRoleElementOperation> roleElementOperations = authRoleElementOperationService.getByRoleIds(roleIdSet);
        List<AuthRoleMenu> roleMenuList = authRoleMenuService.getByRoleIds(roleIdSet);
        result.setRoleElementOperationList(roleElementOperations);
        result.setRoleMenuList(roleMenuList);
        return result;
    }

    @Override
    public void addUserDefaultRole(Long userId) {
        UserRole userRole = new UserRole();
        //设置默认角色Lv0
        AuthRole role = authRoleService.getRoleByCode(ROLE_CODE_LV0);
        userRole.setUserId(userId);
        userRole.setRoleId(role.getId());
        userRoleService.addUserRole(userRole);
    }
}
