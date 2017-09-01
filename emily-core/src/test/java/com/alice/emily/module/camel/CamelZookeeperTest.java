package com.alice.emily.module.camel;

import com.google.common.util.concurrent.Uninterruptibles;
import com.alice.emily.module.camel.configuration.CamelZookeeperTestConfiguration;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.concurrent.TimeUnit;

/**
 * Created by lianhao on 2017/2/16.
 */
@SpringBootTest
@Import(CamelZookeeperTestConfiguration.class)
@TestPropertySource(properties = "emily.zookeeper.embedded.enabled=true")
public class CamelZookeeperTest extends AbstractJUnit4SpringContextTests {

    @EndpointInject(uri = "zookeeper://localhost:2181/test/data?create=true&createMode=PERSISTENT")
    private ProducerTemplate producerTemplate;

    @Test
    public void testZookeeper() {
        producerTemplate.sendBody("A");
        Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);
        producerTemplate.sendBody("B");
        Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);
        producerTemplate.sendBody("C");
        Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);
        producerTemplate.sendBody("D");
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
    }
}
