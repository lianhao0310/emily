package com.alice.emily.command;

import com.google.common.base.Preconditions;
import com.alice.emily.core.SpringBeans;
import com.alice.emily.utils.Errors;
import com.alice.emily.utils.Lazy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created by liupin on 2016/11/10.
 */
public class CMD {

    private static Lazy<CommandDispatcher> dispatcher = Lazy.of(() -> {
        CommandDispatcher dispatcher = SpringBeans.getBean(CommandDispatcher.class);
        Preconditions.checkNotNull(dispatcher, "Please check your configuration: emily.command.enabled=true");
        return dispatcher;
    });

    private CMD() { }

    public static CommandMethod<?, ?> getCommand(String category, String action) {
        return dispatcher.get().getCommand(category, action);
    }

    /**
     * cmd details
     */
    public static List<String> categories() {
        return dispatcher.get().getCommands()
                .stream()
                .map(CommandMethod::getCategory)
                .collect(Collectors.toList());
    }

    public static List<String> actions(String category) {
        return dispatcher.get().getCommands(category)
                .stream()
                .map(CommandMethod::getAction)
                .collect(Collectors.toList());
    }

    /**
     * cmd help info
     */
    public static String help(String category, String action) {
        return getCommand(category, action).toString();
    }

    public static List<String> help(String category) {
        List<CommandMethod<?, ?>> commands = dispatcher.get().getCommands(category);
        if (commands == null || commands.isEmpty()) {
            return Collections.emptyList();
        }
        return commands.stream().map(CommandMethod::toString).collect(Collectors.toList());
    }

    public static List<String> help() {
        List<CommandMethod<?, ?>> commands = dispatcher.get().getCommands();
        if (commands == null || commands.isEmpty()) {
            return Collections.emptyList();
        }
        return commands.stream().map(CommandMethod::toString).collect(Collectors.toList());
    }

    /**
     * cmd execute ways
     */
    public static Object sync(String category, String action, Object... args) {
        return dispatcher.get().execute(category, action, args);
    }

    public static CompletableFuture<?> async(String category, String action, Object... args) {
        return dispatcher.get().submit(category, action, args);
    }

    public static Object eval(String... args) {
        Preconditions.checkArgument(args != null && args.length > 0, "args should at least includes action");
        String category = args[0];
        String action = args.length > 1 ? args[1] : null;
        String[] params;
        CommandMethod<?, ?> command = getCommand(category, action);
        if (command != null) {
            params = Arrays.copyOfRange(args, 2, args.length);
        } else {
            command = getCommand("emily", category);
            Preconditions.checkNotNull(command, "No command found for (%s, %s) or (emily, %s)", category, action, action);
            params = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : null;
        }
        try {
            return command.invoke(params);
        } catch (Exception e) {
            throw Errors.service(e, "Cannot execute command %s", command);
        }
    }
}
