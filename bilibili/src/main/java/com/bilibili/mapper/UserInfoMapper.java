package com.bilibili.mapper;

import com.bilibili.model.domain.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author sgh
 * @description 针对表【t_user_info(用户信息表)】的数据库操作Mapper
 * @createDate 2022-08-01 23:28:35
 * @Entity com.bilibili.model.domain.UserInfo
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    void updateUserInfo(UserInfo userInfo);

    List<UserInfo> getByUserIds(@Param("userIds") Set<Long> userIds);
}




