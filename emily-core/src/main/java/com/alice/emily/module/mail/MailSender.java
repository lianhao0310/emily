package com.alice.emily.module.mail;

import java.util.List;
import java.util.Map;

/**
 * Created by lianhao on 2017/1/8.
 */
public interface MailSender {

    void send(String subject, Map<String, Object> model, String... groups);

    void send(String subject, Map<String, Object> model, List<String> ccs, String... groups);

    void send(String subject, String template, Map<String, Object> model, String... groups);

    void send(String subject, String template, Map<String, Object> model, List<String> ccs, String... groups);
}
