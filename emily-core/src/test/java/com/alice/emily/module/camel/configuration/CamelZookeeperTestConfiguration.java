package com.alice.emily.module.camel.configuration;

import org.apache.camel.Body;
import org.apache.camel.Consume;
import org.apache.camel.Header;
import org.apache.camel.component.zookeeper.ZooKeeperMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lianhao on 2017/2/16.
 */
@Configuration
public class CamelZookeeperTestConfiguration {

    @Bean
    public ZookeeperConsumer zookeeperConsumer() {
        return new ZookeeperConsumer();
    }

    public static class ZookeeperConsumer {

        @Consume(uri = "zookeeper://localhost:2181/test/data?repeat=true")
        public void onNode(@Header(ZooKeeperMessage.ZOOKEEPER_NODE) String node, @Body String msg) {
            System.out.println("Received from " + node + ": " + msg);
        }

    }

}
