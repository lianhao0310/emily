package com.alice.emily.curator;

import com.alice.emily.test.TestPlainConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Created by lianhao on 2017/6/14.
 */
@TestPlainConfiguration
public class CuratorTestApplication {

    @Bean
    public CacheTestListener curatorNodeCacheListener() {
        return new CacheTestListener();
    }
}
