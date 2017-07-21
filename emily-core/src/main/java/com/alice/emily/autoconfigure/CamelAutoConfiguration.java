package com.alice.emily.autoconfigure;

import com.alice.emily.module.camel.EmbeddedZookeeperProperties;
import com.alice.emily.module.camel.EmbeddedZookeeperServer;
import com.alice.emily.utils.Threads;
import org.apache.camel.CamelContext;
import org.apache.camel.ThreadPoolRejectedPolicy;
import org.apache.camel.spi.ThreadPoolProfile;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by liupin on 2017/3/1.
 */
@Configuration
@ConditionalOnClass(CamelContext.class)
public class CamelAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ThreadPoolProfile threadPoolProfile() {
        ThreadPoolProfile profile = new ThreadPoolProfile("emily");
        profile.setAllowCoreThreadTimeOut(false);
        profile.setKeepAliveTime(30L);
        profile.setTimeUnit(TimeUnit.SECONDS);
        profile.setRejectedPolicy(ThreadPoolRejectedPolicy.DiscardOldest);
        profile.setMaxQueueSize(1024 * 4);
        profile.setPoolSize(Threads.processors());
        profile.setMaxPoolSize(Threads.processors() * 2);
        return profile;
    }

    @Configuration
    @AutoConfigureBefore(org.apache.camel.spring.boot.CamelAutoConfiguration.class)
    @EnableConfigurationProperties(EmbeddedZookeeperProperties.class)
    static class CamelZookeeperConfiguration {

        @Bean(initMethod = "startup")
        @ConditionalOnProperty(prefix = "emily.zookeeper.embedded", name = "enabled", havingValue = "true")
        public EmbeddedZookeeperServer embeddedZookeeperServer(EmbeddedZookeeperProperties zookeeperProperties) {
            return new EmbeddedZookeeperServer(zookeeperProperties.getPort());
        }
    }
}
