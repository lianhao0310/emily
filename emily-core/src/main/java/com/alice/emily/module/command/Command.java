package com.alice.emily.module.command;

import java.lang.annotation.*;

/**
 * Created by lianhao on 2016/11/9.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Command {
    String action() default "default";

    String category() default "default";

    String usage() default "";
}
