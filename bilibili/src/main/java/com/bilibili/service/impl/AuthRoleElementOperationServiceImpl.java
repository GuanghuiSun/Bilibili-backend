package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.AuthRoleElementOperation;
import com.bilibili.service.AuthRoleElementOperationService;
import com.bilibili.mapper.AuthRoleElementOperationMapper;
import org.springframework.stereotype.Service;

/**
* @author sgh
* @description 针对表【t_auth_role_element_operation(权限控制-角色与元素操作关联表)】的数据库操作Service实现
* @createDate 2022-08-04 15:11:28
*/
@Service
public class AuthRoleElementOperationServiceImpl extends ServiceImpl<AuthRoleElementOperationMapper, AuthRoleElementOperation>
    implements AuthRoleElementOperationService{

}




