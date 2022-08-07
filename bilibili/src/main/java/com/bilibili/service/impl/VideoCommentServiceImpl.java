package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.UserInfo;
import com.bilibili.model.domain.Video;
import com.bilibili.model.domain.VideoComment;
import com.bilibili.service.UserInfoService;
import com.bilibili.service.VideoCommentService;
import com.bilibili.mapper.VideoCommentMapper;
import com.bilibili.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bilibili.base.ErrorCode.PARAM_ERROR;
import static com.bilibili.base.ErrorCode.REQUEST_SERVICE_ERROR;
import static com.bilibili.constant.MessageConstant.VIDEO_NOT_EXIST_ERROR;

/**
 * @author sgh
 * @description 针对表【t_video_comment(视频评论表)】的数据库操作Service实现
 * @createDate 2022-08-07 19:55:02
 */
@Service
public class VideoCommentServiceImpl extends ServiceImpl<VideoCommentMapper, VideoComment>
        implements VideoCommentService {

    @Resource
    private VideoService videoService;

    @Resource
    private VideoCommentMapper videoCommentMapper;

    @Resource
    private UserInfoService userInfoService;

    @Override
    public void addVideoComment(VideoComment videoComment, Long userId) {
        Long videoId = videoComment.getVideoId();
        if (videoId == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Video query = videoService.getById(videoId);
        if (query == null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, VIDEO_NOT_EXIST_ERROR);
        }
        videoComment.setUserId(userId);
        this.save(videoComment);
    }

    @Override
    public IPage<VideoComment> pageListVideoComments(Integer currentPage, Integer pageSize, Long videoId) {
        if (videoId == null || currentPage == null || pageSize == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Video query = videoService.getById(videoId);
        if (query == null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, VIDEO_NOT_EXIST_ERROR);
        }
        IPage<VideoComment> page = new Page<>(currentPage, pageSize);
        //获取所有根节点的评论
        LambdaQueryWrapper<VideoComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoComment::getVideoId, videoId)
                .eq(VideoComment::getRootId, null).orderByDesc(VideoComment::getId);
        IPage<VideoComment> pages = videoCommentMapper.selectPage(page, wrapper);
        List<VideoComment> list = pages.getRecords();
        if (pages.getTotal() > 0) {
            //批量查询二级评论
            List<Long> parentIdList = list.stream().map(VideoComment::getId).collect(Collectors.toList());
            //获取这些id作为根id的评论 即子评论
            LambdaQueryWrapper<VideoComment> rootWrapper = new LambdaQueryWrapper<>();
            rootWrapper.in(VideoComment::getRootId, parentIdList)
                    .orderBy(true, true, VideoComment::getId);
            List<VideoComment> childVideoCommentList = this.list(wrapper);
            //批量查询所有相关用户信息
            Set<Long> userIdList = list.stream().map(VideoComment::getUserId).collect(Collectors.toSet());
            Set<Long> replyUserIdList = childVideoCommentList.stream().map(VideoComment::getUserId).collect(Collectors.toSet());
            userIdList.addAll(replyUserIdList);
            List<UserInfo> userInfoList = userInfoService.getByUserIds(userIdList);
            //转为map 便于查询
            Map<Long, UserInfo> map = userInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId, userInfo -> userInfo));
            //参数填充
            list.forEach(comment->{
                Long id = comment.getId();
                List<VideoComment> childList = new ArrayList<>();
                childVideoCommentList.forEach(child -> {
                    if(id.equals(child.getRootId())) {
                        child.setUserInfo(map.get(child.getUserId()));
                        child.setReplyUserInfo(map.get(child.getReplyUserId()));
                        childList.add(child);
                    }
                });
                comment.setChildList(childList);
                comment.setUserInfo(map.get(comment.getUserId()));
            });
            pages.setRecords(list);
        }
        return pages;
    }

}




