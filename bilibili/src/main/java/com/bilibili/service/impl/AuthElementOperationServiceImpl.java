package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.AuthElementOperation;
import com.bilibili.service.AuthElementOperationService;
import com.bilibili.mapper.AuthElementOperationMapper;
import org.springframework.stereotype.Service;

/**
* @author sgh
* @description 针对表【t_auth_element_operation(权限控制-页面元素操作表)】的数据库操作Service实现
* @createDate 2022-08-04 15:09:31
*/
@Service
public class AuthElementOperationServiceImpl extends ServiceImpl<AuthElementOperationMapper, AuthElementOperation>
    implements AuthElementOperationService{

}




