package com.bilibili.aspect;

import com.bilibili.annotation.ApiLimitedRole;
import com.bilibili.exception.BusinessException;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bilibili.base.ErrorCode.AUTH_ERROR;

/**
 * 接口权限控制切面
 */
@Order(1)
@Component
@Aspect
public class ApiLimitedRoleAspect {

    @Resource
    private UserSupport userSupport;

    @Resource
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.bilibili.annotation.ApiLimitedRole)")
    public void checkAuth() {

    }

    @Before("checkAuth() && @annotation(apiLimitedRole)")
    public void doBefore(JoinPoint joinPoint, ApiLimitedRole apiLimitedRole) {
        Long userId = userSupport.getCurrentUserId();
        //获取当前用户的角色code
        List<UserRole> userRoleList = userRoleService.getUserRoles(userId);
        //获取被限制角色
        String[] limitedRoleCodeList = apiLimitedRole.limitedRoleCodeList();
        Set<String> limitedRoleCodeSet = Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());
        Set<String> roleCodeSet = userRoleList.stream().map(x -> x.getAuthRole().getCode()).collect(Collectors.toSet());
        //检查当前用户所拥有的的角色和可操作权限列表是否有交集
        //api:当前集合保留包含在limitedRoleCodeSet当中的元素
        roleCodeSet.retainAll(limitedRoleCodeSet);
        if(!roleCodeSet.isEmpty()) {
            throw new BusinessException(AUTH_ERROR);
        }

    }

}
