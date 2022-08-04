package com.bilibili.aspect;

import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.UserMoments;
import com.bilibili.model.domain.UserRole;
import com.bilibili.service.UserRoleService;
import com.bilibili.support.UserSupport;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bilibili.base.ErrorCode.AUTH_ERROR;
import static com.bilibili.constant.MessageConstant.MOMENTS_AUTH_ERROR;
import static com.bilibili.constant.user.AuthRoleConstant.ROLE_CODE_LV0;

/**
 * 数据权限控制切面
 */
@Order(1)
@Component
@Aspect
public class DataLimitedAspect {

    @Resource
    private UserSupport userSupport;

    @Resource
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.bilibili.annotation.DataLimited)")
    public void checkAuth() {

    }

    @Before("checkAuth()")
    public void doBefore(JoinPoint joinPoint) {
        Long userId = userSupport.getCurrentUserId();
        //获取当前用户的角色code
        List<UserRole> userRoleList = userRoleService.getUserRoles(userId);
        Set<String> roleCodeSet = userRoleList.stream().map(x -> x.getAuthRole().getCode()).collect(Collectors.toSet());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if(arg instanceof UserMoments) {
                UserMoments userMoments = (UserMoments) arg;
                Byte type = userMoments.getType();
                //判断角色和发布动态的类型
                if(roleCodeSet.contains(ROLE_CODE_LV0) && type != null &&  0 != type) {
                    throw new BusinessException(AUTH_ERROR, MOMENTS_AUTH_ERROR);
                }
            }
        }

    }

}
