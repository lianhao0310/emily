package com.alice.emily.validator;

import com.alice.emily.validator.constraints.FMax;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by lianhao on 2017/6/20.
 */
public abstract class FMaxValidator<T> implements ConstraintValidator<FMax, T> {

    protected double max;

    @Override
    public void initialize(FMax constraintAnnotation) {
        max = constraintAnnotation.value();
    }

    public static class DoubleV extends FMaxValidator<Double> {

        @Override
        public boolean isValid(Double value, ConstraintValidatorContext context) {
            return value == null || value <= max;
        }
    }

    public static class FloatV extends FMaxValidator<Float> {

        @Override
        public boolean isValid(Float value, ConstraintValidatorContext context) {
            return value == null || value.doubleValue() <= max;
        }
    }
}
