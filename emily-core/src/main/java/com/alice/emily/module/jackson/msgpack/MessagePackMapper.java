package com.alice.emily.module.jackson.msgpack;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

/**
 * Created by lianhao on 2017/4/10.
 */
public class MessagePackMapper extends ObjectMapper {

    public MessagePackMapper() {
        super(new MessagePackFactory());
    }
}
