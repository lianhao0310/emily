package com.alice.emily.module.aop;

import java.lang.annotation.*;

/**
 * Created by liupin on 2017/2/6.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AlertIf {

    Class<? extends Throwable>[] value() default Throwable.class;

    String title() default "";

    String[] group();
}
