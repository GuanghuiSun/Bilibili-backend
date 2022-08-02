package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.UserInfo;
import com.bilibili.service.UserInfoService;
import com.bilibili.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author huawei
* @description 针对表【t_user_info(用户信息表)】的数据库操作Service实现
* @createDate 2022-08-01 23:28:35
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}




