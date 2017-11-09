package com.alice.emily.validator;

import com.alice.emily.validator.constraints.FMin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by lianhao on 2017/6/20.
 */
public abstract class FMinValidator<T> implements ConstraintValidator<FMin, T> {

    protected double min;

    @Override
    public void initialize(FMin constraintAnnotation) {
        min = constraintAnnotation.value();
    }

    public static class DoubleV extends FMinValidator<Double> {

        @Override
        public boolean isValid(Double value, ConstraintValidatorContext context) {
            return value == null || value >= min;
        }

    }

    public static class FloatV extends FMinValidator<Float> {

        @Override
        public boolean isValid(Float value, ConstraintValidatorContext context) {
            return value == null || value.doubleValue() >= min;
        }
    }
}
