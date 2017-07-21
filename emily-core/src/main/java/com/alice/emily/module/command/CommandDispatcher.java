package com.alice.emily.module.command;

import com.alice.emily.utils.Errors;
import com.alice.emily.utils.LOG;
import com.alice.emily.utils.Threads;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Created by liupin on 2016/12/10.
 */
public class CommandDispatcher {

    private CommandBeanPostProcessor commandBeanPostProcessor;
    private ExecutorService executor;
    private volatile boolean terminate;

    public CommandDispatcher(CommandBeanPostProcessor commandBeanPostProcessor) {
        this.commandBeanPostProcessor = commandBeanPostProcessor;
    }

    @PostConstruct
    public void start() {
        executor = Threads.newCached("cmd-worker");
        terminate = false;
        int size = commandBeanPostProcessor.getCommands().size();
        LOG.CMD.info("Found {} command methods on current classpath", size);
        if (size > 0) {
            LOG.CMD.info("Command categories: {}", commandBeanPostProcessor.getCommands().rowKeySet());
        }
    }

    @PreDestroy
    public void stop() {
        terminate = true;
        if (executor != null) {
            Threads.shutdown(executor);
        }
    }

    private List<CommandMethod<?, ?>> sorted(Collection<CommandMethod<?, ?>> commandMethods) {
        Comparator<CommandMethod> comparator = Comparator.<CommandMethod, String>comparing(CommandMethod::getCategory)
                .thenComparing(CommandMethod::getAction);
        return commandMethods.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public CommandMethod<?, ?> getCommand(String category, String action) {
        return commandBeanPostProcessor.getCommands().get(category, action);
    }

    public List<CommandMethod<?, ?>> getCommands(String category) {
        Map<String, CommandMethod<?, ?>> map = commandBeanPostProcessor.getCommands().rowMap().get(category);
        if (map == null || map.isEmpty()) {
            return Collections.emptyList();
        }
        return sorted(map.values());
    }

    public List<CommandMethod<?, ?>> getCommands() {
        return sorted(commandBeanPostProcessor.getCommands().values());
    }

    public Object execute(String category, String action, Object... args) {
        if (terminate) {
            return null;
        }
        CommandMethod<?, ?> commandMethod = commandBeanPostProcessor.getCommands().get(category, action);
        if (commandMethod == null) {
            throw Errors.service("No command executor found for %s %s", category, action);
        }
        Object result;
        try {
            result = commandMethod.invoke(args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw Errors.service(e, "Cannot execute command %s", commandMethod);
        }
        return result;
    }

    public CompletableFuture<?> submit(String category, String action, Object... args) {
        if (terminate) {
            return null;
        }
        return CompletableFuture.supplyAsync(() -> execute(category, action, args), executor);
    }
}
