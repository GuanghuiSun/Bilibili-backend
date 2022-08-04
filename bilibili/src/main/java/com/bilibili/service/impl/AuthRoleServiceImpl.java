package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.AuthRole;
import com.bilibili.service.AuthRoleService;
import com.bilibili.mapper.AuthRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author sgh
* @description 针对表【t_auth_role(权限控制-角色表)】的数据库操作Service实现
* @createDate 2022-08-04 14:51:21
*/
@Service
public class AuthRoleServiceImpl extends ServiceImpl<AuthRoleMapper, AuthRole>
    implements AuthRoleService{

}




