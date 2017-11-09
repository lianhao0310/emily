package com.alice.emily.validator;

import com.alice.emily.exception.UnsupportedClassTypeException;
import org.springframework.util.ObjectUtils;

import javax.annotation.Nonnull;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Created by lianhao on 2017/6/20.
 */
public abstract class AbstractValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

    protected boolean elementNullable;

    @Override
    public boolean isValid(T o, ConstraintValidatorContext context) {
        if (ObjectUtils.isEmpty(o)) {
            return true;
        }
        if (ObjectUtils.isArray(o)) {
            Object[] array = ObjectUtils.toObjectArray(o);
            for (Object item : array) {
                if (!isValid(item)) {
                    return false;
                }
            }
            return true;
        } else if (Collection.class.isAssignableFrom(o.getClass())) {
            Collection collection = (Collection) o;
            for (Object item : collection) {
                if (!isValid(item)) {
                    return false;
                }
            }
            return true;
        } else {
            return isValid(o);
        }
    }

    protected abstract ValidateFunc<?> getValidator(@Nonnull Class<?> type);

    @SuppressWarnings("unchecked")
    protected boolean isValid(Object item) {
        if (item == null) {
            if (!elementNullable) {
                return false;
            }
        } else {
            Class<?> type = item.getClass();
            ValidateFunc<Object> validator = (ValidateFunc<Object>) getValidator(type);
            if (validator == null) {
                throw new UnsupportedClassTypeException("Unsupported class type " + type.getName());
            }
            return validator.apply(item);
        }
        return true;
    }
}
