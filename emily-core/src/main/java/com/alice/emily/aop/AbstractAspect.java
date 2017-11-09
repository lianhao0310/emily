package com.alice.emily.aop;

import com.google.common.base.Preconditions;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by lianhao on 2017/5/2.
 */
public abstract class AbstractAspect {

    @Autowired
    protected ApplicationEventPublisher publisher;

    protected <A extends Annotation> A getAnnotation(JoinPoint joinPoint, Class<A> type) {
        Preconditions.checkNotNull(joinPoint);
        Preconditions.checkNotNull(type);
        A annotation = null;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        if (signature != null) {
            Method method = signature.getMethod();
            annotation = AnnotationUtils.findAnnotation(method, type);
        }
        Object target = joinPoint.getTarget();
        if (target != null && annotation == null) {
            annotation = AnnotationUtils.findAnnotation(target.getClass(), type);
        }
        return annotation;
    }
}