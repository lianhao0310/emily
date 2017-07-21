package com.alice.emily.utils.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;

@SuppressWarnings("deprecation")
public class DelegateLogger implements Logger {

    private final String name;
    private volatile org.apache.logging.log4j.Logger logger;

    public DelegateLogger(String name) {
        this.name = name;
    }


    protected org.apache.logging.log4j.Logger delegate() {
        if (logger == null) {
            synchronized (this) {
                if (logger == null) {
                    logger = LogManager.getLogger(name);
                }
            }
        }
        return logger;
    }

    @Override
    public void catching(Level level, Throwable t) {
        delegate().catching(level, t);
    }

    @Override
    public void catching(Throwable t) {
        delegate().catching(t);
    }

    @Override
    public void debug(Marker marker, Message msg) {
        delegate().debug(marker, msg);
    }

    @Override
    public void debug(Marker marker, Message msg, Throwable t) {
        delegate().debug(marker, msg, t);
    }

    @Override
    public void debug(Marker marker, MessageSupplier msgSupplier) {
        delegate().debug(marker, msgSupplier);
    }

    @Override
    public void debug(Marker marker, MessageSupplier msgSupplier, Throwable t) {
        delegate().debug(marker, msgSupplier, t);
    }

    @Override
    public void debug(Marker marker, CharSequence message) {
        delegate().debug(marker, message);
    }

    @Override
    public void debug(Marker marker, CharSequence message, Throwable t) {
        delegate().debug(marker, message, t);
    }

    @Override
    public void debug(Marker marker, Object message) {
        delegate().debug(marker, message);
    }

    @Override
    public void debug(Marker marker, Object message, Throwable t) {
        delegate().debug(marker, message, t);
    }

    @Override
    public void debug(Marker marker, String message) {
        delegate().debug(marker, message);
    }

    @Override
    public void debug(Marker marker, String message, Object... params) {
        delegate().debug(marker, message, params);
    }

    @Override
    public void debug(Marker marker, String message, Supplier<?>[] paramSuppliers) {
        delegate().debug(marker, message, paramSuppliers);
    }

    @Override
    public void debug(Marker marker, String message, Throwable t) {
        delegate().debug(marker, message, t);
    }

    @Override
    public void debug(Marker marker, Supplier<?> msgSupplier) {
        delegate().debug(marker, msgSupplier);
    }

    @Override
    public void debug(Marker marker, Supplier<?> msgSupplier, Throwable t) {
        delegate().debug(marker, msgSupplier, t);
    }

    @Override
    public void debug(Message msg) {
        delegate().debug(msg);
    }

    @Override
    public void debug(Message msg, Throwable t) {
        delegate().debug(msg, t);
    }

    @Override
    public void debug(MessageSupplier msgSupplier) {
        delegate().debug(msgSupplier);
    }

    @Override
    public void debug(MessageSupplier msgSupplier, Throwable t) {
        delegate().debug(msgSupplier, t);
    }

    @Override
    public void debug(CharSequence message) {
        delegate().debug(message);
    }

    @Override
    public void debug(CharSequence message, Throwable t) {
        delegate().debug(message, t);
    }

    @Override
    public void debug(Object message) {
        delegate().debug(message);
    }

    @Override
    public void debug(Object message, Throwable t) {
        delegate().debug(message, t);
    }

    @Override
    public void debug(String message) {
        delegate().debug(message);
    }

    @Override
    public void debug(String message, Object... params) {
        delegate().debug(message, params);
    }

    @Override
    public void debug(String message, Supplier<?>[] paramSuppliers) {
        delegate().debug(message, paramSuppliers);
    }

    @Override
    public void debug(String message, Throwable t) {
        delegate().debug(message, t);
    }

    @Override
    public void debug(Supplier<?> msgSupplier) {
        delegate().debug(msgSupplier);
    }

    @Override
    public void debug(Supplier<?> msgSupplier, Throwable t) {
        delegate().debug(msgSupplier, t);
    }

    @Override
    public void debug(Marker marker, String message, Object p0) {
        delegate().debug(marker, message, p0);
    }

    @Override
    public void debug(Marker marker, String message, Object p0, Object p1) {
        delegate().debug(marker, message, p0, p1);
    }

    @Override
    public void debug(Marker marker, String message, Object p0, Object p1, Object p2) {
        delegate().debug(marker, message, p0, p1, p2);
    }

