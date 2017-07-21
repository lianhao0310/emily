package com.alice.emily.module.command;

import java.lang.annotation.*;

/**
 * Created by lianhao on 2016/12/14.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Commands {

    String category() default "default";

    DiscoverMode mode() default DiscoverMode.PUBLIC_METHOD;
}
