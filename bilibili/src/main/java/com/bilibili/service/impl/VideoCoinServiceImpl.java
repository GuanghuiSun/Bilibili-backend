package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.UserCoin;
import com.bilibili.model.domain.Video;
import com.bilibili.model.domain.VideoCoin;
import com.bilibili.service.UserCoinService;
import com.bilibili.service.VideoCoinService;
import com.bilibili.mapper.VideoCoinMapper;
import com.bilibili.service.VideoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.bilibili.base.ErrorCode.PARAM_ERROR;
import static com.bilibili.base.ErrorCode.REQUEST_SERVICE_ERROR;
import static com.bilibili.constant.MessageConstant.*;

/**
 * @author sgh
 * @description 针对表【t_video_coin(视频投币表)】的数据库操作Service实现
 * @createDate 2022-08-07 17:28:41
 */
@Service
public class VideoCoinServiceImpl extends ServiceImpl<VideoCoinMapper, VideoCoin>
        implements VideoCoinService {

    @Resource
    private VideoService videoService;

    @Resource
    private UserCoinService userCoinService;

    @Override
    @Transactional
    public void addVideoCoin(VideoCoin videoCoin, Long userId) {
        Long videoId = videoCoin.getVideoId();
        Long amount = videoCoin.getAmount();
        if (videoId == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        //查看是否投过币 视频是否存在
        VideoCoin dbVideoCoin = this.getVideoCoinByUser(videoId, userId);
        //查询用户硬币数量
        UserCoin userCoin = userCoinService.getCoinsAmount(userId);
        Long userAmount = userCoin.getAmount();
        userAmount = userAmount == null ? 0 : userAmount;
        if (userAmount < amount) {
            throw new BusinessException(REQUEST_SERVICE_ERROR_CODE, COINS_INSUFFICIENT_ERROR);
        }

        if (dbVideoCoin == null) {
            videoCoin.setAmount(amount);
            videoCoin.setUserId(userId);
            this.save(videoCoin);
        } else {
            Long newAmount = amount + dbVideoCoin.getAmount();
            dbVideoCoin.setAmount(newAmount);
            dbVideoCoin.setUpdateTime(new Date());
            this.updateById(dbVideoCoin);
        }
        //更新用户硬币数
        userCoin.setAmount(userAmount - amount);
        userCoin.setUpdateTime(new Date());
        userCoinService.updateById(userCoin);
    }

    @Override
    public Map<String, Object> getVideoCoins(Long videoId, Long userId) {
        //查询当前用户是否投币 视频是否存在
        VideoCoin videoCoinByUser = this.getVideoCoinByUser(videoId, userId);
        //查询视频投币数
        LambdaQueryWrapper<VideoCoin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoCoin::getVideoId, videoId);
        long count = this.count(wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("coin", videoCoinByUser != null);
        return result;
    }

    @Override
    public VideoCoin getVideoCoinByUser(Long videoId, Long userId) {
        //查看视频是否存在
        Video query = videoService.getById(videoId);
        if (query == null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, VIDEO_NOT_EXIST_ERROR);
        }
        LambdaQueryWrapper<VideoCoin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoCoin::getVideoId, videoId).eq(VideoCoin::getUserId, userId);
        return this.getOne(wrapper);
    }
}




