package com.alice.emily.module.resteasy.resource;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Data
public class Person {

    @NotBlank
    private String name;
    @Range(min = 0, max = 100)
    private int age;
    private Address address;

    @Data
    public static class Address {
        private String street;
    }

    public static Person newPerson(String name, String address, int age) {
        Address a = new Address();
        a.setStreet(address);
        Person person = new Person();
        person.setName(name);
        person.setAddress(a);
        person.setAge(age);
        return person;
    }
}