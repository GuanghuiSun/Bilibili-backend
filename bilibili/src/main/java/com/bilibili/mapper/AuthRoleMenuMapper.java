package com.bilibili.mapper;

import com.bilibili.model.domain.AuthRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Set;

/**
 * @author sgh
 * @description 针对表【t_auth_role_menu(权限控制-角色页面菜单关联表)】的数据库操作Mapper
 * @createDate 2022-08-04 15:11:43
 * @Entity com.bilibili.model.domain.AuthRoleMenu
 */
public interface AuthRoleMenuMapper extends BaseMapper<AuthRoleMenu> {

    List<AuthRoleMenu> getByRoleIds(Set<Long> roleIdSet);
}




