package com.bilibili.support;

import com.bilibili.exception.BusinessException;
import com.bilibili.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.bilibili.base.ErrorCode.GET_SERVICE_ERROR;
import static com.bilibili.constant.MessageConstant.TOKEN_DECODE_ERROR;

/**
 * 用户支持
 *
 * @author sgh
 * @date 2022-8-2
 */
@Component
@Slf4j
public class UserSupport {

    /**
     * 获取当前请求用户的userId
     * @return userId
     */
    public Long getCurrentUserId() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = null;
        if (requestAttributes != null) {
            token = requestAttributes.getRequest().getHeader("token");
        }
        log.debug(token);
        log.debug(String.valueOf(requestAttributes.getRequest()));
        System.out.println(token);
        Long userId = TokenUtils.verifyToken(token);
        if (userId < 0) {
            throw new BusinessException(GET_SERVICE_ERROR, TOKEN_DECODE_ERROR);
        }
        return userId;
    }
}
