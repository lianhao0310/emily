package com.alice.emily.template.beetl;

import org.beetl.core.GroupTemplate;

import javax.servlet.ServletContext;

/**
 * Created by lianhao on 2017/5/3.
 */
public class BeetlGroupUtilConfiguration extends org.beetl.ext.spring.BeetlGroupUtilConfiguration {

    public BeetlGroupUtilConfiguration(GroupTemplate groupTemplate) {
        this.groupTemplate = groupTemplate;
    }

    @Override
    public void init() {
        try {
            config(groupTemplate);
            initOther();
        } catch (Exception e) {
            throw new RuntimeException("加载GroupTemplate失败", e);
        }
    }

    @Override
    public void setServletContext(ServletContext sc) {
        // skip process for root process preventing exception when MockMvc enabled
    }
}
