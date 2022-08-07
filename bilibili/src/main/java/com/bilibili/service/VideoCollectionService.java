package com.bilibili.service;

import com.bilibili.model.domain.VideoCollection;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author sgh
 * @description 针对表【t_video_collection(视频收藏表)】的数据库操作Service
 * @createDate 2022-08-07 16:10:47
 */
public interface VideoCollectionService extends IService<VideoCollection> {

    /**
     * 收藏视频
     *
     * @param videoCollection 视频收藏请求体
     * @param userId          用户id
     */
    void addVideoCollection(VideoCollection videoCollection, Long userId);

    /**
     * 取消收藏视频
     *
     * @param videoCollection 视频收藏请求体
     * @param userId          用户id
     */
    void cancelVideoCollection(VideoCollection videoCollection, Long userId);

    /**
     * 获取视频收藏数量
     *
     * @param videoId 视频id
     * @param userId  用户id
     * @return 视频收藏量 该用户是否收藏
     */
    Map<String, Object> getVideoLikes(Long videoId, Long userId);
}
