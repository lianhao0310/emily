package com.alice.emily.core;

import com.google.common.util.concurrent.Uninterruptibles;
import com.alice.emily.annotation.AlertIf;
import com.alice.emily.annotation.Audit;
import com.alice.emily.aop.AlertEvent;
import com.alice.emily.utils.Errors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by lianhao on 2017/4/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = { "emily.aop.audit.enabled=true" })
public class AopFeatureTest {

    @Test
    public void testAlertIf() throws Exception {
        AlertIfTestDemo demo = SpringBeans.getBean(AlertIfTestDemo.class);
        assertThat(demo).isNotNull();
        try {
            demo.hello();
        } catch (Exception ignore) {
        }
        try {
            demo.exceptionMethod();
        } catch (Exception ignore) {
        }
    }

    @Test(expected = RuntimeException.class)
    public void testAudit() {
        AuditTestDemo bean = SpringBeans.getBean(AuditTestDemo.class);
        assertThat(bean).isNotNull();
        bean.longRunningTask("Go");
        bean.failedTask();
    }

    @AlertIf(value = Exception.class, type = "nagrand", desc = "Sorry, just for fun!")
    public static class AlertIfTestDemo {

        @AlertIf(value = Exception.class, type = "nagrand", desc = "Sorry, just for fun!")
        public void hello() throws Exception {
            System.out.println("AlertIfTestDemo.hello()");
            throw new Exception();
        }

        public void exceptionMethod() throws Exception {
            System.out.println("AlertIfTestDemo.exceptionMethod()");
            throw new Exception();
        }

        @EventListener(AlertEvent.class)
        public void alertListener(AlertEvent event) {
            assertThat(event.getType()).isEqualTo("nagrand");
            assertThat(event.getDesc()).isEqualTo("Sorry, just for fun!");
            System.out.println("Alert event: " + event);
        }
    }

    public static class AuditTestDemo {

        @Audit("INFO")
        public void longRunningTask(String mission) {
            System.out.println("Long Task " + mission + " begin...");
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            System.out.println("Long Task " + mission + " complete...");
        }

        @Audit
        public void failedTask() {
            System.out.println("Fail Task begin...");
            try {
                throw Errors.system("Just for test");
            } finally {
                System.out.println("Fail Task complete...");
            }
        }

    }
}
