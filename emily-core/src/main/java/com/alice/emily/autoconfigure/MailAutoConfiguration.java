package com.alice.emily.autoconfigure;

import com.alice.emily.module.mail.MailProperties;
import com.alice.emily.module.mail.MailSender;
import com.alice.emily.module.mail.MailSenderImpl;
import com.alice.emily.module.template.TemplateRender;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.MessageContext;
import java.util.Map;

/**
 * Created by liupin on 2017/2/5.
 */
@Configuration
@ConditionalOnClass({ MailSender.class, MessageContext.class })
@ConditionalOnProperty(prefix = "emily.mail", name = "enabled")
@AutoConfigureAfter(BeetlAutoConfiguration.class)
@EnableConfigurationProperties(MailProperties.class)
public class MailAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(TemplateRender.class)
    public TemplateRender getTemplateRender() {
        return new TemplateRender() {
            @Override
            public String render(String template, Map<String, Object> model) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    String v = ToStringBuilder.reflectionToString(entry.getValue());
                    sb.append(entry.getKey()).append(": ").append(v).append("\n");
                }
                sb.setLength(sb.length() - 1);
                return sb.toString();
            }

            @Override
            public String render(String template) {
                return "";
            }
        };
    }

    @Bean
    public MailSender getMailSender(MailProperties configuration, TemplateRender templateRender) {
        return new MailSenderImpl(configuration, templateRender);
    }
}
