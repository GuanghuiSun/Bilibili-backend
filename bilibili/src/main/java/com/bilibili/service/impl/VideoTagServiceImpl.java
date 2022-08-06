package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.VideoTag;
import com.bilibili.service.VideoTagService;
import com.bilibili.mapper.VideoTagMapper;
import org.springframework.stereotype.Service;

/**
 * @author sgh
 * @description 针对表【t_video_tag(视频标签关联表)】的数据库操作Service实现
 * @createDate 2022-08-06 17:12:44
 */
@Service
public class VideoTagServiceImpl extends ServiceImpl<VideoTagMapper, VideoTag>
        implements VideoTagService {

}




