package com.alice.emily.autoconfigure;

import com.alice.emily.curator.CuratorClientConfiguration;
import com.alice.emily.curator.CuratorProperties;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by lianhao on 2017/6/7.
 */
@Configuration
@ConditionalOnClass({ CuratorFramework.class })
@Import({ CuratorClientConfiguration.class })
@AutoConfigureAfter(ZookeeperAutoConfiguration.class)
@EnableConfigurationProperties(CuratorProperties.class)
public class CuratorAutoConfiguration {

}
