package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.Tag;
import com.bilibili.service.TagService;
import com.bilibili.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
 * @author sgh
 * @description 针对表【t_tag(标签信息表)】的数据库操作Service实现
 * @createDate 2022-08-06 17:12:33
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements TagService {

}




