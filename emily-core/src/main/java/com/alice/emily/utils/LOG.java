package com.alice.emily.utils;

import com.google.common.base.Strings;
import com.alice.emily.utils.logging.DelegateLogger;
import com.alice.emily.utils.logging.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.RolloverDescriptionImpl;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.action.AbstractAction;
import org.apache.logging.log4j.core.appender.rolling.action.FileRenameAction;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.BurstFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by lianhao on 2016/12/7.
 */
public class LOG {

    public static final Logger SYSTEM = getLogger("emily");
    public static final Logger CONFIG = getLogger("emily.config");
    public static final Logger THREAD = getLogger("emily.thread");
    public static final Logger JPA = getLogger("emily.jpa");
    public static final Logger RPC = getLogger("emily.rpc");
    public static final Logger CACHE = getLogger("emily.cache");
    public static final Logger CMD = getLogger("emily.command");
    public static final Logger SERVLET = getLogger("emily.servlet");
    public static final Logger REST = getLogger("emily.rest");
    public static final Logger ZOOKEEPER = getLogger("emily.zookeeper");
    public static final Logger MAIL = getLogger("emily.mail");

    public static final Logger AUDIT = getLogger("emily.audit");

    private LOG() { }

    public static Logger getLogger(String name) {
        return new DelegateLogger(name);
    }

    public static Logger getLogger(Class<?> clazz) {
        return new DelegateLogger(clazz.getCanonicalName());
    }

    private static LoggerContext context() {
        return (LoggerContext) LogManager.getContext(false);
    }

    private static PatternLayout patternLayout() {
        Configuration configuration = context().getConfiguration();
        String pattern = System.getProperty("EUPHORIA_LOG_PATTERN");
        pattern = Strings.isNullOrEmpty(pattern)
                ? "%d{yyyy-MM-dd HH:mm:ss.SSS} %7p ${sys:EUPHORIA_PID} --- [%20.20t]%-40.40c{1.} : %m%n" : pattern;
        return PatternLayout.newBuilder()
                .withPattern(pattern)
                .withConfiguration(configuration)
                .build();
    }

    public static Level parseLevel(String level) {
        if (Strings.isNullOrEmpty(level)) return Level.INFO;
        switch (level.toUpperCase()) {
            case "OFF": return Level.OFF;
            case "FATAL": return Level.FATAL;
            case "ERROR": return Level.ERROR;
            case "WARN": return Level.WARN;
            case "INFO": return Level.INFO;
            case "DEBUG": return Level.DEBUG;
            case "TRACE": return Level.TRACE;
            case "ALL": return Level.ALL;
            default: return Level.INFO;
        }
    }

    // Note! when use a name not already defined in the context
    // log4j2 will return root logger config instead
    private static LoggerConfig loggerConfig(String name) {
        Configuration configuration = context().getConfiguration();
        LoggerConfig config = configuration.getLoggerConfig(name);
        if (config == null) return null;
        LoggerConfig rootLogger = configuration.getRootLogger();
        if (config == rootLogger && rootLogger.getName().equals(name)) return config;
        if (!config.getName().equals(name)) return null;
        return config;
    }

    private static ConsoleAppender console() {
        Configuration configuration = context().getConfiguration();
        return configuration.getAppenders().values().stream()
                .filter(appender -> appender instanceof ConsoleAppender)
                .findFirst()
                .map(appender -> (ConsoleAppender) appender)
                .orElseGet(
                        () -> ConsoleAppender.newBuilder()
                                .withLayout(patternLayout())
                                .withName("Console")
                                .setFollow(true)
                                .build()
                );
    }

    private static Appender fileAppender(String location, boolean burst) {
        Configuration configuration = context().getConfiguration();
        Appender appender = configuration.getAppender(location);
        if (appender == null) {
            // 创建BurstFilter控制日志打印频率
            Filter filter = null;
            if (burst) {
                filter = BurstFilter.newBuilder().setRate(16).build();
            }
            // 创建RollingRandomAccessFileAppender
            SizeBasedTriggeringPolicy policy = SizeBasedTriggeringPolicy.createPolicy("250 MB");
            // 创建RolloverStrategy，使日志文件只维持当前一份儿
            RolloverStrategy strategy = manager -> {
                final String currentFileName = manager.getFileName();
                final String renameTo = currentFileName + ".1";
                final File dst = new File(renameTo);
                final FileRenameAction renameAction = new FileRenameAction(new File(currentFileName), dst, manager.isRenameEmptyFiles());
                final AbstractAction deleteAction = new AbstractAction() {
                    @Override
                    public boolean execute() throws IOException {
                        return dst.delete();
                    }
                };
                return new RolloverDescriptionImpl(currentFileName, false, renameAction, deleteAction);
            };
//            DefaultRolloverStrategy strategy = DefaultRolloverStrategy.createStrategy("1", null, null, null, null, true, configuration);
            appender = RollingFileAppender.newBuilder()
                    .withName(location)
                    .withAppend(true)
                    .withCreateOnDemand(true)
                    .withFileName(location)
                    .withFilePattern(location + ".%i.gz")
                    .withPolicy(policy)
                    .withStrategy(strategy)
                    .withLayout(patternLayout())
                    .withFilter(filter)
                    .withConfiguration(configuration)
                    .build();
            configuration.addAppender(appender);
            appender.start();
        }
        return appender;
    }

    public static void updateLogger(String name, String level) {
        updateLogger(name, level, null, false);
    }

    /**
     * 更新日志配置， 提供location时会为日志添加RandomAccessFileAppender
     *
     * @param name     日志名称
     * @param level    日志级别
     * @param location 日志记录文件地址
     */
    public static synchronized Logger updateLogger(String name, String level, String location, boolean burst) {
        checkNotNull(name, "logger name should be provided");
        checkNotNull(level, "logging level should be provided");

        Configuration configuration = context().getConfiguration();
        Level logLevel = parseLevel(level);

        // 获取或创建Logger定义
        LoggerConfig loggerConfig = loggerConfig(name);
        if (loggerConfig == null) {
            AppenderRef[] refs = new AppenderRef[0];
            if (!Strings.isNullOrEmpty(location)) {
                AppenderRef ref = AppenderRef.createAppenderRef(location, null, null);
                refs = new AppenderRef[]{ref};
            }
            // whether it pass the log to the parent. eg: root
            boolean additivity = refs.length == 0;
            loggerConfig = LoggerConfig.createLogger(additivity, logLevel, name, null, refs, null, configuration, null);
            configuration.addLogger(name, loggerConfig);
        }

        // 更新Logger定义
        loggerConfig.setLevel(logLevel);

        // 添加Appender
        if (!Strings.isNullOrEmpty(location)) {
            Appender appender = fileAppender(location, burst);
            loggerConfig.addAppender(appender, logLevel, null);
        }

        context().updateLoggers();
        return getLogger(name);
    }
}
