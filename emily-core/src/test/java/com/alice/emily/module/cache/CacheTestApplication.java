package com.alice.emily.module.cache;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by lianhao on 2017/4/8.
 */
@SpringBootApplication
public class CacheTestApplication {
    @Bean
    public JCacheTestDemo getJCacheTestDemo() {
        return new JCacheTestDemo();
    }
}
