package com.alice.emily.resteasy.pageable;

import com.google.common.base.Preconditions;
import com.alice.emily.resteasy.AbstractMessageBodyReader;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.util.StringUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.jboss.resteasy.util.FindAnnotation.findAnnotation;

/**
 * Created by lianhao on 2017/5/10.
 */
@Setter
@Provider
@Consumes(MediaType.WILDCARD)
public class SortReader extends AbstractMessageBodyReader<Sort> {

    private static final String DEFAULT_PARAMETER = "sort";
    private static final String DEFAULT_PROPERTY_DELIMITER = ",";
    private static final String DEFAULT_QUALIFIER_DELIMITER = "_";
    private static final Sort DEFAULT_SORT = null;

    private static final String SORT_DEFAULTS_NAME = SortDefaults.class.getSimpleName();
    private static final String SORT_DEFAULT_NAME = SortDefault.class.getSimpleName();

    private Sort fallbackSort = DEFAULT_SORT;
    private String sortParameter = DEFAULT_PARAMETER;
    private String propertyDelimiter = DEFAULT_PROPERTY_DELIMITER;
    private String qualifierDelimiter = DEFAULT_QUALIFIER_DELIMITER;

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Sort.class.equals(type);
    }

    @Override
    public Sort readFrom(Class<Sort> type, Type genericType, Annotation[] annotations,
                         MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                         InputStream entityStream) throws IOException, WebApplicationException {
        String[] directionParameter = request.getParameterValues(getSortParameter(annotations));

        // No parameter
        if (directionParameter == null) {
            return getDefaultFromAnnotationOrFallback(annotations);
        }

        // Single empty parameter, e.g "sort="
        if (directionParameter.length == 1 && !StringUtils.hasText(directionParameter[0])) {
            return getDefaultFromAnnotationOrFallback(annotations);
        }

        return parseParameterIntoSort(directionParameter, propertyDelimiter);
    }

    private Sort getDefaultFromAnnotationOrFallback(Annotation[] annotations) {
        SortDefaults annotatedDefaults = findAnnotation(annotations, SortDefaults.class);
        SortDefault annotatedDefault = findAnnotation(annotations, SortDefault.class);

        Preconditions.checkArgument(annotatedDefaults == null || annotatedDefault == null,
                "Cannot use both @%s and @%s on request %s! Move %s into %s to define sorting order!",
                SORT_DEFAULTS_NAME, SORT_DEFAULT_NAME, request.getRequestURI(), SORT_DEFAULT_NAME, SORT_DEFAULTS_NAME);

        if (annotatedDefault != null) {
            return appendOrCreateSortTo(annotatedDefault, null);
        }

        if (annotatedDefaults != null) {
            Sort sort = null;
            for (SortDefault currentAnnotatedDefault : annotatedDefaults.value()) {
                sort = appendOrCreateSortTo(currentAnnotatedDefault, sort);
            }
            return sort;
        }

        return fallbackSort;
    }

    /**
     * Creates a new {@link Sort} instance from the given {@link SortDefault} or appends it to the given {@link Sort}
     * instance if it's not {@literal null}.
     *
     * @param sortDefault
     * @param sortOrNull
     * @return
     */
    private Sort appendOrCreateSortTo(SortDefault sortDefault, Sort sortOrNull) {
        String[] fields = getSpecificPropertyOrDefaultFromValue(sortDefault, "sort");

        if (fields.length == 0) {
            return null;
        }

        Sort sort = new Sort(sortDefault.direction(), fields);
        return sortOrNull == null ? sort : sortOrNull.and(sort);
    }

    private String getSortParameter(Annotation[] annotations) {
        StringBuilder builder = new StringBuilder();

        Qualifier qualifier = findAnnotation(annotations, Qualifier.class);
        if (qualifier != null) {
            builder.append(qualifier.value()).append(qualifierDelimiter);
        }

        return builder.append(sortParameter).toString();
    }

    /**
     * Parses the given sort expressions into a {@link Sort} instance. The implementation expects the sources to be a
     * concatenation of Strings using the given delimiter. If the last element can be parsed into a {@link Sort.Direction} it's
     * considered a {@link Sort.Direction} and a simple property otherwise.
     *
     * @param source    will never be {@literal null}.
     * @param delimiter the delimiter to be used to split up the source elements, will never be {@literal null}.
     * @return Sort
     */
    private Sort parseParameterIntoSort(String[] source, String delimiter) {

        List<Sort.Order> allOrders = new ArrayList<>();

        for (String part : source) {

            if (part == null) {
                continue;
            }

            String[] elements = part.split(delimiter);

            Optional<Sort.Direction> direction = elements.length == 0 ? Optional.empty()
                    : Sort.Direction.fromOptionalString(elements[elements.length - 1]);

            int lastIndex = direction.map(it -> elements.length - 1).orElseGet(() -> elements.length);

            for (int i = 0; i < lastIndex; i++) {
                toOrder(elements[i], direction).ifPresent(allOrders::add);
            }
        }

        return allOrders.isEmpty() ? Sort.unsorted() : Sort.by(allOrders);
    }

    private static Optional<Sort.Order> toOrder(String property, Optional<Sort.Direction> direction) {

        if (!StringUtils.hasText(property)) {
            return Optional.empty();
        }

        return Optional.of(direction.map(it -> new Sort.Order(it, property)).orElseGet(() -> Sort.Order.by(property)));
    }
}
