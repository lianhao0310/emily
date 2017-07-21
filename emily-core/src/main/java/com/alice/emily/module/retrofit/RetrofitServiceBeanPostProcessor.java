package com.alice.emily.module.retrofit;

import com.alice.emily.core.AbstractAnnotationBeanPostProcessor;
import com.alice.emily.core.AnnotationFilter;
import com.alice.emily.utils.LOG;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * Created by liupin on 2017/3/31.
 */
public class RetrofitServiceBeanPostProcessor extends AbstractAnnotationBeanPostProcessor {

    private final RetrofitServiceFactory serviceFactory;

    public RetrofitServiceBeanPostProcessor(RetrofitServiceFactory serviceFactory) {
        super(Members.FIELDS, Phase.PRE_INIT, new AnnotationFilter(RetrofitService.class, AnnotationFilter.INJECTABLE_FIELDS));
        this.serviceFactory = serviceFactory;
    }

    @Override
    protected void withField(Object bean, String beanName, Class<?> targetClass, Field field) {
        if (!field.getType().isInterface()) {
            throw new IllegalArgumentException("@RetrofitService only supported interface: " + field.getType().getName());
        }
        ReflectionUtils.makeAccessible(field);
        Object service = ReflectionUtils.getField(field, bean);
        if (service == null) {
            RetrofitService annotation = field.getAnnotation(RetrofitService.class);
            service = serviceFactory.createServiceInstance(field.getType(), annotation.value());
            ReflectionUtils.setField(field, bean, service);
            LOG.REST.debug("Injected retrofit client {}:{} for field {}.{}", annotation.value(), field.getType().getName(),
                    targetClass.getCanonicalName(), field.getName());
        }
    }
}
