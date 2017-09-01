package com.alice.emily.autoconfigure;

import com.alice.emily.module.zookeeper.ZooKeeperProperties;
import com.alice.emily.module.zookeeper.embedded.EmbeddedZookeeperConfiguration;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Lianhao on 2017/8/21.
 */
@Configuration
@ConditionalOnClass(ZooKeeper.class)
@Import(EmbeddedZookeeperConfiguration.class)
@EnableConfigurationProperties(ZooKeeperProperties.class)
public class ZookeeperAutoConfiguration {
}
