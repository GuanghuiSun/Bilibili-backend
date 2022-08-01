package com.bilibili.handler;

import com.bilibili.base.BaseResponse;
import com.bilibili.base.ErrorCode;
import com.bilibili.base.ResultUtils;
import com.bilibili.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理器
 *
 * @author sgh
 * @date 2022-8-1
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)//Spring容器内bean的执行优先级
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param e   业务异常类
     * @param <T> 返回数据
     * @return 通用响应体
     */
    @ExceptionHandler(BusinessException.class)
    public <T> BaseResponse<T> businessExceptionHandler(BusinessException e) {
        log.error("businessException:" + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    public <T> BaseResponse<T> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException:" + e.getMessage(), e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }
}
