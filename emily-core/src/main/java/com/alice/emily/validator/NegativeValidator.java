package com.alice.emily.validator;

import com.google.common.collect.Maps;
import com.alice.emily.validator.constraints.Negative;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * Created by lianhao on 2017/6/20.
 */
public class NegativeValidator extends AbstractValidator<Negative, Object> {

    private static Map<Class<?>, ValidateFunc<?>> validators = Maps.newHashMap();

    static {
        validators.put(BigDecimal.class, ValidateFunc.lessThan(BigDecimal.ZERO));
        validators.put(BigInteger.class, ValidateFunc.lessThan(BigInteger.ZERO));
        validators.put(Byte.class, ValidateFunc.lessThan((byte) 0));
        validators.put(Short.class, ValidateFunc.lessThan((short) 0));
        validators.put(Integer.class, ValidateFunc.lessThan(0));
        validators.put(Long.class, ValidateFunc.lessThan(0L));
        validators.put(Double.class, ValidateFunc.lessThan(0.0));
        validators.put(Float.class, ValidateFunc.lessThan(0.0f));
    }

    @Override
    protected ValidateFunc<?> getValidator(@Nonnull Class<?> type) {
        return validators.get(type);
    }

    @Override
    public void initialize(Negative constraintAnnotation) {
        elementNullable = constraintAnnotation.elementNullable();
    }
}
