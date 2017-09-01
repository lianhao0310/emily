package com.alice.emily.module.zookeeper.embedded;


import com.alice.emily.module.zookeeper.ZooKeeperProperties;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Lianhao on 2017/8/21.
 */
@Configuration
@ConditionalOnClass({ ZooKeeper.class })
@ConditionalOnProperty(prefix = "emily.zk.embedded", name = "enabled", havingValue = "true")
public class EmbeddedZookeeperConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public EmbeddedZooKeeper embeddedZookeeperServer(ZooKeeperProperties properties) throws Exception {

        ZooKeeperProperties.Zookeeper embedded = properties.getEmbedded();
        EmbeddedZooKeeper zooKeeper = new EmbeddedZooKeeper();
        zooKeeper.setClientPort(embedded.getPort());
        zooKeeper.setDataDir(embedded.getDataDir());
        zooKeeper.setDataLogDir(embedded.getDataLogDir());
        zooKeeper.setTickTime(embedded.getTickTime());
        zooKeeper.setMaxSessionTimeout(embedded.getMaxSessionTimeout());
        zooKeeper.setMinSessionTimeout(embedded.getMinSessionTimeout());
        return zooKeeper;

    }

}
