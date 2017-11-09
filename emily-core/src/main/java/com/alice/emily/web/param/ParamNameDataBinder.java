package com.alice.emily.web.param;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.annotation.Nonnull;
import javax.servlet.ServletRequest;
import java.util.Map;

/**
 * ServletRequestDataBinder which supports fields renaming using {@link ParamName}
 */
public class ParamNameDataBinder extends ExtendedServletRequestDataBinder {

    private final Map<String, String> renameMapping;

    public ParamNameDataBinder(Object target,
                               String objectName,
                               Map<String, String> renameMapping) {
        super(target, objectName);
        this.renameMapping = renameMapping;
    }

    @Override
    protected void addBindValues(@Nonnull MutablePropertyValues mpvs, ServletRequest request) {
        super.addBindValues(mpvs, request);
        for (Map.Entry<String, String> entry : renameMapping.entrySet()) {
            String from = entry.getKey();
            String to = entry.getValue();
            if (mpvs.contains(from)) {
                PropertyValue propertyValue = mpvs.getPropertyValue(from);
                if (propertyValue != null) {
                    mpvs.add(to, propertyValue.getValue());
                }
            }
        }
    }
}