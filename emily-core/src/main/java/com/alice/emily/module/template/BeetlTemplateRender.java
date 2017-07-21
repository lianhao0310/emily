package com.alice.emily.module.template;

import com.google.common.base.Preconditions;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;

import java.util.Map;

/**
 * Created by liupin on 2017/2/5.
 */
public class BeetlTemplateRender implements TemplateRender {

    private final GroupTemplate groupTemplate;

    public BeetlTemplateRender(GroupTemplate groupTemplate) {
        this.groupTemplate = groupTemplate;
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

    @Override
    public String render(String template) {
        return render(template, null);
    }
}
