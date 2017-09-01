package com.alice.emily.module.zookeeper.embedded;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.SystemUtils;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.springframework.context.Lifecycle;
import org.springframework.util.ErrorHandler;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.SocketUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Lianhao on 2017/8/21.
 */
@Log4j2
@Getter
public class EmbeddedZooKeeper implements Lifecycle {
    /**
     * ZooKeeper server properties
     */
    @Setter
    private Integer clientPort;
    @Setter
    private String dataDir;
    @Setter
    private String dataLogDir;
    @Setter
    private Integer tickTime = 3000;
    @Setter
    private Integer minSessionTimeout = -1;
    @Setter
    private Integer maxSessionTimeout = -1;

    /**
     * ZooKeeper server
     */
    private ServerCnxnFactory cnxnFactory;
    private ZooKeeperServer zkServer;
    private FileTxnSnapLog txnLog;

    /**
     * {@link ErrorHandler} to be invoked if an Exception is thrown from the ZooKeeper server thread.
     */
    @Setter
    private ErrorHandler errorHandler;

    private Properties prepareProperties() {

        if (StringUtils.isEmpty(dataDir)) {
            File file = new File(SystemUtils.getJavaIoTmpDir(), "zookeeper");
            FileSystemUtils.deleteRecursively(file);
            dataDir = file.getAbsolutePath();
        }

        if (StringUtils.isEmpty(dataLogDir)) {
            dataLogDir = dataDir;
        }

        if (clientPort == null) {
            clientPort = SocketUtils.findAvailableTcpPort();
        }

        Properties properties = new Properties();
        properties.setProperty("clientPort", String.valueOf(clientPort));
        properties.setProperty("dataDir", dataDir);
        properties.setProperty("dataLogDir", dataLogDir);
        properties.setProperty("tickTime", String.valueOf(tickTime));
        properties.setProperty("minSessionTimeout", String.valueOf(minSessionTimeout));
        properties.setProperty("maxSessionTimeout", String.valueOf(maxSessionTimeout));

        return properties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRunning() {
        return (zkServer != null && zkServer.isRunning());
    }

    /**
     * Start the ZooKeeper server in a background thread.
     */
    @Override
    public synchronized void start() {
        if (isRunning()) return;
        try {
            Properties properties = prepareProperties();

            QuorumPeerConfig quorumPeerConfig = new QuorumPeerConfig();
            quorumPeerConfig.parseProperties(properties);

            ServerConfig config = new ServerConfig();
            config.readFrom(quorumPeerConfig);

            txnLog = new FileTxnSnapLog(new File(config.getDataLogDir()), new File(config.getDataDir()));

            zkServer = new ZooKeeperServer();
            zkServer.setTxnLogFactory(txnLog);
            zkServer.setTickTime(config.getTickTime());
            zkServer.setMinSessionTimeout(config.getMinSessionTimeout());
            zkServer.setMaxSessionTimeout(config.getMaxSessionTimeout());

            cnxnFactory = ServerCnxnFactory.createFactory();
            cnxnFactory.configure(config.getClientPortAddress(), config.getMaxClientCnxns());
            cnxnFactory.startup(zkServer);

        } catch (Exception e) {
            if (errorHandler != null) {
                errorHandler.handleError(e);
            } else {
                log.error("Embedded ZooKeeper start failed", e);
            }
        }
    }

    /**
     * Shutdown the ZooKeeper server.
     */
    @Override
    public synchronized void stop() {

        if (zkServer != null) {
            zkServer.shutdown();
            zkServer = null;
        }

        if (cnxnFactory != null) {
            cnxnFactory.closeAll();
            cnxnFactory = null;
        }

        if (txnLog != null) {
            try {
                txnLog.close();
            } catch (IOException ignore) {
            }
            txnLog = null;
        }

    }
}
