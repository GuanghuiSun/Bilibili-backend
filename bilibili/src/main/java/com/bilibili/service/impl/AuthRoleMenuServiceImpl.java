package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.AuthRoleMenu;
import com.bilibili.service.AuthRoleMenuService;
import com.bilibili.mapper.AuthRoleMenuMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
* @author sgh
* @description 针对表【t_auth_role_menu(权限控制-角色页面菜单关联表)】的数据库操作Service实现
* @createDate 2022-08-04 15:11:43
*/
@Service
public class AuthRoleMenuServiceImpl extends ServiceImpl<AuthRoleMenuMapper, AuthRoleMenu>
    implements AuthRoleMenuService{

    @Resource
    private AuthRoleMenuMapper authRoleMenuMapper;

    @Override
    public List<AuthRoleMenu> getByRoleIds(Set<Long> roleIdSet) {
        return authRoleMenuMapper.getByRoleIds(roleIdSet);
    }
}




