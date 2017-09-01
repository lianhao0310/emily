package com.alice.emily.example.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by zk_chs on 16/10/24.
 */
@Component
public class ZkFactory implements ApplicationRunner {

    private static Logger log = LogManager.getLogger();

    @Value("${emily.curator.connect-string}")
    private String host;

    public static CuratorFramework client;

    public CuratorFramework getInstance (){
        return client;
    }


    public void close (){
        log.info("zkClient closing...");
        client.close();
        log.info("zkClient closed...");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("zkClient starting, connecting to {}", host);
        client = CuratorFrameworkFactory.builder()
                .connectString(host)
                .sessionTimeoutMs(30000)
                .connectionTimeoutMs(30000)
                .canBeReadOnly(false)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .defaultData(null)
                .build();
        client.start();
        log.info("zkClient started, connected to {}", host);
    }
}
