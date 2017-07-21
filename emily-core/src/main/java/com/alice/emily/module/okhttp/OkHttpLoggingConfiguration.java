package com.alice.emily.module.okhttp;

import com.google.common.base.Strings;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lianhao on 2017/3/31.
 */
@Configuration
public class OkHttpLoggingConfiguration {

    private final OkHttpProperties properties;

    public OkHttpLoggingConfiguration(OkHttpProperties properties) {
        this.properties = properties;
    }

    @Bean
    @OkHttpInterceptor
    @ConditionalOnMissingBean(HttpLoggingInterceptor.class)
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor;

        if (!Strings.isNullOrEmpty(properties.getLogging().getLoggerName())) {
            Logger log = LogManager.getLogger(properties.getLogging().getLoggerName());
            HttpLoggingInterceptor.Logger logger = message -> log.log(properties.getLogging().getLoggerLevel(), message);
            interceptor = new HttpLoggingInterceptor(logger);
        } else {
            interceptor = new HttpLoggingInterceptor();
        }

        interceptor.setLevel(properties.getLogging().getLevel());
        return interceptor;
    }

}
