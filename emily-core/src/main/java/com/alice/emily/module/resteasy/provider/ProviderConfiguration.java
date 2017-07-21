package com.alice.emily.module.resteasy.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.alice.emily.module.resteasy.validation.api.ResteasyViolationExceptionMapper;
import com.alice.emily.module.resteasy.validation.plugins.ValidatorContextResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderConfiguration {

    @Bean
    public ObjectMapperResolver jacksonObjectMapperResolver(ObjectMapper objectMapper) {
        return new ObjectMapperResolver(objectMapper);
    }

    @Bean
    public ValidatorContextResolver validatorContextResolver() {
        return new ValidatorContextResolver();
    }

    @Bean
    public ResteasyViolationExceptionMapper resteasyViolationExceptionMapper() {
        return new ResteasyViolationExceptionMapper();
    }

    @Bean
    @ConditionalOnProperty(prefix = "emily.resteasy", value = "httpCache", matchIfMissing = true)
    public HttpCacheInterceptor httpCacheInterceptor() {
        return new HttpCacheInterceptor();
    }
}