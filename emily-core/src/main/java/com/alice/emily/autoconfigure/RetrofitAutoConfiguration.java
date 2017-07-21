package com.alice.emily.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.alice.emily.module.retrofit.RetrofitProperties;
import com.alice.emily.module.retrofit.RetrofitServiceBeanPostProcessor;
import com.alice.emily.module.retrofit.RetrofitServiceFactory;
import com.alice.emily.module.retrofit.RetrofitServiceFactoryImpl;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by lianhao on 2017/3/30.
 */
@Configuration
@ConditionalOnClass({ Retrofit.class, OkHttpClient.class })
@AutoConfigureAfter({ OkHttpAutoConfiguration.class, JacksonExtAutoConfiguration.class })
@EnableConfigurationProperties(RetrofitProperties.class)
public class RetrofitAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RetrofitServiceFactory retrofitServiceFactory(OkHttpClient okHttpClient, RetrofitProperties properties) {
        return new RetrofitServiceFactoryImpl(okHttpClient, properties);
    }

    @Bean
    public static RetrofitServiceBeanPostProcessor retrofitServiceBeanPostProcessor(RetrofitServiceFactory retrofitServiceFactory) {
        return new RetrofitServiceBeanPostProcessor(retrofitServiceFactory);
    }

    @Configuration
    @ConditionalOnClass(Java8CallAdapterFactory.class)
    public static class Java8CallAdapterFactoryConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public Java8CallAdapterFactory java8CallAdapterFactory() {
            return Java8CallAdapterFactory.create();
        }
    }

    @Configuration
    @ConditionalOnClass(GuavaCallAdapterFactory.class)
    public static class GuavaCallAdapterFactoryConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public GuavaCallAdapterFactory guavaCallAdapterFactory() {
            return GuavaCallAdapterFactory.create();
        }
    }

    @Configuration
    @ConditionalOnClass(RxJava2CallAdapterFactory.class)
    public static class RxJava2CallAdapterFactoryConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public RxJava2CallAdapterFactory rxJava2CallAdapterFactory() {
            return RxJava2CallAdapterFactory.create();
        }
    }

    @Configuration
    @ConditionalOnClass(ScalarsConverterFactory.class)
    public static class ScalarsConverterFactoryConfiguration {

        @Bean
        @Order()
        @ConditionalOnMissingBean
        public ScalarsConverterFactory scalarsConverterFactory() {
            return ScalarsConverterFactory.create();
        }
    }

    @Configuration
    @ConditionalOnClass(JacksonConverterFactory.class)
    public static class JacksonConverterFactoryConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public JacksonConverterFactory jacksonConverterFactory(ObjectMapper objectMapper) {
            return JacksonConverterFactory.create(objectMapper);
        }
    }
}
