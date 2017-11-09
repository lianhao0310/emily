package com.alice.emily.curator.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lianhao on 2017/6/13.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CuratorCacheListener {

    String[] value();

    CuratorCacheType type() default CuratorCacheType.NODE;
}
