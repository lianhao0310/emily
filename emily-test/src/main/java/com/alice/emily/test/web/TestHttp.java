package com.alice.emily.test.web;

import com.alice.emily.core.SpringContext;
import com.alice.emily.utils.HTTP;
import com.alice.emily.utils.HTTP.HttpRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by lianhao on 2017/6/12.
 */
public class TestHttp {

    public static String ensureUrl(CharSequence url) {
        String validUrl = SpringContext.baseUrl();
        if (StringUtils.isNotBlank(url) && !StringUtils.startsWithAny(url, "http:", "https:")) {
            validUrl = validUrl + url;
        }
        return validUrl;
    }

    public static HTTP.HttpRequest get(final CharSequence url) {
        return HTTP.get(ensureUrl(url));
    }

    public static HttpRequest post(final CharSequence url) {
        return HTTP.post(ensureUrl(url));
    }

    public static HttpRequest put(final CharSequence url) {
        return HTTP.put(ensureUrl(url));
    }

    public static HttpRequest delete(final CharSequence url) {
        return HTTP.delete(ensureUrl(url));
    }

    public static HttpRequest options(final CharSequence url) {
        return HTTP.options(ensureUrl(url));
    }

    public static HttpRequest trace(final CharSequence url) {
        return HTTP.trace(ensureUrl(url));
    }

    public static HttpRequest head(final CharSequence url) {
        return HTTP.head(ensureUrl(url));
    }
}
