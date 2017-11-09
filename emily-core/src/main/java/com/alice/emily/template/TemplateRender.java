package com.alice.emily.template;

import java.util.Map;

/**
 * Created by lianhao on 2017/1/9.
 */
public interface TemplateRender {

    boolean support(String template);

    String render(String template, Map<String, Object> model);
}
