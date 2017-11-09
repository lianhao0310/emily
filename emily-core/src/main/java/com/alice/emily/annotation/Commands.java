package com.alice.emily.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liupin on 2016/12/14.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Component
public @interface Commands {

    String category() default "default";

    DiscoverMode mode() default DiscoverMode.PUBLIC_METHOD;

    enum DiscoverMode {
        PUBLIC_METHOD, ANNOTATED_METHOD
    }
}
