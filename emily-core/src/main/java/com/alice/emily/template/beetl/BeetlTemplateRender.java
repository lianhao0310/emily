package com.alice.emily.template.beetl;

import com.google.common.base.Preconditions;
import com.alice.emily.template.TemplateRender;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by lianhao on 2017/2/5.
 */
@Order(100)
public class BeetlTemplateRender implements TemplateRender {

    private final GroupTemplate groupTemplate;
    private final BeetlProperties beetlProperties;

    public BeetlTemplateRender(GroupTemplate groupTemplate, BeetlProperties beetlProperties) {
        this.groupTemplate = groupTemplate;
        this.beetlProperties = beetlProperties;
    }

    @Override
    public boolean support(String template) {
        return !StringUtils.isEmpty(template) && template.endsWith(beetlProperties.getSuffix());
    }

    @Override
    public String render(String template, Map<String, Object> model) {
        Template t = groupTemplate.getTemplate(template);
        Preconditions.checkNotNull(t, "Cannot found template %s in classpath:template");
        if (model != null) {
            t.binding(model);
        }
        return t.render();
    }
}
