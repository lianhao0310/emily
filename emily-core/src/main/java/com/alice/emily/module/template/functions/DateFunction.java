package com.alice.emily.module.template.functions;

import org.beetl.core.Context;

import java.util.Date;

/**
 * Created by lianhao on 2017/1/17.
 */
public class DateFunction extends org.beetl.ext.fn.DateFunction {

    @Override
    public Date call(Object[] paras, Context ctx) {
        if (paras.length == 1) {
            Class<?> type = paras[0].getClass();
            if (long.class.isAssignableFrom(type)) {
                return new Date((long) paras[0]);
            }
            if (Long.class.isAssignableFrom(type)) {
                return new Date((Long) paras[0]);
            }
        }
        return super.call(paras, ctx);
    }
}
