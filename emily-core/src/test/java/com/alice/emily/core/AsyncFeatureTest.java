package com.palmaplus.euphoria.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by liupin on 2017/2/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncFeatureTest {

    @Test
    public void testAsync() throws ExecutionException, InterruptedException {
        AsyncTestDemo bean = BeanProvider.getBean(AsyncTestDemo.class);
        bean.errorTask();
        System.out.println(bean.getThreadName().get());
    }

    public static class AsyncTestDemo {

        @Async
        public Future<String> getThreadName() {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                return new AsyncResult<>(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Async
        public void errorTask() {
            throw new RuntimeException("I am thrown from " + this);
        }
    }

}
