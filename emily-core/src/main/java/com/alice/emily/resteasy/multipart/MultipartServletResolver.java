package com.alice.emily.resteasy.multipart;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lianhao on 2017/3/29.
 */
public class MultipartServletResolver extends StandardServletMultipartResolver {

    public static final String MULTI_FILE_MAP = MultipartServletResolver.class.getName() + ".MULTI_FILE_MAP";

    @Override
    public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException {
        MultipartHttpServletRequest multipartHttpServletRequest = super.resolveMultipart(request);
        multipartHttpServletRequest.setAttribute(MULTI_FILE_MAP, multipartHttpServletRequest.getMultiFileMap());
        return multipartHttpServletRequest;
    }

    @SuppressWarnings("unchecked")
    public static MultiValueMap<String, MultipartFile> getMultiFileMap(HttpServletRequest request) {
        return (MultiValueMap<String, MultipartFile>) request.getAttribute(MULTI_FILE_MAP);
    }
}
