package com.alice.emily.module.aop;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.alice.emily.module.mail.MailSender;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liupin on 2017/2/6.
 */
@Aspect
public class AlertIfAspect {

    private static final Map<Integer, Long> STACK_TIMESTAMPS = Maps.newConcurrentMap();
    private final MailSender mailSender;

    public AlertIfAspect(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Around("@within(alertIf) || @annotation(alertIf)")
    public Object process(ProceedingJoinPoint ctx, AlertIf alertIf) throws Throwable {
        if (alertIf == null) {
            MethodSignature signature = (MethodSignature) ctx.getSignature();
            alertIf = signature.getMethod().getAnnotation(AlertIf.class);
        }
        if (alertIf == null) {
            alertIf = ctx.getTarget().getClass().getAnnotation(AlertIf.class);
        }
        try {
            return ctx.proceed();
        } catch (Throwable throwable) {
            Class<? extends Throwable> eClass = throwable.getClass();
            //获取注解参数
            String[] group = alertIf.group();
            Class<? extends Throwable>[] classes = alertIf.value();
            String title = alertIf.title();
            //判断是否在需要处理的异常之内
            for (Class<? extends Throwable> aClass : classes) {
                //是
                if (aClass.isAssignableFrom(eClass)) {
                    if (timeUp()) {
                        sendMail(ctx, throwable, group, title);
                    }
                    break;
                }
            }
            throw throwable;
        }
    }

    private boolean timeUp() {
        //控制发送间隔
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int hashCode = Arrays.hashCode(stackTraceElements);
        Long lastTime = STACK_TIMESTAMPS.get(hashCode);
        long currentTime = System.currentTimeMillis();
        if (lastTime != null && currentTime - lastTime < 60 * 1000) {
            return false;
        }
        STACK_TIMESTAMPS.put(hashCode, currentTime);
        return true;
    }

    private void sendMail(ProceedingJoinPoint ctx, Throwable throwable, String[] group, String title) {
        Map<String, Object> model = Maps.newHashMap();
        MethodSignature signature = (MethodSignature) ctx.getSignature();
        Method method = signature.getMethod();

        // method
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
        model.put("stackTrace", Throwables.getStackTraceAsString(throwable));
        mailSender.send(title, "alert_if.html", model, group);
    }
}
