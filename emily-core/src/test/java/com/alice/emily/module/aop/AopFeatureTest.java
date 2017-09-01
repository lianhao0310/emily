package com.palmaplus.euphoria.module.aop;

import com.google.common.util.concurrent.Uninterruptibles;
import com.palmaplus.euphoria.core.BeanProvider;
import com.palmaplus.euphoria.utils.Errors;
import com.palmaplus.euphoria.utils.Hacks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by liupin on 2017/4/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AopFeatureTest {

    static {
        Hacks.removeCryptographyRestrictions();
    }

    @Test
    public void testAlertIf() throws Exception {
        System.out.println("------------ start -----------");
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(1000);

        for (int i = 0; i < 10; i++) {
            fixedThreadPool.execute(() -> {
                for (int i1 = 0; i1 < 100; i1++) {
                    try {
                        AlertIfTestDemo demo = BeanProvider.getBean(AlertIfTestDemo.class);
                        Uninterruptibles.sleepUninterruptibly(10, TimeUnit.MILLISECONDS);
                        demo.exceptionMethod();
                    } catch (Exception ignore) {
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }
        countDownLatch.await();
        System.out.println("------------ finish -------------");
    }

    @Test(expected = RuntimeException.class)
    public void testAudit() {
        AuditTestDemo bean = BeanProvider.getBean(AuditTestDemo.class);
        bean.longRunningTask("Go");
        bean.failedTask();
    }

    @AlertIf(value = Exception.class, group = "nagrand", title = "Sorry, just for fun!")
    public static class AlertIfTestDemo {

        //    @AlertIf(value = Exception.class, group = {"pop", "nagrand"}, title = "Test, be relax!")
        public void hello() throws Exception {
            throw new Exception();
        }

        public void exceptionMethod() throws Exception {
            throw new Exception();
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
