package com.alice.emily.data;

import com.alice.emily.test.TestPlainApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by lianhao on 2017/5/3.
 */
@ActiveProfiles("data")
@SpringBootTest(classes = TestPlainApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigurationPackage
public abstract class AbstractDataTest extends AbstractJUnit4SpringContextTests {
}
