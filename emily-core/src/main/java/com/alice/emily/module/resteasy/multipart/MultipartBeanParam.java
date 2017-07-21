package com.alice.emily.module.resteasy.multipart;

import java.lang.annotation.*;

/**
 * Created by liupin on 2017/3/29.
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultipartBeanParam {
}
