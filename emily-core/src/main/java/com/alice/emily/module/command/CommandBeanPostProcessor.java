package com.alice.emily.module.command;

import com.google.common.base.Joiner;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.alice.emily.utils.Annotations;
import com.alice.emily.utils.LOG;
import com.alice.emily.utils.Reflection;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lianhao on 2016/11/10.
 */
public class CommandBeanPostProcessor implements BeanPostProcessor, DisposableBean {

    private Table<String, String, CommandMethod<?, ?>> commandMethodTable = HashBasedTable.create();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        Commands commands = targetClass.getAnnotation(Commands.class);
        if (commands == null) {
            return bean;
        }
        for (Method method : getCandidateMethods(targetClass, commands)) {
            Command command = Annotations.getAnnotation(method, Command.class);
            // add command to command method table
            try {
                String category = getCategory(beanName, commands, command);
                String action = getAction(method, category, command);
                CommandMethod<?, ?> commandMethod = new CommandMethod<>(bean, method, category, action);
                commandMethodTable.put(category, action, commandMethod);
                LOG.CMD.trace("CMD: {}", commandMethod);
            } catch (Exception e) {
                LOG.CMD.warn("Cannot process command from bean: {}", bean.getClass().getCanonicalName(), e);
            }
        }
        return bean;
    }

    private List<Method> getCandidateMethods(Class<?> beanClass, Commands commands) {
        List<Method> methods = Lists.newArrayList();
        methods.addAll(Reflection.getAllMethods(beanClass));
        methods.removeIf(method -> !isValidCommandMethod(commands, method));
        return methods;
    }

    private boolean isValidCommandMethod(Commands commands, Method method) {
        if (method.isAnnotationPresent(CommandIgnore.class)) {
            return false;
        }
        switch (commands.mode()) {
            case PUBLIC_METHOD:
                return Modifier.isPublic(method.getModifiers());
            case ANNOTATED_METHOD:
                return Annotations.isAnnotationPresent(method, Command.class);
            default:
                return false;
        }
    }

    private String getCategory(String beanName, Commands commands, Command command) {
        String category = null;

        // if category provides on @Commands, use it
        if (commands != null) {
            category = commands.category();
        }

        // if category provides on @Command, use it in favor of @Commands
        if (command != null && !"default".equals(command.category())) {
            category = command.category();
        }

        // otherwise use bean name, consider default as not provided
        if (category == null || "default".equals(category)) {
            category = beanName;
        }

        return category;
    }

    private String getAction(Method method, String category, Command command) {
        String action = null;

        // if action is provided, use it
        if (command != null && !"default".equals(command.action())) {
            action = command.action();
        }

        // otherwise use method name, consider default as not provided
        if (action == null || "default".equals(action)) {
            action = method.getName();
        }

        // ensure action not conflict
        int index = computeActionIndex(method, category, action);

        return index == 0 ? action : action + index;
    }

    private int computeActionIndex(Method method, String category, String action) {
        int index = 0;

        Map<String, CommandMethod<?, ?>> commandMethodMap = commandMethodTable.rowMap().get(category);
        if (commandMethodMap == null || !commandMethodMap.containsKey(action)) {
            return index;
        }

        // get sorted command methods
        Comparator<CommandMethod<?, ?>> comparator = Comparator.comparingInt(cm -> cm.getMethod().getParameterCount());
        comparator = comparator.thenComparing(cm -> Joiner.on(",").join(cm.getMethod().getParameterTypes()));
        List<CommandMethod<?, ?>> methods = commandMethodMap.values()
                .stream()
                .filter(cm -> cm.getAction().startsWith(action))
                .sorted(comparator)
                .collect(Collectors.toList());

        methods.forEach(m -> commandMethodTable.remove(category, m.getAction()));

        // reindex the command methods in the table
        int originCount = method.getParameterCount();
        for (int i = 0; i < methods.size(); i++) {
            CommandMethod<?, ?> cm = methods.get(i);
            String newAction;
            int count = cm.getMethod().getParameterCount();

            // calculate new action and index
            if (count <= originCount) {
                newAction = action + (i == 0 ? "" : i);
                index = i + 1;
            } else {
                newAction = action + (i + 1);
            }

            commandMethodTable.put(category, newAction, cm.clone(newAction));
        }
        return index;
    }

    public Table<String, String, CommandMethod<?, ?>> getCommands() {
        return commandMethodTable;
    }

    @Override
    public void destroy() throws Exception {
        commandMethodTable.clear();
    }
}
