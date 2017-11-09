package com.alice.emily.template.freemarker;

import com.alice.emily.exception.TemplateException;
import com.alice.emily.template.TemplateRender;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.core.annotation.Order;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by lianhao on 2017/5/9.
 */
@Order(300)
public class FreemarkerTemplateRender implements TemplateRender {

    @Autowired
    private FreeMarkerProperties freeMarkerProperties;

    @Autowired
    private Configuration freemarkerConfiguration;

    @Override
    public boolean support(String template) {
        return !StringUtils.isEmpty(template) && template.endsWith(freeMarkerProperties.getSuffix());
    }

    @Override
    public String render(String template, Map<String, Object> model) {
        try {
            Template t = freemarkerConfiguration.getTemplate(template, Charset.forName("UTF-8").name());
            return FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        } catch (Exception e) {
            throw new TemplateException("Cannot render freemarker template " + template, e);
        }
    }
}
