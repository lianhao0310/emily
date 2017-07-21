package com.alice.emily.autoconfigure;

import com.alice.emily.module.cache.InfinispanCacheBeanPostProcessor;
import org.infinispan.commons.api.BasicCache;
import org.infinispan.manager.CacheContainer;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liupin on 2017/2/6.
 */
@Configuration
@ConditionalOnClass({ BasicCache.class, CacheContainer.class })
@EnableCaching
@ImportAutoConfiguration({ CacheAutoConfiguration.class, InfinispanCacheConfiguration.class })
public class InfinispanAutoConfiguration {

    @Bean
    public InfinispanCacheBeanPostProcessor infinispanCacheBeanPostProcessor() {
        return new InfinispanCacheBeanPostProcessor();
    }
}