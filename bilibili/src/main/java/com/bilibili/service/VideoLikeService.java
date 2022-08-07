package com.bilibili.service;

import com.bilibili.model.domain.VideoLike;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author sgh
 * @description 针对表【t_video_like(视频点赞表)】的数据库操作Service
 * @createDate 2022-08-07 15:04:49
 */
public interface VideoLikeService extends IService<VideoLike> {
    /**
     * 点赞视频
     *
     * @param videoId 视频id
     * @param userId  用户id
     */
    void addVideoLike(Long videoId, Long userId);

    /**
     * 取消点赞
     *
     * @param videoId 视频 id
     * @param userId  用户id
     */
    void cancelVideoLike(Long videoId, Long userId);

    /**
     * 获取视频点赞量
     *
     * @param videoId 视频id
     * @param userId  用户id
     * @return 视频点赞量 该用户是否点赞了
     */
    Map<String, Object> getVideoLikes(Long videoId, Long userId);
}
