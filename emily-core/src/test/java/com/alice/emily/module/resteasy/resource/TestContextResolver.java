package com.alice.emily.module.resteasy.resource;

import org.springframework.stereotype.Component;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Created by lianhao on 2016/12/6.
 */
@Component
@Provider
public class TestContextResolver implements ContextResolver<PersonService> {
    @Override
    public PersonService getContext(Class<?> type) {
        System.out.println("I am test context resolver");
        return new PersonService();
    }
}
