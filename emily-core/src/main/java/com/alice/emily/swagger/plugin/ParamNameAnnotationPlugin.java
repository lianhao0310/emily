package com.alice.emily.swagger.plugin;

import com.alice.emily.web.param.ParamName;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spi.service.contexts.ParameterContext;

/**
 * Created by lianhao on 2017/6/23.
 */
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class ParamNameAnnotationPlugin implements AnnotationAwarePlugin {

    @Override
    public void apply(ModelPropertyContext context) {
        getAnnotation(context, ParamName.class)
                .ifPresent(paramName -> context.getBuilder().name(paramName.value()));
    }

    @Override
    public void apply(ParameterContext context) {
        getAnnotation(context, ParamName.class)
                .ifPresent(paramName -> context.parameterBuilder().name(paramName.value()));
    }
}
