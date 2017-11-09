package com.alice.emily.template;

import com.alice.emily.test.TestPlainApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by lianhao on 2017/5/17.
 */
@SpringBootTest(classes = TestPlainApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TemplateServiceWebTest extends TemplateServiceNoWebTest {
}
