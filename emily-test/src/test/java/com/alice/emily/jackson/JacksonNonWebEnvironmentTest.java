package com.alice.emily.jackson;

import com.alice.emily.test.TestPlainApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by lianhao on 2017/4/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestPlainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class JacksonNonWebEnvironmentTest extends JacksonWebEnvironmentTest {

}