    @Override
    public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        delegate().debug(marker, message, p0, p1, p2, p3);
    }

    @Override
    public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        delegate().debug(marker, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        delegate().debug(marker, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        delegate().debug(marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        delegate().debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        delegate().debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        delegate().debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void debug(String message, Object p0) {
        delegate().debug(message, p0);
    }

    @Override
    public void debug(String message, Object p0, Object p1) {
        delegate().debug(message, p0, p1);
    }

    @Override
    public void debug(String message, Object p0, Object p1, Object p2) {
        delegate().debug(message, p0, p1, p2);
    }

    @Override
    public void debug(String message, Object p0, Object p1, Object p2, Object p3) {
        delegate().debug(message, p0, p1, p2, p3);
    }

    @Override
    public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        delegate().debug(message, p0, p1, p2, p3, p4);
    }

    @Override
    public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        delegate().debug(message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        delegate().debug(message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        delegate().debug(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        delegate().debug(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        delegate().debug(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void entry() {
        delegate().entry();
    }

    @Override
    public void entry(Object... params) {
        delegate().entry(params);
    }

    @Override
    public void error(Marker marker, Message msg) {
        delegate().error(marker, msg);
    }

    @Override
    public void error(Marker marker, Message msg, Throwable t) {
        delegate().error(marker, msg, t);
    }

    @Override
    public void error(Marker marker, MessageSupplier msgSupplier) {
        delegate().error(marker, msgSupplier);
    }

    @Override
    public void error(Marker marker, MessageSupplier msgSupplier, Throwable t) {
        delegate().error(marker, msgSupplier, t);
    }

    @Override
    public void error(Marker marker, CharSequence message) {
        delegate().error(marker, message);
    }

    @Override
    public void error(Marker marker, CharSequence message, Throwable t) {
        delegate().error(marker, message, t);
    }

    @Override
    public void error(Marker marker, Object message) {
        delegate().error(marker, message);
    }

    @Override
    public void error(Marker marker, Object message, Throwable t) {
        delegate().error(marker, message, t);
    }

    @Override
    public void error(Marker marker, String message) {
        delegate().error(marker, message);
    }

    @Override
    public void error(Marker marker, String message, Object... params) {
        delegate().error(marker, message, params);
    }

    @Override
    public void error(Marker marker, String message, Supplier<?>[] paramSuppliers) {
        delegate().error(marker, message, paramSuppliers);
    }

    @Override
    public void error(Marker marker, String message, Throwable t) {
        delegate().error(marker, message, t);
    }

    @Override
    public void error(Marker marker, Supplier<?> msgSupplier) {
        delegate().error(marker, msgSupplier);
    }

    @Override
    public void error(Marker marker, Supplier<?> msgSupplier, Throwable t) {
        delegate().error(marker, msgSupplier, t);
    }

    @Override
    public void error(Message msg) {
        delegate().error(msg);
    }

    @Override
    public void error(Message msg, Throwable t) {
        delegate().error(msg, t);
    }

    @Override
    public void error(MessageSupplier msgSupplier) {
        delegate().error(msgSupplier);
    }

    @Override
    public void error(MessageSupplier msgSupplier, Throwable t) {
        delegate().error(msgSupplier, t);
    }

    @Override
    public void error(CharSequence message) {
        delegate().error(message);
    }

    @Override
    public void error(CharSequence message, Throwable t) {
        delegate().error(message, t);
    }

    @Override
    public void error(Object message) {
        delegate().error(message);
    }

    @Override
    public void error(Object message, Throwable t) {
        delegate().error(message, t);
    }

    @Override
    public void error(String message) {
        delegate().error(message);
    }

    @Override
    public void error(String message, Object... params) {
        delegate().error(message, params);
    }

    @Override
    public void error(String message, Supplier<?>[] paramSuppliers) {
        delegate().error(message, paramSuppliers);
    }

    @Override
    public void error(String message, Throwable t) {
        delegate().error(message, t);
    }

    @Override
    public void error(Supplier<?> msgSupplier) {
        delegate().error(msgSupplier);
    }

    @Override
    public void error(Supplier<?> msgSupplier, Throwable t) {
        delegate().error(msgSupplier, t);
    }

    @Override
    public void error(Marker marker, String message, Object p0) {
        delegate().error(marker, message, p0);
    }

    @Override
    public void error(Marker marker, String message, Object p0, Object p1) {
        delegate().error(marker, message, p0, p1);
    }

    @Override
    public void error(Marker marker, String message, Object p0, Object p1, Object p2) {
        delegate().error(marker, message, p0, p1, p2);
    }

    @Override
    public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        delegate().error(marker, message, p0, p1, p2, p3);
    }

    @Override
    public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        delegate().error(marker, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        delegate().error(marker, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        delegate().error(marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        delegate().error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        delegate().error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        delegate().error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void error(String message, Object p0) {
        delegate().error(message, p0);
    }

    @Override
    public void error(String message, Object p0, Object p1) {
        delegate().error(message, p0, p1);
    }

    @Override
    public void error(String message, Object p0, Object p1, Object p2) {
        delegate().error(message, p0, p1, p2);
    }

    @Override
    public void error(String message, Object p0, Object p1, Object p2, Object p3) {
        delegate().error(message, p0, p1, p2, p3);
    }

    @Override
    public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        delegate().error(message, p0, p1, p2, p3, p4);
    }

    @Override
    public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        delegate().error(message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        delegate().error(message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        delegate().error(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        delegate().error(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        delegate().error(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void exit() {
        delegate().exit();
    }

    @Override
    public <R> R exit(R result) {
        return delegate().exit(result);
    }

    @Override
    public void fatal(Marker marker, Message msg) {
        delegate().fatal(marker, msg);
    }

    @Override
    public void fatal(Marker marker, Message msg, Throwable t) {
        delegate().fatal(marker, msg, t);
    }

    @Override
    public void fatal(Marker marker, MessageSupplier msgSupplier) {
        delegate().fatal(marker, msgSupplier);
    }

    @Override
    public void fatal(Marker marker, MessageSupplier msgSupplier, Throwable t) {
        delegate().fatal(marker, msgSupplier, t);
    }

    @Override
    public void fatal(Marker marker, CharSequence message) {
        delegate().fatal(marker, message);
    }

    @Override
    public void fatal(Marker marker, CharSequence message, Throwable t) {
        delegate().fatal(marker, message, t);
    }

    @Override
    public void fatal(Marker marker, Object message) {
        delegate().fatal(marker, message);
    }

    @Override
    public void fatal(Marker marker, Object message, Throwable t) {
        delegate().fatal(marker, message, t);
    }

    @Override
    public void fatal(Marker marker, String message) {
        delegate().fatal(marker, message);
    }

    @Override
    public void fatal(Marker marker, String message, Object... params) {
        delegate().fatal(marker, message, params);
    }

    @Override
    public void fatal(Marker marker, String message, Supplier<?>[] paramSuppliers) {
        delegate().fatal(marker, message, paramSuppliers);
    }

    @Override
    public void fatal(Marker marker, String message, Throwable t) {
        delegate().fatal(marker, message, t);
    }

    @Override
    public void fatal(Marker marker, Supplier<?> msgSupplier) {
        delegate().fatal(marker, msgSupplier);
    }

    @Override
    public void fatal(Marker marker, Supplier<?> msgSupplier, Throwable t) {
        delegate().fatal(marker, msgSupplier, t);
    }

    @Override
    public void fatal(Message msg) {
        delegate().fatal(msg);
    }

    @Override
    public void fatal(Message msg, Throwable t) {
        delegate().fatal(msg, t);
    }

    @Override
    public void fatal(MessageSupplier msgSupplier) {
        delegate().fatal(msgSupplier);
    }

    @Override
    public void fatal(MessageSupplier msgSupplier, Throwable t) {
        delegate().fatal(msgSupplier, t);
    }

    @Override
    public void fatal(CharSequence message) {
        delegate().fatal(message);
    }

    @Override
    public void fatal(CharSequence message, Throwable t) {
        delegate().fatal(message, t);
    }

    @Override
    public void fatal(Object message) {
        delegate().fatal(message);
    }

    @Override
    public void fatal(Object message, Throwable t) {
        delegate().fatal(message, t);
    }

    @Override
    public void fatal(String message) {
        delegate().fatal(message);
    }

    @Override
    public void fatal(String message, Object... params) {
        delegate().fatal(message, params);
    }

    @Override
    public void fatal(String message, Supplier<?>[] paramSuppliers) {
        delegate().fatal(message, paramSuppliers);
    }

    @Override
    public void fatal(String message, Throwable t) {
        delegate().fatal(message, t);
    }

    @Override
    public void fatal(Supplier<?> msgSupplier) {
        delegate().fatal(msgSupplier);
    }

    @Override
    public void fatal(Supplier<?> msgSupplier, Throwable t) {
        delegate().fatal(msgSupplier, t);
    }

    @Override
    public void fatal(Marker marker, String message, Object p0) {
        delegate().fatal(marker, message, p0);
    }

    @Override
    public void fatal(Marker marker, String message, Object p0, Object p1) {
        delegate().fatal(marker, message, p0, p1);
    }

    @Override
    public void fatal(Marker marker, String message, Object p0, Object p1, Object p2) {
        delegate().fatal(marker, message, p0, p1, p2);
    }

    @Override
    public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        delegate().fatal(marker, message, p0, p1, p2, p3);
    }

    @Override
    public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        delegate().fatal(marker, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        delegate().fatal(marker, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        delegate().fatal(marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        delegate().fatal(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        delegate().fatal(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        delegate().fatal(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void fatal(String message, Object p0) {
        delegate().fatal(message, p0);
    }

    @Override
    public void fatal(String message, Object p0, Object p1) {
        delegate().fatal(message, p0, p1);
    }

    @Override
    public void fatal(String message, Object p0, Object p1, Object p2) {
        delegate().fatal(message, p0, p1, p2);
    }

    @Override
    public void fatal(String message, Object p0, Object p1, Object p2, Object p3) {
        delegate().fatal(message, p0, p1, p2, p3);
    }

    @Override
    public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        delegate().fatal(message, p0, p1, p2, p3, p4);
    }

    @Override
    public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        delegate().fatal(message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        delegate().fatal(message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        delegate().fatal(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        delegate().fatal(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        delegate().fatal(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public Level getLevel() {
        return delegate().getLevel();
    }

    @Override
    public <MF extends MessageFactory> MF getMessageFactory() {
        return delegate().getMessageFactory();
    }

    @Override
    public String getName() {
        return delegate().getName();
    }

    @Override
    public void info(Marker marker, Message msg) {
        delegate().info(marker, msg);
    }

    @Override
    public void info(Marker marker, Message msg, Throwable t) {
        delegate().info(marker, msg, t);
    }

    @Override
    public void info(Marker marker, MessageSupplier msgSupplier) {
        delegate().info(marker, msgSupplier);
    }

    @Override
    public void info(Marker marker, MessageSupplier msgSupplier, Throwable t) {
        delegate().info(marker, msgSupplier, t);
    }

    @Override
    public void info(Marker marker, CharSequence message) {
        delegate().info(marker, message);
    }

    @Override
    public void info(Marker marker, CharSequence message, Throwable t) {
        delegate().info(marker, message, t);
    }

    @Override
    public void info(Marker marker, Object message) {
        delegate().info(marker, message);
    }

    @Override
    public void info(Marker marker, Object message, Throwable t) {
        delegate().info(marker, message, t);
    }

    @Override
    public void info(Marker marker, String message) {
        delegate().info(marker, message);
    }

    @Override
    public void info(Marker marker, String message, Object... params) {
        delegate().info(marker, message, params);
    }

    @Override
    public void info(Marker marker, String message, Supplier<?>[] paramSuppliers) {
        delegate().info(marker, message, paramSuppliers);
    }

    @Override
    public void info(Marker marker, String message, Throwable t) {
        delegate().info(marker, message, t);
    }

    @Override
    public void info(Marker marker, Supplier<?> msgSupplier) {
        delegate().info(marker, msgSupplier);
    }

    @Override
    public void info(Marker marker, Supplier<?> msgSupplier, Throwable t) {
        delegate().info(marker, msgSupplier, t);
    }

    @Override
    public void info(Message msg) {
        delegate().info(msg);
    }

    @Override
    public void info(Message msg, Throwable t) {
        delegate().info(msg, t);
    }

    @Override
    public void info(MessageSupplier msgSupplier) {
        delegate().info(msgSupplier);
    }

    @Override
    public void info(MessageSupplier msgSupplier, Throwable t) {
        delegate().info(msgSupplier, t);
    }

    @Override
    public void info(CharSequence message) {
        delegate().info(message);
    }

    @Override
    public void info(CharSequence message, Throwable t) {
        delegate().info(message, t);
    }

    @Override
    public void info(Object message) {
        delegate().info(message);
    }

    @Override
    public void info(Object message, Throwable t) {
        delegate().info(message, t);
    }

    @Override
    public void info(String message) {
        delegate().info(message);
    }

    @Override
    public void info(String message, Object... params) {
        delegate().info(message, params);
    }

    @Override
    public void info(String message, Supplier<?>[] paramSuppliers) {
        delegate().info(message, paramSuppliers);
    }

    @Override
    public void info(String message, Throwable t) {
        delegate().info(message, t);
    }

    @Override
    public void info(Supplier<?> msgSupplier) {
        delegate().info(msgSupplier);
    }

    @Override
    public void info(Supplier<?> msgSupplier, Throwable t) {
        delegate().info(msgSupplier, t);
    }

    @Override
    public void info(Marker marker, String message, Object p0) {
        delegate().info(marker, message, p0);
    }

    @Override
    public void info(Marker marker, String message, Object p0, Object p1) {
        delegate().info(marker, message, p0, p1);
    }

    @Override
    public void info(Marker marker, String message, Object p0, Object p1, Object p2) {
        delegate().info(marker, message, p0, p1, p2);
    }

    @Override
    public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        delegate().info(marker, message, p0, p1, p2, p3);
    }

    @Override
    public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        delegate().info(marker, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        delegate().info(marker, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        delegate().info(marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        delegate().info(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        delegate().info(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        delegate().info(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void info(String message, Object p0) {
        delegate().info(message, p0);
    }

    @Override
    public void info(String message, Object p0, Object p1) {
        delegate().info(message, p0, p1);
    }

    @Override
    public void info(String message, Object p0, Object p1, Object p2) {
        delegate().info(message, p0, p1, p2);
    }

    @Override
    public void info(String message, Object p0, Object p1, Object p2, Object p3) {
        delegate().info(message, p0, p1, p2, p3);
    }

    @Override
    public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        delegate().info(message, p0, p1, p2, p3, p4);
    }

    @Override
    public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        delegate().info(message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        delegate().info(message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        delegate().info(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        delegate().info(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        delegate().info(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public boolean isDebugEnabled() {
        return delegate().isDebugEnabled();
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return delegate().isDebugEnabled(marker);
    }

    @Override
    public boolean isEnabled(Level level) {
        return delegate().isEnabled(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker) {
        return delegate().isEnabled(level, marker);
    }

    @Override
    public boolean isErrorEnabled() {
        return delegate().isErrorEnabled();
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return delegate().isErrorEnabled(marker);
    }

    @Override
    public boolean isFatalEnabled() {
        return delegate().isFatalEnabled();
    }

    @Override
    public boolean isFatalEnabled(Marker marker) {
        return delegate().isFatalEnabled(marker);
    }

    @Override
    public boolean isInfoEnabled() {
        return delegate().isInfoEnabled();
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return delegate().isInfoEnabled(marker);
    }

    @Override
    public boolean isTraceEnabled() {
        return delegate().isTraceEnabled();
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return delegate().isTraceEnabled(marker);
    }

    @Override
    public boolean isWarnEnabled() {
        return delegate().isWarnEnabled();
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return delegate().isWarnEnabled(marker);
    }

    @Override
    public void log(Level level, Marker marker, Message msg) {
        delegate().log(level, marker, msg);
    }

    @Override
    public void log(Level level, Marker marker, Message msg, Throwable t) {
        delegate().log(level, marker, msg, t);
    }

    @Override
    public void log(Level level, Marker marker, MessageSupplier msgSupplier) {
        delegate().log(level, marker, msgSupplier);
    }

    @Override
    public void log(Level level, Marker marker, MessageSupplier msgSupplier, Throwable t) {
        delegate().log(level, marker, msgSupplier, t);
    }

    @Override
    public void log(Level level, Marker marker, CharSequence message) {
        delegate().log(level, marker, message);
    }

    @Override
    public void log(Level level, Marker marker, CharSequence message, Throwable t) {
        delegate().log(level, marker, message, t);
    }

    @Override
    public void log(Level level, Marker marker, Object message) {
        delegate().log(level, marker, message);
    }

    @Override
    public void log(Level level, Marker marker, Object message, Throwable t) {
        delegate().log(level, marker, message, t);
    }

    @Override
    public void log(Level level, Marker marker, String message) {
        delegate().log(level, marker, message);
    }

    @Override
    public void log(Level level, Marker marker, String message, Object... params) {
        delegate().log(level, marker, message, params);
    }

    @Override
    public void log(Level level, Marker marker, String message, Supplier<?>[] paramSuppliers) {
        delegate().log(level, marker, message, paramSuppliers);
    }

    @Override
    public void log(Level level, Marker marker, String message, Throwable t) {
        delegate().log(level, marker, message, t);
    }

    @Override
    public void log(Level level, Marker marker, Supplier<?> msgSupplier) {
        delegate().log(level, marker, msgSupplier);
    }

    @Override
    public void log(Level level, Marker marker, Supplier<?> msgSupplier, Throwable t) {
        delegate().log(level, marker, msgSupplier, t);
    }

    @Override
    public void log(Level level, Message msg) {
        delegate().log(level, msg);
    }

    @Override
    public void log(Level level, Message msg, Throwable t) {
        delegate().log(level, msg, t);
    }

    @Override
    public void log(Level level, MessageSupplier msgSupplier) {
        delegate().log(level, msgSupplier);
    }

    @Override
    public void log(Level level, MessageSupplier msgSupplier, Throwable t) {
        delegate().log(level, msgSupplier, t);
    }

    @Override
    public void log(Level level, CharSequence message) {
        delegate().log(level, message);
    }

    @Override
    public void log(Level level, CharSequence message, Throwable t) {
        delegate().log(level, message, t);
    }

    @Override
    public void log(Level level, Object message) {
        delegate().log(level, message);
    }

    @Override
    public void log(Level level, Object message, Throwable t) {
        delegate().log(level, message, t);
    }

    @Override
    public void log(Level level, String message) {
        delegate().log(level, message);
    }

    @Override
    public void log(Level level, String message, Object... params) {
        delegate().log(level, message, params);
    }

    @Override
    public void log(Level level, String message, Supplier<?>[] paramSuppliers) {
        delegate().log(level, message, paramSuppliers);
    }

    @Override
    public void log(Level level, String message, Throwable t) {
        delegate().log(level, message, t);
    }

    @Override
    public void log(Level level, Supplier<?> msgSupplier) {
        delegate().log(level, msgSupplier);
    }

    @Override
    public void log(Level level, Supplier<?> msgSupplier, Throwable t) {
        delegate().log(level, msgSupplier, t);
    }

    @Override
    public void log(Level level, Marker marker, String message, Object p0) {
        delegate().log(level, marker, message, p0);
    }

    @Override
    public void log(Level level, Marker marker, String message, Object p0, Object p1) {
        delegate().log(level, marker, message, p0, p1);
    }

    @Override
    public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
        delegate().log(level, marker, message, p0, p1, p2);
    }

    @Override
    public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        delegate().log(level, marker, message, p0, p1, p2, p3);
    }

    @Override
    public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        delegate().log(level, marker, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        delegate().log(level, marker, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        delegate().log(level, marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        delegate().log(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        delegate().log(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        delegate().log(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void log(Level level, String message, Object p0) {
        delegate().log(level, message, p0);
    }

    @Override
    public void log(Level level, String message, Object p0, Object p1) {
        delegate().log(level, message, p0, p1);
    }

    @Override
    public void log(Level level, String message, Object p0, Object p1, Object p2) {
        delegate().log(level, message, p0, p1, p2);
    }

    @Override
    public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3) {
        delegate().log(level, message, p0, p1, p2, p3);
    }

    @Override
    public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        delegate().log(level, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        delegate().log(level, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        delegate().log(level, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        delegate().log(level, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        delegate().log(level, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        delegate().log(level, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void printf(Level level, Marker marker, String format, Object... params) {
        delegate().printf(level, marker, format, params);
    }

    @Override
    public void printf(Level level, String format, Object... params) {
        delegate().printf(level, format, params);
    }

    @Override
    public <T extends Throwable> T throwing(Level level, T t) {
        return delegate().throwing(level, t);
    }

    @Override
    public <T extends Throwable> T throwing(T t) {
        return delegate().throwing(t);
    }

    @Override
    public void trace(Marker marker, Message msg) {
        delegate().trace(marker, msg);
    }

    @Override
    public void trace(Marker marker, Message msg, Throwable t) {
        delegate().trace(marker, msg, t);
    }

    @Override
    public void trace(Marker marker, MessageSupplier msgSupplier) {
        delegate().trace(marker, msgSupplier);
    }

    @Override
    public void trace(Marker marker, MessageSupplier msgSupplier, Throwable t) {
        delegate().trace(marker, msgSupplier, t);
    }

    @Override
    public void trace(Marker marker, CharSequence message) {
        delegate().trace(marker, message);
    }

    @Override
    public void trace(Marker marker, CharSequence message, Throwable t) {
        delegate().trace(marker, message, t);
    }

    @Override
    public void trace(Marker marker, Object message) {
        delegate().trace(marker, message);
    }

    @Override
    public void trace(Marker marker, Object message, Throwable t) {
        delegate().trace(marker, message, t);
    }

    @Override
    public void trace(Marker marker, String message) {
        delegate().trace(marker, message);
    }

    @Override
    public void trace(Marker marker, String message, Object... params) {
        delegate().trace(marker, message, params);
    }

    @Override
    public void trace(Marker marker, String message, Supplier<?>[] paramSuppliers) {
        delegate().trace(marker, message, paramSuppliers);
    }

    @Override
    public void trace(Marker marker, String message, Throwable t) {
        delegate().trace(marker, message, t);
    }

    @Override
    public void trace(Marker marker, Supplier<?> msgSupplier) {
        delegate().trace(marker, msgSupplier);
    }

    @Override
    public void trace(Marker marker, Supplier<?> msgSupplier, Throwable t) {
        delegate().trace(marker, msgSupplier, t);
    }

    @Override
    public void trace(Message msg) {
        delegate().trace(msg);
    }

    @Override
    public void trace(Message msg, Throwable t) {
        delegate().trace(msg, t);
    }

    @Override
    public void trace(MessageSupplier msgSupplier) {
        delegate().trace(msgSupplier);
    }

    @Override
    public void trace(MessageSupplier msgSupplier, Throwable t) {
        delegate().trace(msgSupplier, t);
    }

    @Override
    public void trace(CharSequence message) {
        delegate().trace(message);
    }

    @Override
    public void trace(CharSequence message, Throwable t) {
        delegate().trace(message, t);
    }

    @Override
    public void trace(Object message) {
        delegate().trace(message);
    }

    @Override
    public void trace(Object message, Throwable t) {
        delegate().trace(message, t);
    }

    @Override
    public void trace(String message) {
        delegate().trace(message);
    }

    @Override
    public void trace(String message, Object... params) {
        delegate().trace(message, params);
    }

    @Override
    public void trace(String message, Supplier<?>[] paramSuppliers) {
        delegate().trace(message, paramSuppliers);
    }

    @Override
    public void trace(String message, Throwable t) {
        delegate().trace(message, t);
    }

    @Override
    public void trace(Supplier<?> msgSupplier) {
        delegate().trace(msgSupplier);
    }

    @Override
    public void trace(Supplier<?> msgSupplier, Throwable t) {
        delegate().trace(msgSupplier, t);
    }

    @Override
    public void trace(Marker marker, String message, Object p0) {
        delegate().trace(marker, message, p0);
    }

    @Override
    public void trace(Marker marker, String message, Object p0, Object p1) {
        delegate().trace(marker, message, p0, p1);
    }

    @Override
    public void trace(Marker marker, String message, Object p0, Object p1, Object p2) {
        delegate().trace(marker, message, p0, p1, p2);
    }

    @Override
    public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        delegate().trace(marker, message, p0, p1, p2, p3);
    }

    @Override
    public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        delegate().trace(marker, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        delegate().trace(marker, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        delegate().trace(marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        delegate().trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        delegate().trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        delegate().trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void trace(String message, Object p0) {
        delegate().trace(message, p0);
    }

    @Override
    public void trace(String message, Object p0, Object p1) {
        delegate().trace(message, p0, p1);
    }

    @Override
    public void trace(String message, Object p0, Object p1, Object p2) {
        delegate().trace(message, p0, p1, p2);
    }

    @Override
    public void trace(String message, Object p0, Object p1, Object p2, Object p3) {
        delegate().trace(message, p0, p1, p2, p3);
    }

    @Override
    public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        delegate().trace(message, p0, p1, p2, p3, p4);
    }

    @Override
    public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        delegate().trace(message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        delegate().trace(message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        delegate().trace(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        delegate().trace(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        delegate().trace(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public EntryMessage traceEntry() {
        return delegate().traceEntry();
    }

    @Override
    public EntryMessage traceEntry(String format, Object... params) {
        return delegate().traceEntry(format, params);
    }

    @Override
    public EntryMessage traceEntry(Supplier<?>[] paramSuppliers) {
        return delegate().traceEntry(paramSuppliers);
    }

    @Override
    public EntryMessage traceEntry(String format, Supplier<?>[] paramSuppliers) {
        return delegate().traceEntry(format, paramSuppliers);
    }

    @Override
    public EntryMessage traceEntry(Message message) {
        return delegate().traceEntry(message);
    }

    @Override
    public void traceExit() {
        delegate().traceExit();
    }

    @Override
    public <R> R traceExit(R result) {
        return delegate().traceExit(result);
    }

    @Override
    public <R> R traceExit(String format, R result) {
        return delegate().traceExit(format, result);
    }

    @Override
    public void traceExit(EntryMessage message) {
        delegate().traceExit(message);
    }

    @Override
    public <R> R traceExit(EntryMessage message, R result) {
        return delegate().traceExit(message, result);
    }

    @Override
    public <R> R traceExit(Message message, R result) {
        return delegate().traceExit(message, result);
    }

    @Override
    public void warn(Marker marker, Message msg) {
        delegate().warn(marker, msg);
    }

    @Override
    public void warn(Marker marker, Message msg, Throwable t) {
        delegate().warn(marker, msg, t);
    }

    @Override
    public void warn(Marker marker, MessageSupplier msgSupplier) {
        delegate().warn(marker, msgSupplier);
    }

    @Override
    public void warn(Marker marker, MessageSupplier msgSupplier, Throwable t) {
        delegate().warn(marker, msgSupplier, t);
    }

    @Override
    public void warn(Marker marker, CharSequence message) {
        delegate().warn(marker, message);
    }

    @Override
    public void warn(Marker marker, CharSequence message, Throwable t) {
        delegate().warn(marker, message, t);
    }

    @Override
    public void warn(Marker marker, Object message) {
        delegate().warn(marker, message);
    }

    @Override
    public void warn(Marker marker, Object message, Throwable t) {
        delegate().warn(marker, message, t);
    }

    @Override
    public void warn(Marker marker, String message) {
        delegate().warn(marker, message);
    }

    @Override
    public void warn(Marker marker, String message, Object... params) {
        delegate().warn(marker, message, params);
    }

    @Override
    public void warn(Marker marker, String message, Supplier<?>[] paramSuppliers) {
        delegate().warn(marker, message, paramSuppliers);
    }

    @Override
    public void warn(Marker marker, String message, Throwable t) {
        delegate().warn(marker, message, t);
    }

    @Override
    public void warn(Marker marker, Supplier<?> msgSupplier) {
        delegate().warn(marker, msgSupplier);
    }

    @Override
    public void warn(Marker marker, Supplier<?> msgSupplier, Throwable t) {
        delegate().warn(marker, msgSupplier, t);
    }

    @Override
    public void warn(Message msg) {
        delegate().warn(msg);
    }

    @Override
    public void warn(Message msg, Throwable t) {
        delegate().warn(msg, t);
    }

    @Override
    public void warn(MessageSupplier msgSupplier) {
        delegate().warn(msgSupplier);
    }

    @Override
    public void warn(MessageSupplier msgSupplier, Throwable t) {
        delegate().warn(msgSupplier, t);
    }

    @Override
    public void warn(CharSequence message) {
        delegate().warn(message);
    }

    @Override
    public void warn(CharSequence message, Throwable t) {
        delegate().warn(message, t);
    }

    @Override
    public void warn(Object message) {
        delegate().warn(message);
    }

    @Override
    public void warn(Object message, Throwable t) {
        delegate().warn(message, t);
    }

    @Override
    public void warn(String message) {
        delegate().warn(message);
    }

    @Override
    public void warn(String message, Object... params) {
        delegate().warn(message, params);
    }

    @Override
    public void warn(String message, Supplier<?>[] paramSuppliers) {
        delegate().warn(message, paramSuppliers);
    }

    @Override
    public void warn(String message, Throwable t) {
        delegate().warn(message, t);
    }

    @Override
    public void warn(Supplier<?> msgSupplier) {
        delegate().warn(msgSupplier);
    }

    @Override
    public void warn(Supplier<?> msgSupplier, Throwable t) {
        delegate().warn(msgSupplier, t);
    }

    @Override
    public void warn(Marker marker, String message, Object p0) {
        delegate().warn(marker, message, p0);
    }

    @Override
    public void warn(Marker marker, String message, Object p0, Object p1) {
        delegate().warn(marker, message, p0, p1);
    }

    @Override
    public void warn(Marker marker, String message, Object p0, Object p1, Object p2) {
        delegate().warn(marker, message, p0, p1, p2);
    }

    @Override
    public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        delegate().warn(marker, message, p0, p1, p2, p3);
    }

    @Override
    public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        delegate().warn(marker, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        delegate().warn(marker, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        delegate().warn(marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        delegate().warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        delegate().warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        delegate().warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void warn(String message, Object p0) {
        delegate().warn(message, p0);
    }

    @Override
    public void warn(String message, Object p0, Object p1) {
        delegate().warn(message, p0, p1);
    }

    @Override
    public void warn(String message, Object p0, Object p1, Object p2) {
        delegate().warn(message, p0, p1, p2);
    }

    @Override
    public void warn(String message, Object p0, Object p1, Object p2, Object p3) {
        delegate().warn(message, p0, p1, p2, p3);
    }

    @Override
    public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        delegate().warn(message, p0, p1, p2, p3, p4);
    }

    @Override
    public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        delegate().warn(message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        delegate().warn(message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        delegate().warn(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        delegate().warn(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        delegate().warn(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
}