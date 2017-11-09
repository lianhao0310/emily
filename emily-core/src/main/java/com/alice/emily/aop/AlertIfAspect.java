package com.alice.emily.aop;

import com.alice.emily.annotation.AlertIf;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * Created by lianhao on 2017/2/6.
 */
@Aspect
public class AlertIfAspect extends AbstractAspect {

    @Around("@annotation(alertIf) || @within(alertIf)")
    public Object process(ProceedingJoinPoint ctx, AlertIf alertIf) throws Throwable {
        alertIf = alertIf == null ? getAnnotation(ctx, AlertIf.class) : alertIf;
        try {
            return ctx.proceed();
        } catch (Throwable throwable) {
            Class<? extends Throwable> eClass = throwable.getClass();
            // 获取注解参数
            String[] type = alertIf.type();
            Class<? extends Throwable>[] classes = alertIf.value();
            String desc = alertIf.desc();
            // 判断是否在需要处理的异常之内
            for (Class<? extends Throwable> aClass : classes) {
                // 是
                if (aClass.isAssignableFrom(eClass)) {
                    fireEvent(ctx, throwable, type, desc);
                    break;
                }
            }
            throw throwable;
        }
    }

    private void fireEvent(ProceedingJoinPoint ctx, Throwable throwable, String[] type, String desc) {
        MethodSignature signature = (MethodSignature) ctx.getSignature();
        Method method = signature.getMethod();
        if (type != null && type.length > 0) {
            for (String t : type) {
                AlertEvent event = AlertEvent.builder()
                        .type(t)
                        .desc(desc)
                        .method(method)
                        .args(ctx.getArgs())
                        .throwable(throwable)
                        .build();
                publisher.publishEvent(event);
            }
        }

/*        // method
        model.put("methodName", method.getName());
        model.put("className", method.getDeclaringClass().getName());
        model.put("returnType", method.getReturnType().getName());

        // parameter
        Parameter[] parameters = method.getParameters();
        List<String> parameterNameList = Arrays.stream(parameters).map(Parameter::getName).collect(Collectors.toList());
        List<String> parameterTypeList = Arrays.stream(method.getParameters())
                .map(it -> it.getType().getSimpleName())
                .collect(Collectors.toList());
        model.put("parameterNames", parameterNameList);
        model.put("parameterTypes", parameterTypeList);
        model.put("parameterValues", ctx.getArgs());

        // stack trace
        model.put("stackTrace", Throwables.getStackTraceAsString(throwable));*/
    }
}