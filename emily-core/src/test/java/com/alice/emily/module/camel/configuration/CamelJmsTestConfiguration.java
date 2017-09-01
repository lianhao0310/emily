package com.alice.emily.module.camel.configuration;

import org.apache.camel.Consume;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lianhao on 2017/2/10.
 */
@Configuration
public class CamelJmsTestConfiguration {

    @Bean
    public JmsConsumer consumer() {
        return new JmsConsumer();
    }

    public static class JmsConsumer {
        @Consume(uri = "sjms:topic:upload?connectionFactory=#jmsConnectionFactory")
        public void onTopic1(String msg) {
            System.out.println("Received from topic: " + msg);
        }

        @Consume(uri = "sjms:topic:upload?connectionFactory=#jmsConnectionFactory")
        public void onTopic2(String msg) {
            System.out.println("Received from topic: " + msg);
        }

        @Consume(uri = "sjms:queue:download?connectionFactory=#jmsConnectionFactory")
        public void onQueue1(String msg) {
            System.out.println("Received from queue: " + msg);
        }

        @Consume(uri = "sjms:queue:download?connectionFactory=#jmsConnectionFactory")
        public void onQueue2(String msg) {
            System.out.println("Received from queue: " + msg);
        }

    }
}
