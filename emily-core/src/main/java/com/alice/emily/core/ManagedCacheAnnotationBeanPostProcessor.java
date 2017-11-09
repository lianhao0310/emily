package com.alice.emily.core;

import com.alice.emily.annotation.ManagedCache;
import com.alice.emily.utils.LOG;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * Created by lianhao on 2017/6/19.
 */
public class ManagedCacheAnnotationBeanPostProcessor extends AbstractAnnotationBeanPostProcessor {

    private final CacheManager cacheManager;

    public ManagedCacheAnnotationBeanPostProcessor(CacheManager cacheManager) {
        super(Members.FIELDS, Phase.PRE_INIT,
                new AnnotationFilter(ManagedCache.class, AnnotationFilter.INJECTABLE_FIELDS));
        this.cacheManager = cacheManager;
    }

    @Override
    protected void withField(Object bean, String beanName, Class<?> targetClass, Field field) {
        ReflectionUtils.makeAccessible(field);
        Object value = ReflectionUtils.getField(field, bean);
        if (value != null) {
            return;
        }
        ManagedCache annotation = field.getAnnotation(ManagedCache.class);
        Class<?> type = field.getType();
        Cache cache = cacheManager.getCache(annotation.value());
        if (cache == null) {
            LOG.CACHE.warn("Cache {} cannot be retrieved, Please check your configuration", annotation.value());
            return;
        }
        if (type.isAssignableFrom(cache.getClass())) {
            ReflectionUtils.setField(field, bean, cache);
        } else if (type.isAssignableFrom(cache.getNativeCache().getClass())) {
            ReflectionUtils.setField(field, bean, cache.getNativeCache());
        } else {
            LOG.CACHE.warn("Unsupported cache {} with type {}", annotation.value(), type);
        }
    }
}
