package com.alice.emily.module.template;

import java.util.Map;

/**
 * Created by liupin on 2017/1/9.
 */
public interface TemplateRender {

    String render(String template, Map<String, Object> model);

    String render(String template);
}
