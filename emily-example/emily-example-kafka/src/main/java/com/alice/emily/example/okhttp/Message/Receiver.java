package com.alice.emily.example.okhttp.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Created by Lianhao on 2017/8/21.
 */
@Component
public class Receiver {
    private Gson gson = new GsonBuilder().create();

    @KafkaListener(topics = "mykafka")
    public void processMessage(String content) {
        Message m = gson.fromJson(content, Message.class);
    }
}
