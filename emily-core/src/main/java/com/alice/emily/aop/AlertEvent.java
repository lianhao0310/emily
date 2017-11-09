package com.alice.emily.aop;

import lombok.Builder;
import lombok.Value;

import java.lang.reflect.Method;

/**
 * Created by lianhao on 2017/5/2.
 */
@Value
@Builder
public class AlertEvent {

    private String type;

    private String desc;

    private Method method;

    private Object[] args;

    private Throwable throwable;
}