package com.bilibili.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.BulletScreenComments;
import com.bilibili.service.BulletScreenCommentsService;
import com.bilibili.mapper.BulletScreenCommentsMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.bilibili.base.ErrorCode.PARAM_ERROR;
import static com.bilibili.constant.KafkaConstant.BSC_SAVE_TOPIC;
import static com.bilibili.constant.RedisConstant.BULLET_SCREEN_COMMENTS;

/**
 * @author sgh
 * @description 针对表【t_bullet_screen_comments(弹幕记录表)】的数据库操作Service实现
 * @createDate 2022-08-08 16:04:57
 */
@Service
public class BulletScreenCommentsServiceImpl extends ServiceImpl<BulletScreenCommentsMapper, BulletScreenComments>
        implements BulletScreenCommentsService {

    @Resource
    private BulletScreenCommentsMapper bulletScreenCommentsMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void saveBulletScreenComments(BulletScreenComments bulletScreenComments) {
        String key = BULLET_SCREEN_COMMENTS + ":" + bulletScreenComments.getVideoId();
        //保存到redis 创建时间 转为时间戳作为score排序
        stringRedisTemplate.opsForZSet().add(key, JSONUtil.toJsonStr(bulletScreenComments), bulletScreenComments.getCreateTime().getTime());
        //发送消息到kafka异步存储到数据库中
        kafkaTemplate.send(BSC_SAVE_TOPIC, bulletScreenComments)
                .addCallback(success -> log.debug("弹幕存储成功"),
                        failure -> log.error("弹幕存储失败"));
    }

    @Override
    public List<BulletScreenComments> getBulletScreenComments(Long videoId, String startTime, String endTime) {
        if (videoId == null || startTime == null || endTime == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        try {
            //查询redis
            String key = BULLET_SCREEN_COMMENTS + ":" + videoId;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long begin = sdf.parse(startTime).getTime();
            long end = sdf.parse(endTime).getTime();
            Set<ZSetOperations.TypedTuple<String>> typedTuples =
                    stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, begin, end);
            List<BulletScreenComments> result;
            if (typedTuples != null && !typedTuples.isEmpty()) {
                result = typedTuples.stream()
                        .map(x -> JSONUtil.toBean(x.getValue(), BulletScreenComments.class))
                        .sorted((o1, o2) -> o2.getCreateTime().after(o1.getCreateTime()) ? 1 : -1)
                        .collect(Collectors.toList());
                return result;
            }
            //redis没有查询数据库
            Map<String, Object> param = new HashMap<>();
            param.put("videoId", videoId);
            param.put("startTime", startTime);
            param.put("endTime", endTime);
            result = bulletScreenCommentsMapper.getBulletScreenComments(param);
            //存到redis
            Set<ZSetOperations.TypedTuple<String>> collect = result.stream()
                    .map(x -> ZSetOperations.TypedTuple.of(JSONUtil.toJsonStr(x), Convert.convert(double.class, x.getCreateTime().getTime())))
                    .collect(Collectors.toSet());
            stringRedisTemplate.opsForZSet().add(key, collect);
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}



