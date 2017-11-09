package com.alice.emily.resteasy.pageable;

import com.google.common.base.Preconditions;
import com.alice.emily.resteasy.AbstractMessageBodyReader;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static org.jboss.resteasy.util.FindAnnotation.findAnnotation;

/**
 * Created by lianhao on 2017/5/10.
 */
@Setter
@Provider
@Consumes(MediaType.WILDCARD)
public class PageableReader extends AbstractMessageBodyReader<Pageable> {

    private static final String INVALID_DEFAULT_PAGE_SIZE = "Invalid default page size configured for resource %s! Must not be less than one!";

    private static final String DEFAULT_PAGE_PARAMETER = "page";
    private static final String DEFAULT_SIZE_PARAMETER = "size";
    private static final String DEFAULT_PREFIX = "";
    private static final String DEFAULT_QUALIFIER_DELIMITER = "_";
    private static final int DEFAULT_MAX_PAGE_SIZE = 2000;
    private static final Pageable DEFAULT_PAGE_REQUEST = PageRequest.of(0, 20);

    private Pageable fallbackPageable = DEFAULT_PAGE_REQUEST;
    private String pageParameterName = DEFAULT_PAGE_PARAMETER;
    private String sizeParameterName = DEFAULT_SIZE_PARAMETER;
    private String prefix = DEFAULT_PREFIX;
    private String qualifierDelimiter = DEFAULT_QUALIFIER_DELIMITER;
    private int maxPageSize = DEFAULT_MAX_PAGE_SIZE;
    private boolean oneIndexedParameters = false;

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Pageable.class.equals(type);
    }

    @Override
    public Pageable readFrom(Class<Pageable> type, Type genericType, Annotation[] annotations,
                             MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                             InputStream entityStream) throws IOException, WebApplicationException {
        Pageable defaultOrFallback = getDefaultFromAnnotationOrFallback(annotations);

        String pageString = request.getParameter(getParameterNameToUse(pageParameterName, annotations));
        String pageSizeString = request.getParameter(getParameterNameToUse(sizeParameterName, annotations));

        boolean pageAndSizeGiven = StringUtils.hasText(pageString) && StringUtils.hasText(pageSizeString);

        if (!pageAndSizeGiven && defaultOrFallback == null) {
            return null;
        }

        int page = StringUtils.hasText(pageString) ? parseAndApplyBoundaries(pageString, Integer.MAX_VALUE, true)
                : defaultOrFallback.getPageNumber();
        int pageSize = StringUtils.hasText(pageSizeString) ? parseAndApplyBoundaries(pageSizeString, maxPageSize, false)
                : defaultOrFallback.getPageSize();

        // Limit lower bound
        pageSize = pageSize < 1 ? defaultOrFallback.getPageSize() : pageSize;
        // Limit upper bound
        pageSize = pageSize > maxPageSize ? maxPageSize : pageSize;

        MessageBodyReader<Sort> reader = workers.getMessageBodyReader(Sort.class, Sort.class, annotations, mediaType);
        Sort sort = reader == null ? null
                : reader.readFrom(Sort.class, Sort.class, annotations, mediaType, httpHeaders, entityStream);

        // Default if necessary and default configured
        sort = sort == null && defaultOrFallback != null ? defaultOrFallback.getSort() : sort;

        return PageRequest.of(page, pageSize, sort);
    }

    private Pageable getDefaultFromAnnotationOrFallback(Annotation[] annotations) {
        PageableDefault defaults = findAnnotation(annotations, PageableDefault.class);
        if (defaults != null) {
            Integer defaultPageNumber = defaults.page();
            Integer defaultPageSize = getSpecificPropertyOrDefaultFromValue(defaults, "size");

            Preconditions.checkState(defaultPageSize > 1, INVALID_DEFAULT_PAGE_SIZE, request.getRequestURI());
            if (defaults.sort().length == 0) {
                return PageRequest.of(defaultPageNumber, defaultPageSize);
            }

            return PageRequest.of(defaultPageNumber, defaultPageSize, defaults.direction(), defaults.sort());
        }
        return fallbackPageable;
    }

    private String getParameterNameToUse(String source, Annotation[] annotations) {
        StringBuilder builder = new StringBuilder(prefix);
        Qualifier qualifier = findAnnotation(annotations, Qualifier.class);
        if (qualifier != null) {
            builder.append(qualifier.value());
            builder.append(qualifierDelimiter);
        }
        return builder.append(source).toString();
    }

    /**
     * Tries to parse the given {@link String} into an integer and applies the given boundaries. Will return 0 if the
     * {@link String} cannot be parsed.
     *
     * @param parameter  the parameter value.
     * @param upper      the upper bound to be applied.
     * @param shiftIndex whether to shift the index if {@link #oneIndexedParameters} is set to true.
     * @return int
     */
    private int parseAndApplyBoundaries(String parameter, int upper, boolean shiftIndex) {

        try {
            int parsed = Integer.parseInt(parameter) - (oneIndexedParameters && shiftIndex ? 1 : 0);
            return parsed < 0 ? 0 : parsed > upper ? upper : parsed;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
