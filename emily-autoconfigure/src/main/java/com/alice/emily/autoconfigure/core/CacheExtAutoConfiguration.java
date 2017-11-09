package com.alice.emily.autoconfigure.core;

import com.alice.emily.core.AbstractBeanPostProcessorsRegistrar;
import com.alice.emily.core.ManagedCacheAnnotationBeanPostProcessor;
import org.infinispan.jcache.embedded.JCacheManager;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ObjectUtils;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.io.IOException;

/**
 * Created by lianhao on 2017/6/19.
 */
@Configuration
@ConditionalOnClass(CacheManager.class)
@EnableConfigurationProperties(CacheProperties.class)
@EnableCaching(proxyTargetClass = true)
@AutoConfigureBefore(CacheAutoConfiguration.class)
@AutoConfigureAfter({ CouchbaseAutoConfiguration.class, HazelcastAutoConfiguration.class,
        RedisAutoConfiguration.class })
@Import(CacheExtAutoConfiguration.ManagedCacheAnnotationBenPostProcessorRegistrar.class)
public class CacheExtAutoConfiguration {

    @Configuration
    @ConditionalOnClass(JCacheManager.class)
    @ConditionalOnProperty(prefix = "spring.cache.jcache", name = "provider",
            havingValue = "org.infinispan.jcache.embedded.JCachingProvider")
    static class InfinispanJCacheFixConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public JCacheManager jCacheCacheManager(CacheProperties cacheProperties) throws IOException {
            Resource configLocation = cacheProperties
                    .resolveConfigLocation(cacheProperties.getJcache().getConfig());
            CachingProvider cachingProvider = Caching
                    .getCachingProvider(cacheProperties.getJcache().getProvider());
            EmbeddedCacheManager cacheManager = new DefaultCacheManager(configLocation.getInputStream(), true);
            return new JCacheManager(configLocation.getURI(), cacheManager, cachingProvider);
        }

    }

    static class ManagedCacheAnnotationBenPostProcessorRegistrar
            extends AbstractBeanPostProcessorsRegistrar {

        @Override
        protected void doRegisterBeanDefinitions(AnnotationMetadata annotationMetadata,
                                                 BeanDefinitionRegistry registry) {
            Class<?> beanClass = ManagedCacheAnnotationBeanPostProcessor.class;
            if (ObjectUtils.isEmpty(
                    this.beanFactory.getBeanNamesForType(beanClass, true, false))) {
                RootBeanDefinition beanDefinition = new RootBeanDefinition(beanClass);
                beanDefinition.setSynthetic(true);
                registry.registerBeanDefinition("managedCacheAnnotationBeanPostProcessor", beanDefinition);
            }
        }

    }

}
