package com.alice.emily.utils;

import org.junit.Test;

/**
 * Created by Lianhao on 2017/12/18.
 */
public class LazyTest {
    private Lazy<Person> stringLazy = Lazy.of(Person::new);
    @Test
    public void testGet(){
        System.out.println(stringLazy); //com.alice.emily.utils.Lazy@2a2d45ba
        System.out.println(stringLazy.get()); //mike lian
    }
    public class Person{
        private String firstName;
        private String lastName;
        public Person(){
            this.firstName = "mike";
            this.lastName = "lian";
        }

        public String toString(){
            return this.firstName + " " + this.lastName;
        }
    }
}
