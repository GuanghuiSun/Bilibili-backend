package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.UserRole;
import com.bilibili.service.UserRoleService;
import com.bilibili.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sgh
 * @description 针对表【t_user_role(用户角色关联表)】的数据库操作Service实现
 * @createDate 2022-08-04 15:12:02
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
        implements UserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<UserRole> getUserRoles(Long userId) {
        return userRoleMapper.getUserRoles(userId);
    }

    @Override
    public void addUserRole(UserRole userRole) {
        this.save(userRole);
    }
}




