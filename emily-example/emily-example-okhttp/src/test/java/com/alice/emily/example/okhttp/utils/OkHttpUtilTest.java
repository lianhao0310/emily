package com.alice.emily.example.okhttp.utils;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lianhao on 2017/7/25.
 */
public class OkHttpUtilTest {
    @Test
    public void get() throws Exception {
        try {
            String getResponse = OkHttpUtil.get("http://localhost:8080/default/123");
            System.out.println(getResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void post() throws Exception {
        try {
            String json = "{'name':'mike'}";
            String postResponse = OkHttpUtil.post("http://localhost:8080/default/post", json);
            System.out.println(postResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void postMap() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "mike");
        map.put("age", "20");
        try {
            String postResponse = OkHttpUtil.post("http://localhost:8080/default/post", map);
            System.out.println(postResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}