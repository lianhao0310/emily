package com.palmaplus.euphoria.module.cache;

import com.google.common.util.concurrent.Uninterruptibles;
import com.palmaplus.euphoria.module.aop.Audit;

import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;
import java.util.concurrent.TimeUnit;

@Audit("INFO")
public class JCacheTestDemo {

    @CacheResult(cacheName = "test")
    public String longTimeSearch(String input) {
        System.out.println("Start calculate for " + input);
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        return input;
    }

    @CacheRemove(cacheName = "test")
    public void deleteResult(String input) {
        System.out.println("Delete result for " + input);
    }

}