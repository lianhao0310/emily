package com.alice.emily.resteasy.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.alice.emily.resteasy.pageable.PageableReader;
import com.alice.emily.resteasy.pageable.SortReader;
import com.alice.emily.resteasy.principal.AuthenticationPrincipalReader;
import com.alice.emily.resteasy.validation.api.ResteasyViolationExceptionMapper;
import com.alice.emily.resteasy.validation.plugins.ValidatorContextResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.expression.BeanResolver;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

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
    @ConditionalOnProperty(prefix = "emily.resteasy", value = "http-cache")
    public HttpCacheInterceptor httpCacheInterceptor() {
        return new HttpCacheInterceptor();
    }

    @Configuration
    @ConditionalOnClass({ Pageable.class, PageableDefault.class, SortDefault.class })
    public static class PageableReaderConfiguration {

        @Bean
        public PageableReader pageableReader() {
            return new PageableReader();
        }

        @Bean
        public SortReader sortReader() {
            return new SortReader();
        }
    }

    @Configuration
    @ConditionalOnClass({ AuthenticationPrincipal.class })
    public static class SecurityPrincipalConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public AuthenticationPrincipalReader authenticationPrincipalReader(BeanResolver beanResolver) {
            return new AuthenticationPrincipalReader(beanResolver);
        }
    }
}