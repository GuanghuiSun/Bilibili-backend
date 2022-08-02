package com.bilibili.mapper;

import com.bilibili.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author sgh
* @description 针对表【t_user(用户表)】的数据库操作Mapper
* @createDate 2022-08-01 23:28:26
* @Entity com.bilibili.model.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    void updateUser(User user);
}




