package com.palmaplus.euphoria.module.camel;

import com.google.common.util.concurrent.Uninterruptibles;
import com.palmaplus.euphoria.module.camel.configuration.CamelJmsTestConfiguration;
import org.apache.activemq.artemis.jms.client.ActiveMQDestination;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.apache.activemq.artemis.jms.client.ActiveMQTopic;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.concurrent.TimeUnit;

/**
 * Created by liupin on 2017/2/10.
 */
@SpringBootTest
@Import(CamelJmsTestConfiguration.class)
@TestPropertySource(properties = "spring.artemis.embedded.enabled=true")
public class CamelSJMSTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private JmsTemplate jmsTemplate;

    @EndpointInject(uri = "sjms:topic:upload?connectionFactory=#jmsConnectionFactory")
    private ProducerTemplate jmsTopicProducer;

    @EndpointInject(uri = "sjms:queue:download?connectionFactory=#jmsConnectionFactory")
    private ProducerTemplate jmsQueueProducer;

    @Test
    public void testJMS() {
        ActiveMQTopic topic = ActiveMQDestination.createTopic("upload");
        jmsTemplate.convertAndSend(topic, "A");
        jmsTopicProducer.sendBody("B");
        ActiveMQQueue queue = ActiveMQDestination.createQueue("download");
        jmsTemplate.convertAndSend(queue, "C");
        jmsTemplate.convertAndSend(queue, "D");
        jmsQueueProducer.sendBody("E");
        jmsQueueProducer.sendBody("F");
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
    }
}
