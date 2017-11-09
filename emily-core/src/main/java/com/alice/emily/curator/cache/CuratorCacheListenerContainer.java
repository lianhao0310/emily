package com.alice.emily.curator.cache;

import com.alice.emily.curator.CuratorOperations;
import lombok.Setter;
import org.springframework.context.Lifecycle;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * Created by lianhao on 2017/6/13.
 */
@Setter
public class CuratorCacheListenerContainer implements Lifecycle {

    private Set<CuratorCacheListenerAdapter> containers;

    private final CuratorOperations operations;
    private final ThreadPoolTaskExecutor executor;

    private volatile boolean running = false;

    public CuratorCacheListenerContainer(CuratorOperations operations,
                                         ThreadPoolTaskExecutor executor) {
        this.operations = operations;
        this.executor = executor;
    }

    @Override
    public void start() {
        if (!CollectionUtils.isEmpty(containers)) {
            for (CuratorCacheListenerAdapter container : containers) {
                container.setExecutor(executor);
                container.setOperations(operations);
                container.start();
            }
        }
        this.running = true;
    }

    @Override
    public void stop() {
        if (!CollectionUtils.isEmpty(containers)) {
            for (CuratorCacheListenerAdapter container : containers) {
                if (container.isRunning()) {
                    container.stop();
                }
            }
        }
        this.running = false;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }
}
