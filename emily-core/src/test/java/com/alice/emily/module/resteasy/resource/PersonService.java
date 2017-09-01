package com.alice.emily.module.resteasy.resource;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by lianhao on 2016/12/21.
 */
@Service
public class PersonService {

    private Map<String, Person> persons = Maps.newHashMap();

    public Person getPerson(String name) {
        return persons.get(name);
    }

    public Person putPerson(String name, Person person) {
        return persons.put(name, person);
    }
}
