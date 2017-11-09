package com.alice.emily.template;

import com.google.common.collect.Maps;
import com.alice.emily.test.TestPlainApplication;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.IOException;
import java.util.Map;

/**
 * Created by lianhao on 2017/5/9.
 */
@SpringBootTest(classes = TestPlainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TemplateServiceNoWebTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private TemplateService templateService;

    @Test
    public void testBeetl() throws IOException {
        System.out.println("********************** Beetl *******************");
        Map<String, Object> model = Maps.newHashMap();
        model.put("title", "Beetl");
        model.put("greeting", "Hi Beetl");
        String result = templateService.render("greeting.btl", model);
        System.out.println(result);
    }

    @Test
    public void testFreemarker() throws IOException {
        System.out.println("******************** Freemarker *****************");
        Map<String, Object> latestProduct = Maps.newHashMap();
        latestProduct.put("name", "emily");
        latestProduct.put("url", "https://www.alice.com");

        Map<String, Object> model = Maps.newHashMap();
        model.put("user", "Freemarker");
        model.put("latestProduct", latestProduct);

        String result = templateService.render("welcome.ftl", model);
        System.out.println(result);
    }

    @Test
    public void testThymeleaf() throws IOException {
        System.out.println("******************** Thymeleaf *****************");
        Map<String, Object> model = Maps.newHashMap();
        model.put("welcome", "I am using thymeleaf");
        String result = templateService.render("home.html", model);
        System.out.println(result);
    }
}
