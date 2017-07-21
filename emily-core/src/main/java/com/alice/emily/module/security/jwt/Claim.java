package com.alice.emily.module.security.jwt;

import java.lang.annotation.*;

/**
 * Created by liupin on 2017/4/7.
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Claim {

    String value() default "";
}
