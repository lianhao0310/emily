package com.alice.emily.module.aop;

import com.google.common.base.Stopwatch;
import com.alice.emily.utils.LOG;
import org.apache.logging.log4j.Level;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by lianhao on 2017/2/6.
 */
@Aspect
public class AuditAspect {

    @Around("@annotation(audit) || @within(audit)")
    public Object process(ProceedingJoinPoint ctx, Audit audit) throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        if (audit == null) {
            MethodSignature signature = (MethodSignature) ctx.getSignature();
            audit = signature.getMethod().getAnnotation(Audit.class);
        }
        String status = "SUCCESS";
        try {
            return ctx.proceed();
        } catch (Throwable e) {
            status = "FAIL";
            throw e;
        } finally {
            Level level = LOG.parseLevel(audit.value());
            LOG.AUDIT.log(level, format(ctx, status, stopwatch));
        }
    }

    private String format(ProceedingJoinPoint ctx, String status, Stopwatch stopwatch) {
        return String.format("[AUDIT] [%7s] [%10s]: %s", status, stopwatch.stop().toString(), format(ctx));
    }

    private String format(ProceedingJoinPoint ctx) {
        Object target = ctx.getTarget();
        Class<?> targetClass = target.getClass();
        String className = targetClass.getCanonicalName();
        String method = ctx.getSignature().getName();
        String params = "";
        if (ctx.getArgs() != null && ctx.getArgs().length > 0) {
            params = Stream.of(ctx.getArgs()).map(String::valueOf).collect(Collectors.joining(","));
        }
        return String.format("%s.%s(%s)", className, method, params);
    }
}
