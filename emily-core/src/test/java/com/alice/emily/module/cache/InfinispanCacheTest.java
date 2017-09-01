package com.alice.emily.module.cache;

import lombok.Data;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.infinispan.Cache;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.QueryFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by lianhao on 2017/2/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.cache.type=infinispan")
public class InfinispanCacheTest {

    @Autowired
    private JCacheTestDemo demo;

    @InfinispanCache("test")
    private Cache<String, String> testCache;

    @InfinispanCache("test_index")
    private Cache<Long, UserInfo> userCache;

    @Test
    public void testJcache() {
        String cacheItem = "O(∩_∩)O哈哈哈~";
        String result = demo.longTimeSearch(cacheItem);
        assertThat(result).isNotBlank().isEqualTo(cacheItem);
        result = demo.longTimeSearch(cacheItem);
        assertThat(result).isNotBlank().isEqualTo(cacheItem);

        demo.deleteResult(cacheItem);
        result = demo.longTimeSearch(cacheItem);
        assertThat(result).isNotBlank().isEqualTo(cacheItem);

        result = testCache.get(cacheItem);
        assertThat(result).isNotBlank().isEqualTo(cacheItem);
    }

    @Test
    public void testQuery() {
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setId(1);
        userInfo1.setUsername("Jack");
        userInfo1.setPassword("123456");
        userInfo1.setAddress("东靖路");

        UserInfo userInfo2 = new UserInfo();
        userInfo2.setId(2);
        userInfo2.setUsername("Jone");
        userInfo2.setPassword("123456");
        userInfo2.setAddress("淞虹路");

        userCache.put(userInfo1.getId(), userInfo1);
        userCache.put(userInfo2.getId(), userInfo2);

        Assert.assertTrue(userCache.containsValue(userInfo1));
        Assert.assertTrue(userCache.containsValue(userInfo2));

        QueryFactory factory = Search.getQueryFactory(userCache);
        List<UserInfo> list = factory.from(UserInfo.class)
                .having("username").eq("Jack")
                .build().list();
        Assert.assertTrue(list.size() == 1);
        Assert.assertEquals(userInfo1, list.get(0));
    }

    @Data
    @Indexed
    public static class UserInfo implements Serializable {
        private long id;
        @Field(analyze = Analyze.NO)
        private String username;
        @Field(analyze = Analyze.NO)
        private String password;
        private String address;
    }
}
