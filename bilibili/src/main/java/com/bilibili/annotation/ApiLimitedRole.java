package com.bilibili.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 接口权限校验注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Component
public @interface ApiLimitedRole {

    String[] limitedRoleCodeList() default {};
}
