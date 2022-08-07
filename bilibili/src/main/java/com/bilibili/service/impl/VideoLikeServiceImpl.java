package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.Video;
import com.bilibili.model.domain.VideoLike;
import com.bilibili.service.VideoLikeService;
import com.bilibili.mapper.VideoLikeMapper;
import com.bilibili.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static com.bilibili.base.ErrorCode.REQUEST_SERVICE_ERROR;
import static com.bilibili.constant.MessageConstant.*;

/**
 * @author sgh
 * @description 针对表【t_video_like(视频点赞表)】的数据库操作Service实现
 * @createDate 2022-08-07 15:04:49
 */
@Service
public class VideoLikeServiceImpl extends ServiceImpl<VideoLikeMapper, VideoLike>
        implements VideoLikeService {

    @Resource
    private VideoService videoService;

    public VideoLike checkVideoLike(Long videoId, Long userId) {
        //检查视频是否存在
        Video video = videoService.getById(videoId);
        if (video == null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, VIDEO_NOT_EXIST_ERROR);
        }
        //检查是否点赞过
        LambdaQueryWrapper<VideoLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoLike::getVideoId, videoId).eq(VideoLike::getUserId, userId);
        return this.getOne(wrapper);
    }

    @Override
    public void addVideoLike(Long videoId, Long userId) {
        VideoLike query = this.checkVideoLike(videoId, userId);
        if (query != null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR_CODE, VIDEO_LIKE_REPEAT_ERROR);
        }
        VideoLike videoLike = new VideoLike();
        videoLike.setUserId(userId);
        videoLike.setVideoId(videoId);
        this.save(videoLike);
    }

    @Override
    public void cancelVideoLike(Long videoId, Long userId) {
        VideoLike query = this.checkVideoLike(videoId, userId);
        if (query == null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR_CODE, VIDEO_LIKE_NOT_EXIST_ERROR);
        }
        query.setIsDeleted((byte) 1);
        this.updateById(query);
    }

    @Override
    public Map<String, Object> getVideoLikes(Long videoId, Long userId) {
        LambdaQueryWrapper<VideoLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoLike::getVideoId, videoId);
        long count = this.count(wrapper);
        //检查当前用户是否点赞了
        VideoLike query = null;
        if(userId != null) {
            query = this.checkVideoLike(videoId, userId);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("like", query != null);
        return result;
    }
}




