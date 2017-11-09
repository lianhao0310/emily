package com.alice.emily.template;

import java.io.IOException;
import java.util.Map;

/**
 * Created by lianhao on 2017/5/9.
 */
public interface TemplateService {

    String render(String template, Map<String, Object> model) throws IOException;
}
