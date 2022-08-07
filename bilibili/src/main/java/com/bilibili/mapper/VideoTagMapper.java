package com.bilibili.mapper;

import com.bilibili.model.domain.VideoTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sgh
 * @description 针对表【t_video_tag(视频标签关联表)】的数据库操作Mapper
 * @createDate 2022-08-06 17:12:44
 * @Entity com.bilibili.model.domain.VideoTag
 */
public interface VideoTagMapper extends BaseMapper<VideoTag> {

    void batchAddVideoTags(@Param("videoTagList") List<VideoTag> videoTagList);

}




