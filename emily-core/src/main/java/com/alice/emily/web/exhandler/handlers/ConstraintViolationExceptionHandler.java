package com.alice.emily.web.exhandler.handlers;

import com.alice.emily.core.SpringContext;
import com.alice.emily.web.exhandler.messages.ErrorMessage;
import com.alice.emily.web.exhandler.messages.ValidationErrorMessage;
import org.springframework.core.convert.ConversionException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Path.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConstraintViolationExceptionHandler
        extends ErrorMessageRestExceptionHandler<ConstraintViolationException> {

    public ConstraintViolationExceptionHandler() {
        super(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ValidationErrorMessage createBody(@Nonnull ConstraintViolationException ex,
                                             @Nonnull HttpServletRequest req) {

        ErrorMessage tmpl = super.createBody(ex, req);
        ValidationErrorMessage msg = new ValidationErrorMessage(tmpl);

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            Node pathNode = findLastNonEmptyPathNode(violation.getPropertyPath());
            String rejectedValue = convertToString(violation.getInvalidValue());
            if (pathNode != null) {
                msg.addError(pathNode.getName(), rejectedValue, violation.getMessage());
            } else {
                msg.addError(null, rejectedValue, violation.getMessage());
            }
        }
        return msg;
    }


    private Node findLastNonEmptyPathNode(Path path) {

        List<Node> list = new ArrayList<>();
        for (Node aPath : path) {
            list.add(aPath);
        }
        Collections.reverse(list);
        for (Node node : list) {
            if (!StringUtils.isEmpty(node.getName())) {
                return node;
            }
        }
        return null;
    }

    private String convertToString(Object invalidValue) {

        if (invalidValue == null) {
            return null;
        }

        try {
            return SpringContext.conversionService().convert(invalidValue, String.class);

        } catch (ConversionException ex) {
            return invalidValue.toString();
        }
    }

}
