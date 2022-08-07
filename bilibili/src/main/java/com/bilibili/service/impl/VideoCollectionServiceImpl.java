package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.CollectionGroup;
import com.bilibili.model.domain.Video;
import com.bilibili.model.domain.VideoCollection;
import com.bilibili.service.CollectionGroupService;
import com.bilibili.service.VideoCollectionService;
import com.bilibili.mapper.VideoCollectionMapper;
import com.bilibili.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static com.bilibili.base.ErrorCode.PARAM_ERROR;
import static com.bilibili.base.ErrorCode.REQUEST_SERVICE_ERROR;
import static com.bilibili.constant.MessageConstant.*;

/**
 * @author sgh
 * @description 针对表【t_video_collection(视频收藏表)】的数据库操作Service实现
 * @createDate 2022-08-07 16:10:47
 */
@Service
public class VideoCollectionServiceImpl extends ServiceImpl<VideoCollectionMapper, VideoCollection>
        implements VideoCollectionService {

    @Resource
    private VideoService videoService;

    @Resource
    private CollectionGroupService collectionGroupService;

    public VideoCollection checkVideoCollect(Long videoId, Long groupId, Long userId) {
        if (videoId == null || videoId < 0 || groupId == null || groupId < 0) {
            throw new BusinessException(PARAM_ERROR);
        }
        //查看视频是否存在
        Video query = videoService.getById(videoId);
        if (query == null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, VIDEO_NOT_EXIST_ERROR);
        }
        //检查分组是否存在
        LambdaQueryWrapper<CollectionGroup> groupWrapper = new LambdaQueryWrapper<>();
        groupWrapper.eq(CollectionGroup::getUserId, userId).eq(CollectionGroup::getId, groupId);
        CollectionGroup queryGroup = collectionGroupService.getOne(groupWrapper);
        if (queryGroup == null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, COLLECT_GROUP_NOT_EXIST_ERROR);
        }
        //检查是否收藏过
        LambdaQueryWrapper<VideoCollection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoCollection::getVideoId, videoId)
                .eq(VideoCollection::getGroupId, groupId)
                .eq(VideoCollection::getUserId, userId);
        return this.getOne(wrapper);
    }

    @Override
    public void addVideoCollection(VideoCollection videoCollection, Long userId) {
        Long videoId = videoCollection.getVideoId();
        Long groupId = videoCollection.getGroupId();
        VideoCollection query = this.checkVideoCollect(videoId, groupId, userId);
        if (query != null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR_CODE, VIDEO_COLLECT_REPEAT_ERROR);
        }
        videoCollection.setUserId(userId);
        this.save(videoCollection);
    }

    @Override
    public void cancelVideoCollection(VideoCollection videoCollection, Long userId) {
        Long videoId = videoCollection.getVideoId();
        Long groupId = videoCollection.getGroupId();
        VideoCollection query = this.checkVideoCollect(videoId, groupId, userId);
        if (query == null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR_CODE, VIDEO_COLLECT_NOT_EXIST_ERROR);
        }
        query.setIsDeleted((byte) 1);
        this.updateById(query);
    }

    @Override
    public Map<String, Object> getVideoLikes(Long videoId, Long userId) {
        if (videoId == null || videoId < 0) {
            throw new BusinessException(PARAM_ERROR);
        }
        //查看视频是否存在
        Video query = videoService.getById(videoId);
        if (query == null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, VIDEO_NOT_EXIST_ERROR);
        }
        //获取所有收藏数量
        LambdaQueryWrapper<VideoCollection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoCollection::getVideoId, videoId);
        long count = this.count(wrapper);
        //获取用户是否收藏
        wrapper.eq(VideoCollection::getUserId, userId);
        VideoCollection queryOne = this.getOne(wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("collect", queryOne != null);
        return result;
    }
}




