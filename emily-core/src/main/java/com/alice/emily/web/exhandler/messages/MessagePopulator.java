package com.alice.emily.web.exhandler.messages;

import org.springframework.http.HttpStatus;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by lianhao on 2017/6/29.
 */
public interface MessagePopulator {

    <E extends Exception> ErrorMessage createAndPopulate(@Nonnull E ex,
                                                         @Nonnull HttpServletRequest req,
                                                         @Nonnull HttpStatus defaultStatus);
    /**
     * Interpolates the message template using the given variables.
     *
     * @param messageTemplate The message to interpolate.
     * @param variables Map of variables that will be accessible for the template.
     * @return An interpolated message.
     */
    String interpolate(String messageTemplate, Map<String, Object> variables);

    String getMessage(String name);
}
