package com.alice.emily.validator;

import com.google.common.collect.Maps;
import com.alice.emily.validator.constraints.Positive;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * Created by lianhao on 2017/6/20.
 */
public class PositiveValidator extends AbstractValidator<Positive, Object> {

    private static Map<Class<?>, ValidateFunc<?>> validators = Maps.newHashMap();

    static {
        validators.put(BigDecimal.class, ValidateFunc.greeterThan(BigDecimal.ZERO));
        validators.put(BigInteger.class, ValidateFunc.greeterThan(BigInteger.ZERO));
        validators.put(Byte.class, ValidateFunc.greeterThan((byte) 0));
        validators.put(Short.class, ValidateFunc.greeterThan((short) 0));
        validators.put(Integer.class, ValidateFunc.greeterThan(0));
        validators.put(Long.class, ValidateFunc.greeterThan(0L));
        validators.put(Double.class, ValidateFunc.greeterThan(0.0));
        validators.put(Float.class, ValidateFunc.greeterThan(0.0f));
    }

    @Override
    protected ValidateFunc<?> getValidator(@Nonnull Class<?> type) {
        return validators.get(type);
    }

    @Override
    public void initialize(Positive constraintAnnotation) {
        elementNullable = constraintAnnotation.elementNullable();
    }
}
