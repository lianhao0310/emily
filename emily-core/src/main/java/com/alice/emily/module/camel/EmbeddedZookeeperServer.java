package com.alice.emily.module.camel;

import com.alice.emily.utils.Errors;
import com.alice.emily.utils.LOG;
import org.apache.zookeeper.server.NIOServerCnxnFactory;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.springframework.beans.factory.DisposableBean;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.file.Files;


public class EmbeddedZookeeperServer implements DisposableBean {

    private int port = 2181;
    private ServerCnxnFactory factory;

    public EmbeddedZookeeperServer(int port) {
        this.port = port;
    }

    public void startup() {
        try {
            File snapshotDir = Files.createTempDirectory("zookeeper-snapshot").toFile();
            File logDir = Files.createTempDirectory("zookeeper-logs").toFile();
            snapshotDir.deleteOnExit();
            logDir.deleteOnExit();
            int tickTime = 500;
            ZooKeeperServer zkServer = new ZooKeeperServer(snapshotDir, logDir, tickTime);
            this.factory = NIOServerCnxnFactory.createFactory();
            this.factory.configure(new InetSocketAddress("localhost", port), 16);
            factory.startup(zkServer);
            LOG.ZOOKEEPER.debug("Start local zookeeper server listening on {}", port);
        } catch (Exception e) {
            throw Errors.service("Unable to start local ZooKeeper server", e);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (factory != null) {
            factory.shutdown();
            LOG.ZOOKEEPER.debug("Stop local zookeeper server!");
        }
    }
}