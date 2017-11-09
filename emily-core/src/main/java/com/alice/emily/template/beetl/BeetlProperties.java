package com.alice.emily.template.beetl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.template.AbstractTemplateViewResolverProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Created by lianhao on 2017/5/9.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "spring.beetl")
public class BeetlProperties extends AbstractTemplateViewResolverProperties {

    public static final String DEFAULT_PREFIX = "";

    public static final String DEFAULT_SUFFIX = ".btl";

    private Resource config = new ClassPathResource("beetl-eu-default.properties");
    private String root = "templates/";

    public BeetlProperties() {
        super(DEFAULT_PREFIX, DEFAULT_SUFFIX);
    }
}
