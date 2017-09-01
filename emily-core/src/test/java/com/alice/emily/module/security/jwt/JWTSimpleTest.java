package com.alice.emily.module.security.jwt;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * Created by lianhao on 2017/3/15.
 */
public class JWTSimpleTest extends AbstractJsonWebTokenTest {

    @Test
    public void testAccessReject() {
        ResponseEntity<Map> entity;

        entity = template.getForEntity("/protected-controller", Map.class);
        Assert.assertEquals(401, entity.getStatusCodeValue());
        entity = template.getForEntity("/protected-resource", Map.class);
        Assert.assertEquals(401, entity.getStatusCodeValue());

        String token = getToken("admin", "password");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        entity = template.exchange("/protected-controller", HttpMethod.GET, httpEntity, Map.class);
        Assert.assertEquals(403, entity.getStatusCodeValue());

        entity = template.exchange("/protected-resource", HttpMethod.GET, httpEntity, Map.class);
        Assert.assertEquals(403, entity.getStatusCodeValue());
    }

    @Test
    public void testAccessProtectedController() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getToken("emily", "password"));
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> entity = template.exchange("/protected-controller", HttpMethod.GET, httpEntity, String.class);
        Assert.assertEquals(200, entity.getStatusCodeValue());
        System.out.println(entity.getBody());
    }

    @Test
    public void testAccessProtectedResource() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getToken("emily", "password"));
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> entity = template.exchange("/protected-resource", HttpMethod.GET, httpEntity, String.class);
        Assert.assertEquals(200, entity.getStatusCodeValue());
        System.out.println(entity.getBody());
    }

    @Test
    public void testAccessInsecureResource() {
        ResponseEntity<String> entity = template.getForEntity("/insecure", String.class);
        Assert.assertTrue(entity.getStatusCode().is2xxSuccessful());
        System.out.println(entity.getBody());
    }
}
