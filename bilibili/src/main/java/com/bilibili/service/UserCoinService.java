package com.bilibili.service;

import com.bilibili.model.domain.UserCoin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author sgh
 * @description 针对表【t_user_coin(用户硬币表)】的数据库操作Service
 * @createDate 2022-08-07 17:26:05
 */
public interface UserCoinService extends IService<UserCoin> {

    /**
     * 获取用户硬币总数
     *
     * @param userId 用户id
     * @return 硬币总数
     */
    UserCoin getCoinsAmount(Long userId);
}
