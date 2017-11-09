package com.alice.emily.autoconfigure;

import com.alice.emily.zookeeper.ZooKeeperProperties;
import com.alice.emily.zookeeper.embedded.EmbeddedZookeeperConfiguration;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by lianhao on 2017/6/26.
 */
@Configuration
@ConditionalOnClass(ZooKeeper.class)
@Import(EmbeddedZookeeperConfiguration.class)
@EnableConfigurationProperties(ZooKeeperProperties.class)
public class ZookeeperAutoConfiguration {
}
