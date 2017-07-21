package com.alice.emily.module.command;

import java.lang.annotation.*;

/**
 * Created by lianhao on 2016/11/23.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CommandIgnore {
}
