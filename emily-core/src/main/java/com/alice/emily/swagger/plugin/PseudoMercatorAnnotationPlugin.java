package com.alice.emily.swagger.plugin;

import com.alice.emily.validator.constraints.PseudoMercatorX;
import com.alice.emily.validator.constraints.PseudoMercatorY;
import org.springframework.core.annotation.Order;
import springfox.bean.validators.plugins.Validators;
import springfox.documentation.service.AllowableRangeValues;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spi.service.contexts.ParameterContext;

/**
 * Created by lianhao on 2017/6/23.
 */
@Order(Validators.BEAN_VALIDATOR_PLUGIN_ORDER)
public class PseudoMercatorAnnotationPlugin implements AnnotationAwarePlugin {

    @Override
    public void apply(ParameterContext context) {
        getAnnotation(context, PseudoMercatorX.class)
                .ifPresent(pseudoMercatorX -> {
                    AllowableRangeValues rangeValues =
                            new AllowableRangeValues("-20026376.39", false, "20026376.39", false);
                    context.parameterBuilder()
                            .allowableValues(rangeValues)
                            .description("Must be valid WGS84 Web Mercator (Auxiliary Sphere) x coordinate");
                });

        getAnnotation(context, PseudoMercatorY.class).ifPresent(pseudoMercatorY -> {
            AllowableRangeValues rangeValues =
                    new AllowableRangeValues("-20048966.10", false, "20048966.10", false);
            context.parameterBuilder()
                    .allowableValues(rangeValues)
                    .description("Must be valid WGS84 Web Mercator (Auxiliary Sphere) y coordinate");
        });
    }

    @Override
    public void apply(ModelPropertyContext context) {
        getAnnotation(context, PseudoMercatorX.class)
                .ifPresent(pseudoMercatorX -> {
                    AllowableRangeValues rangeValues =
                            new AllowableRangeValues("-20026376.39", false, "20026376.39", false);
                    context.getBuilder()
                            .allowableValues(rangeValues)
                            .description("Must be valid WGS84 Web Mercator (Auxiliary Sphere) x coordinate");
                });
        getAnnotation(context, PseudoMercatorY.class)
                .ifPresent(pseudoMercatorY -> {
                    AllowableRangeValues rangeValues =
                            new AllowableRangeValues("-20048966.10", false, "20048966.10", false);
                    context.getBuilder()
                            .allowableValues(rangeValues)
                            .description("Must be valid WGS84 Web Mercator (Auxiliary Sphere) y coordinate");
                });
    }
}
