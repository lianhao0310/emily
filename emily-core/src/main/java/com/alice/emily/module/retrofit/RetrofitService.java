package com.alice.emily.module.retrofit;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates an bean field as Retrofit service.
 * <p>
 * Use this annotation to inject a Retrofit annotated interface for manage bean.
 *
 * @author lianhao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Component
public @interface RetrofitService {

    String value();
}