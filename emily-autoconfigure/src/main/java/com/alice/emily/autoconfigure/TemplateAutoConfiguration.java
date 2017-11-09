package com.alice.emily.autoconfigure;

import com.alice.emily.template.TemplateConfiguration;
import com.alice.emily.template.beetl.BeetlConfiguration;
import com.alice.emily.template.freemarker.FreemarkerConfiguration;
import com.alice.emily.template.thymeleaf.ThymeleafConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by lianhao on 2017/5/3.
 */
@Configuration
@Import({ TemplateConfiguration.class,
        BeetlConfiguration.class,
        ThymeleafConfiguration.class,
        FreemarkerConfiguration.class })
@AutoConfigureAfter({ ThymeleafAutoConfiguration.class, FreeMarkerAutoConfiguration.class })
public class TemplateAutoConfiguration {
}
