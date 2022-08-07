package com.bilibili.service;

import com.bilibili.model.domain.VideoCoin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author sgh
 * @description 针对表【t_video_coin(视频投币表)】的数据库操作Service
 * @createDate 2022-08-07 17:28:41
 */
public interface VideoCoinService extends IService<VideoCoin> {

    /**
     * 视频投币
     *
     * @param videoCoin 视频投币信息
     * @param userId    用户id
     */
    void addVideoCoin(VideoCoin videoCoin, Long userId);

    /**
     * 获取视频投币数量
     *
     * @param videoId 视频id
     * @param userId  用户id
     * @return 视频投币量 该用户是否投币
     */
    Map<String, Object> getVideoCoins(Long videoId, Long userId);

    /**
     * 获取用户对该视频的投币情况
     *
     * @param videoId 视频id
     * @param userId  用户id
     * @return 用户视频投币情况
     */
    VideoCoin getVideoCoinByUser(Long videoId, Long userId);
}
