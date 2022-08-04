package com.bilibili.model.auth;

import com.bilibili.model.domain.AuthRoleElementOperation;
import com.bilibili.model.domain.AuthRoleMenu;
import lombok.Data;

import java.util.List;

/**
 * 用户权限类
 *
 * @author sgh
 * @date 2022-8-4
 */
@Data
public class UserAuthorities {
    /**
     * 元素操作权限
     */
    private List<AuthRoleElementOperation> roleElementOperationList;

    /**
     * 页面访问权限
     */
    private List<AuthRoleMenu> roleMenuList;
}
