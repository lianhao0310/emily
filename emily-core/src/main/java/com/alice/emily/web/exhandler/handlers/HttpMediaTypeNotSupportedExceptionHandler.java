package com.alice.emily.web.exhandler.handlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class HttpMediaTypeNotSupportedExceptionHandler
        extends ErrorMessageRestExceptionHandler<HttpMediaTypeNotSupportedException> {

    public HttpMediaTypeNotSupportedExceptionHandler() {
        super(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected HttpHeaders createHeaders(@Nonnull HttpMediaTypeNotSupportedException ex,
                                        @Nonnull HttpServletRequest req) {

        HttpHeaders headers = super.createHeaders(ex, req);
        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();

        if (!CollectionUtils.isEmpty(mediaTypes)) {
            headers.setAccept(mediaTypes);
        }
        return headers;
    }
}
