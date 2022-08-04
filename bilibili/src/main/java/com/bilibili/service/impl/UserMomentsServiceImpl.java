package com.bilibili.service.impl;

import java.util.ArrayList;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.UserMoments;
import com.bilibili.model.result.ScrollResult;
import com.bilibili.service.UserMomentsService;
import com.bilibili.mapper.UserMomentsMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;

import static com.bilibili.base.ErrorCode.PUT_SERVICE_ERROR;
import static com.bilibili.constant.KafkaConstant.MOMENTS_TOPIC;
import static com.bilibili.constant.MessageConstant.PUT_MOMENTS_ERROR;
import static com.bilibili.constant.RedisConstant.SUBSCRIBED_INBOX;

/**
 * @author sgh
 * @description 针对表【t_user_moments(用户动态表)】的数据库操作Service实现
 * @createDate 2022-08-03 20:46:12
 */
@Service
public class UserMomentsServiceImpl extends ServiceImpl<UserMomentsMapper, UserMoments>
        implements UserMomentsService {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addUserMoments(UserMoments userMoments) {
        //保存动态
        boolean result = this.save(userMoments);
        if (!result) {
            throw new BusinessException(PUT_SERVICE_ERROR, PUT_MOMENTS_ERROR);
        }
        //发送消息给粉丝
        kafkaTemplate.send(MOMENTS_TOPIC, userMoments).addCallback(
                success -> log.debug("动态消息发送成功"),
                failure -> log.error("消息发送失败"));
    }

    @Override
    public ScrollResult getUserSubscribedMoments(Long userId, Long max, Long offset, Long size) {
        String key = SUBSCRIBED_INBOX + ":" + userId;
        //分数的最大值（max），分数的最小值（min），偏移量（offset），数量（size）
        Set<ZSetOperations.TypedTuple<String>> typedTuples =
                stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, -1, max, offset, size);
        if (typedTuples == null || typedTuples.isEmpty()) {
            return new ScrollResult(Collections.emptyList(), offset, max, size);
        }
        List<Long> ids = new ArrayList<>(typedTuples.size());
        //最小时间
        long minTime = 0;
        //偏移量
        long os = 1;
        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
            //获取momentsId
            ids.add(Long.valueOf(Objects.requireNonNull(typedTuple.getValue())));
            //获取时间戳
            long time = Objects.requireNonNull(typedTuple.getScore()).longValue();
            if (time == minTime) {
                ++os;
            } else {
                minTime = time;
                os = 1;
            }
        }
        //根据id获取moments
        String idStr = CharSequenceUtil.join(",", ids);
        List<UserMoments> userMoments = query().in("id", ids).last("ORDER BY FIELD(id, " + idStr + ")").list();
        //封装为结果集
        ScrollResult result = new ScrollResult();
        result.setData(userMoments);
        result.setOffset(os);
        result.setMinTime(minTime);
        result.setSize(size);
        return result;
    }
}




