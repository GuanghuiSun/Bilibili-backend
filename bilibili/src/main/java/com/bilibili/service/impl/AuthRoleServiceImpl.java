package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.base.ErrorCode;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.AuthRole;
import com.bilibili.service.AuthRoleService;
import com.bilibili.mapper.AuthRoleMapper;
import org.springframework.stereotype.Service;

import static com.bilibili.base.ErrorCode.GET_SERVICE_ERROR;
import static com.bilibili.constant.MessageConstant.ROLE_CODE_ERROR;

/**
 * @author sgh
 * @description 针对表【t_auth_role(权限控制-角色表)】的数据库操作Service实现
 * @createDate 2022-08-04 14:51:21
 */
@Service
public class AuthRoleServiceImpl extends ServiceImpl<AuthRoleMapper, AuthRole>
        implements AuthRoleService {

    @Override
    public AuthRole getRoleByCode(String roleCode) {
        LambdaQueryWrapper<AuthRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuthRole::getCode, roleCode);
        AuthRole role = this.getOne(wrapper);
        if (role == null) {
            throw new BusinessException(GET_SERVICE_ERROR, ROLE_CODE_ERROR);
        }
        return role;
    }
}




