package com.alice.emily.swagger.plugin;

import com.alice.emily.validator.constraints.Negative;
import com.alice.emily.validator.constraints.Positive;
import org.springframework.core.annotation.Order;
import springfox.bean.validators.plugins.Validators;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spi.service.contexts.ParameterContext;

/**
 * Created by lianhao on 2017/6/23.
 */
@Order(Validators.BEAN_VALIDATOR_PLUGIN_ORDER)
public class PositiveNegativeAnnotationPlugin implements AnnotationAwarePlugin {

    @Override
    public void apply(ModelPropertyContext context) {
        getAnnotation(context, Negative.class)
                .ifPresent(negative -> {
                    boolean elementNullable = negative.elementNullable();
                    context.getBuilder().description("value must be negative."
                            + (elementNullable ? "" : "As for list or array, element cannot be null or empty!"));
                });

        getAnnotation(context, Positive.class)
                .ifPresent(positive -> {
                    boolean elementNullable = positive.elementNullable();
                    context.getBuilder().description("value must be positive."
                            + (elementNullable ? "" : "As for list or array, element cannot be null or empty!"));
                });
    }

    @Override
    public void apply(ParameterContext context) {
        getAnnotation(context, Negative.class)
                .ifPresent(negative -> {
                    boolean elementNullable = negative.elementNullable();
                    context.parameterBuilder().description("value must be negative."
                            + (elementNullable ? "" : "As for list or array, element cannot be null or empty!"));
                });

        getAnnotation(context, Positive.class)
                .ifPresent(positive -> {
                    boolean elementNullable = positive.elementNullable();
                    context.parameterBuilder().description("value must be positive."
                            + (elementNullable ? "" : "As for list or array, element cannot be null or empty!"));
                });
    }

}
