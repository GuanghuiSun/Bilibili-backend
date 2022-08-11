package com.bilibili.websocket;

import com.alibaba.fastjson.JSONObject;
import com.bilibili.model.domain.BulletScreenComments;
import com.bilibili.service.BulletScreenCommentsService;
import com.bilibili.utils.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.bilibili.constant.KafkaConstant.BSC_SEND_TOPIC;

/**
 * 处理websocket
 *
 * @author sgh
 * @date 2022-8-8
 */
@Component
@ServerEndpoint("/myServer/{token}")
public class WebSocketService {

    /**
     * 记录日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 原子整型 用于多线程环境下记录连接数
     */
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    /**
     * 哈希表 多线程环境下存储每个客户端对应的webSocketService
     * 因为默认是单例注入，所以我们需要自己去创建一些service
     */
    public static final ConcurrentHashMap<String, WebSocketService> WEBSOCKET_MAP = new ConcurrentHashMap<>();

    /**
     * 客户端session 从websocketService中获取
     */
    private Session session;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 客户端唯一标识
     */
    private String sessionId;

    /**
     * WebSocketService是多例的，但是使用stringRedisTemplate是单例的，只会注入一次，
     * 所以只会有一个webSocketService获得了redis，其他的不会重复注入，都为null
     */
    private static ApplicationContext APPLICATION_CONTEXT;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketService.APPLICATION_CONTEXT = applicationContext;
    }

    /**
     * 连接成功调用该方法
     *
     * @param session 会话
     * @param token   用户token
     */
    @OnOpen
    public void openConnection(Session session, @PathParam("token") String token) {
        try {
            this.userId = TokenUtils.verifyToken(token);
        } catch (Exception ignored) {
        }
        this.sessionId = session.getId();
        this.session = session;
        if (WEBSOCKET_MAP.containsKey(sessionId)) {
            WEBSOCKET_MAP.remove(sessionId);
            WEBSOCKET_MAP.put(sessionId, this);
        } else {
            WEBSOCKET_MAP.put(sessionId, this);
            ONLINE_COUNT.getAndIncrement();
        }
        logger.info("用户连接成功：" + sessionId + ", 当前在线人数:" + ONLINE_COUNT.get());
        //向前端发送连接确认消息
        try {
            this.sendMessage("200");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 断开连接时调用
     */
    @OnClose
    public void closeConnection() {
        if (WEBSOCKET_MAP.containsKey(this.sessionId)) {
            WEBSOCKET_MAP.remove(sessionId);
            ONLINE_COUNT.decrementAndGet();
        }
        logger.info("用户退出:" + sessionId + ", 当前在线人数:" + ONLINE_COUNT.get());
    }

    /**
     * 获取到消息
     *
     * @param message 消息
     */
    @OnMessage
    public void onMessage(String message) {
        logger.info("用户信息:" + sessionId + "报文:" + message);
        if (!StringUtils.isBlank(message)) {
            //群发消息
            for (Map.Entry<String, WebSocketService> entry : WEBSOCKET_MAP.entrySet()) {
                WebSocketService webSocketService = entry.getValue();
                //异步发送弹幕消息
                JSONObject json = new JSONObject();
                json.put("message", message);
                json.put("sessionId", webSocketService.getSessionId());
                KafkaTemplate<String, Object> kafkaTemplate = (KafkaTemplate<String, Object>) APPLICATION_CONTEXT.getBean("kafkaTemplate");
                kafkaTemplate.send(BSC_SEND_TOPIC, json)
                        .addCallback(success -> logger.debug("弹幕消息发送成功"),
                                failure -> logger.debug("弹幕消息发送失败"));
            }
            //保存弹幕
            if (userId != null) {
                BulletScreenComments comment = JSONObject.parseObject(message, BulletScreenComments.class);
                comment.setUserId(userId);
                BulletScreenCommentsService bulletScreenCommentsService =
                        (BulletScreenCommentsService) APPLICATION_CONTEXT.getBean("bulletScreenCommentsService");
                bulletScreenCommentsService.saveBulletScreenComments(comment);
            }
        }
    }

    @OnError
    public void onError(Throwable error) {
        logger.error(error.getMessage());
        error.printStackTrace();
    }

    /**
     * 定时发送在线观看人数
     */
    @Scheduled(fixedRate = 5000)
    public void noticeOnlineCount() {
        try {
            for (Map.Entry<String, WebSocketService> entry : WEBSOCKET_MAP.entrySet()) {
                WebSocketService webSocketService = entry.getValue();
                if (webSocketService.session.isOpen()) {
                    JSONObject json = new JSONObject();
                    json.put("onlineCount", ONLINE_COUNT.get());
                    json.put("message", "当前在线观看人数:" + ONLINE_COUNT.get());
                    webSocketService.sendMessage(json.toJSONString());
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public Session getSession() {
        return session;
    }

    public String getSessionId() {
        return sessionId;
    }
}
