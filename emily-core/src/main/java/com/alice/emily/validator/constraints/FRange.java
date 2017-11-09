package com.alice.emily.validator.constraints;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by lianhao on 2017/6/20.
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@FMin(Double.MIN_VALUE)
@FMax(Double.MAX_VALUE)
@Constraint(validatedBy = { })
@ReportAsSingleViolation
public @interface FRange {

    @OverridesAttribute(constraint = FMin.class, name = "value")
    double min() default Double.MIN_VALUE;

    @OverridesAttribute(constraint = FMax.class, name = "value")
    double max() default Double.MAX_VALUE;

    //默认错误消息
    String message() default "{com.alice.emily.validator.constraints.FRange.message}";

    //分组
    Class<?>[] groups() default { };

    //负载
    Class<? extends Payload>[] payload() default { };

    //指定多个时使用
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FRange[] value();
    }
}
