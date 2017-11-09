package com.alice.emily.command;

import com.google.common.collect.Table;
import com.google.common.util.concurrent.MoreExecutors;
import com.alice.emily.core.SpringContext;
import com.alice.emily.utils.Errors;
import com.alice.emily.utils.LOG;
import lombok.Setter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * Created by liupin on 2016/12/10.
 */
@Setter
public class CommandDispatcher implements InitializingBean, DisposableBean {

    private Table<String, String, CommandMethod<?, ?>> commands;
    private Executor executor;
    private ConversionService conversionService;
    private volatile boolean terminate;

    public CommandDispatcher(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void destroy() throws Exception {
        terminate = true;
    }

    @Override
    public void afterPropertiesSet() {
        if (commands == null) {
            return;
        }
        if (executor == null) {
            executor = MoreExecutors.directExecutor();
        }
        if (conversionService == null) {
            conversionService = SpringContext.conversionService();
        }
        terminate = false;
        commands.values().forEach(cm -> cm.setConversionService(conversionService));
        int size = commands.size();
        LOG.CMD.info("Found {} command methods on current classpath", size);
        if (size > 0) {
            LOG.CMD.info("Command categories: {}", commands.rowKeySet());
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
        return commands.get(category, action);
    }

    public List<CommandMethod<?, ?>> getCommands(String category) {
        Map<String, CommandMethod<?, ?>> map = commands.rowMap().get(category);
        if (map == null || map.isEmpty()) {
            return Collections.emptyList();
        }
        return sorted(map.values());
    }

    public List<CommandMethod<?, ?>> getCommands() {
        return sorted(commands.values());
    }

    public Object execute(String category, String action, Object... args) {
        if (terminate) {
            return null;
        }
        CommandMethod<?, ?> commandMethod = commands.get(category, action);
        if (commandMethod == null) {
            throw Errors.service("No command executor found for %s %s", category, action);
        }
        Object result;
        try {
            result = commandMethod.invoke(args);
        } catch (Exception e) {
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
