package com.alice.emily.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lianhao on 2017/2/6.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AlertIf {

    Class<? extends Throwable>[] value() default Throwable.class;

    String desc() default "";

    String[] type();
}
