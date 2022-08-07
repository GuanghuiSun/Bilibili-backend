package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.UserCoin;
import com.bilibili.service.UserCoinService;
import com.bilibili.mapper.UserCoinMapper;
import org.springframework.stereotype.Service;

/**
 * @author sgh
 * @description 针对表【t_user_coin(用户硬币表)】的数据库操作Service实现
 * @createDate 2022-08-07 17:26:05
 */
@Service
public class UserCoinServiceImpl extends ServiceImpl<UserCoinMapper, UserCoin>
        implements UserCoinService {

    @Override
    public UserCoin getCoinsAmount(Long userId) {
        LambdaQueryWrapper<UserCoin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCoin::getUserId, userId);
        return this.getOne(wrapper);
    }
}




