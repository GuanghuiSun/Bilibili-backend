package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.AuthMenu;
import com.bilibili.service.AuthMenuService;
import com.bilibili.mapper.AuthMenuMapper;
import org.springframework.stereotype.Service;

/**
* @author sgh
* @description 针对表【t_auth_menu(权限控制-页面访问表)】的数据库操作Service实现
* @createDate 2022-08-04 15:11:20
*/
@Service
public class AuthMenuServiceImpl extends ServiceImpl<AuthMenuMapper, AuthMenu>
    implements AuthMenuService{

}




