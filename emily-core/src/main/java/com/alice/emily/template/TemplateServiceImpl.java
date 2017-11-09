package com.alice.emily.template;

import com.alice.emily.exception.UnsupportedTemplateException;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by lianhao on 2017/5/9.
 */
@Log4j2
public class TemplateServiceImpl implements TemplateService {

    private final List<TemplateRender> templateRenders;

    public TemplateServiceImpl(List<TemplateRender> templateRenders) {
        this.templateRenders = templateRenders;
    }

    @Override
    public String render(String template, Map<String, Object> model) throws IOException {
        if (!CollectionUtils.isEmpty(templateRenders)) {
            for (TemplateRender templateRender : templateRenders) {
                if (templateRender.support(template)) {
                    log.debug("{} is supported by {}", template, templateRender);
                    return templateRender.render(template, model);
                }
            }
        }
        throw new UnsupportedTemplateException(template + " is not supported, Please check your config!");
    }
}
