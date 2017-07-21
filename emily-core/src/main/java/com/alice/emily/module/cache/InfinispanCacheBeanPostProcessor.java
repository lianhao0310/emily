package com.alice.emily.module.cache;

import com.alice.emily.core.AbstractAnnotationBeanPostProcessor;
import com.alice.emily.core.AnnotationFilter;
import com.alice.emily.utils.LOG;
import org.infinispan.commons.api.BasicCache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by liupin on 2017/2/21.
 */
public class InfinispanCacheBeanPostProcessor extends AbstractAnnotationBeanPostProcessor {

    @Autowired
    private EmbeddedCacheManager cacheManager;

    public InfinispanCacheBeanPostProcessor() {
        super(Members.FIELDS, Phase.PRE_INIT, new AnnotationFilter(InfinispanCache.class, AnnotationFilter.INJECTABLE_FIELDS));
    }

    @Override
    protected void withField(Object bean, String beanName, Class<?> targetClass, Field field) {
        InfinispanCache annotation = field.getAnnotation(InfinispanCache.class);
        // check field type
        Class<?> type = field.getType();
        if (!BasicCache.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Field " + targetClass.getCanonicalName() + "." + field.getName() + " must be a subtype of "
                    + BasicCache.class.getCanonicalName());
        }
        // read field value
        ReflectionUtils.makeAccessible(field);
        BasicCache cache = (BasicCache) ReflectionUtils.getField(field, bean);
        if (cache != null) return;
        // get or create cache
        cache = cacheManager.getCache(annotation.value());
        if (cache == null) {
            if (!annotation.createIfAbsent()) {
                LOG.CACHE.warn("Cache {} should be either defined in XML or turn on the createIfAbsent switch", annotation.value());
                return;
            }
            if (StringUtils.isEmpty(annotation.template())) {
                cacheManager.defineConfiguration(annotation.value(), null);
            } else {
                cacheManager.defineConfiguration(annotation.value(), annotation.template(), null);
            }
            cache = cacheManager.getCache(annotation.value(), true);
        }
        ReflectionUtils.setField(field, bean, cache);
        LOG.CACHE.debug("Injected infinispan cache {} for field {}.{}", annotation.value(), targetClass.getCanonicalName(), field.getName());
    }
}
