package com.alice.emily.template.thymeleaf;

import com.alice.emily.template.TemplateRender;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Collections;
import java.util.Map;

/**
 * Created by lianhao on 2017/5/9.
 */
@Order(200)
public class ThymeleafTemplateRender implements TemplateRender {

    @Autowired
    private SpringTemplateEngine thymeleafEngine;

    @Value("${spring.thymeleaf.suffix:.html}")
    private String thymeleafSuffix;

    @Override
    public boolean support(String template) {
        return !StringUtils.isEmpty(template) && template.endsWith(thymeleafSuffix);
    }

    @Override
    public String render(String template, Map<String, Object> model) {
        Context context = new Context();
        context.setVariables(model == null ? Collections.emptyMap() : model);
        String templateName = FilenameUtils.removeExtension(template);
        return thymeleafEngine.process(templateName, context);
    }
}
