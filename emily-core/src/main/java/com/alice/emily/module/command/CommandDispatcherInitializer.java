package com.alice.emily.module.command;

import com.alice.emily.core.BeanProvider;
import com.alice.emily.utils.Errors;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

/**
 * Created by lianhao on 2017/2/6.
 */
public class CommandDispatcherInitializer extends LazyInitializer<CommandDispatcher> {

    @Override
    protected CommandDispatcher initialize() throws ConcurrentException {
        return BeanProvider.getBean(CommandDispatcher.class);
    }

    @Override
    public CommandDispatcher get() {
        try {
            return super.get();
        } catch (ConcurrentException e) {
            throw Errors.rethrow(e, "Command dispatcher initialize failed");
        }
    }
}
