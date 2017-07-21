package com.alice.emily.module.cache;

import java.lang.annotation.*;

/**
 * Created by lianhao on 2017/2/21.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InfinispanCache {
    String value();
    String template() default "";
    boolean createIfAbsent() default true;
}
