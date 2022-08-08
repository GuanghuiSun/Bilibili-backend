package com.bilibili.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.bilibili.constant.KafkaConstant.*;

/**
 * kafka配置
 *
 * @author sgh
 * @date 2022-8-3
 */
@Configuration
public class KafkaConfig {

    /**
     * 设置动态分区
     *
     * @return topic
     */
    @Bean
    public NewTopic momentsTopic() {
        return new NewTopic(MOMENTS_TOPIC, 3, (short) 2);
    }

    /**
     * 设置弹幕发送分区
     *
     * @return topic
     */
    @Bean
    public NewTopic bulletScreenCommentsTopic() {
        return new NewTopic(BSC_SEND_TOPIC, 3, (short) 2);
    }
}
