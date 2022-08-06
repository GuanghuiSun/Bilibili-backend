package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.Video;
import com.bilibili.service.VideoService;
import com.bilibili.mapper.VideoMapper;
import org.springframework.stereotype.Service;

/**
 * @author sgh
 * @description 针对表【t_video(视频投稿记录表)】的数据库操作Service实现
 * @createDate 2022-08-06 17:12:38
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
        implements VideoService {

}




