package com.alice.emily.security;

import com.alice.emily.autoconfigure.core.SecurityExtAutoConfiguration;
import com.alice.emily.security.resource.ProtectedController;
import com.alice.emily.test.TestPlainApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Created by lianhao on 2017/4/13.
 */
@ActiveProfiles("security")
@ComponentScan(basePackageClasses = ProtectedController.class)
@ImportAutoConfiguration({ SecurityAutoConfiguration.class, SecurityExtAutoConfiguration.class })
@SpringBootTest(classes = TestPlainApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AbstractSecurityTest extends AbstractJUnit4SpringContextTests {

    static final String PROTECTED_RESOURCE_USER = "/protected/resource/user";
    static final String PROTECTED_RESOURCE_TEST = "/protected/resource/test";
    static final String PROTECTED_CONTROLLER_USER = "/protected/controller/user";
    static final String PROTECTED_CONTROLLER_TEST = "/protected/controller/test";
    static final String RESOURCE_ADMIN = "/protected/resource/constraint/admin";

    @Autowired
    protected TestRestTemplate template;

    @Autowired
    protected WebTestClient client;
}
