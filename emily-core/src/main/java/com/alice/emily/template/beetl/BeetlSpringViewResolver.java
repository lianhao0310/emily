package com.alice.emily.template.beetl;

/**
 * Created by lianhao on 2017/5/10.
 */
public class BeetlSpringViewResolver extends org.beetl.ext.spring.BeetlSpringViewResolver {

    @Override
    public void setPrefix(String prefix) {
        // prevent warning logging
    }
}
