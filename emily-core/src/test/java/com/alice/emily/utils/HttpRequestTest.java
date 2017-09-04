package com.alice.emily.utils;

import org.jboss.resteasy.util.HttpResponseCodes;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by Lianhao on 2017/9/4.
 */
public class HttpRequestTest {

    @Test
    public void testGet(){
        HTTP.HttpRequest httpRequest = HTTP.get("http://127.0.0.1:8080/user/info", true, "phone", "17092613935");

        assertThat(httpRequest.code()).isEqualTo(HttpResponseCodes.SC_OK);
        System.out.println(httpRequest.body());
    }
}