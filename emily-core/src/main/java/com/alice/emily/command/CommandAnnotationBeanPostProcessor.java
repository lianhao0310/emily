package com.alice.emily.command;

import com.google.common.base.Joiner;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.alice.emily.annotation.Command;
import com.alice.emily.annotation.CommandIgnore;
import com.alice.emily.annotation.Commands;
import com.alice.emily.utils.LOG;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liupin on 2016/11/10.
 */
public class CommandAnnotationBeanPostProcessor
        implements SmartInitializingSingleton, BeanFactoryAware, BeanPostProcessor, DisposableBean {

    private Table<String, String, CommandMethod<?, ?>> commandMethodTable = HashBasedTable.create();

    private BeanFactory beanFactory;

    @Override
    public void afterSingletonsInstantiated() {
        CommandDispatcher dispatcher = this.beanFactory.getBean(CommandDispatcher.class);
        dispatcher.setCommands(commandMethodTable);
        dispatcher.afterPropertiesSet();
    }

    @Override
    public void setBeanFactory(@Nonnull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessAfterInitialization(@Nonnull Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        Commands commands = targetClass.getAnnotation(Commands.class);
        if (commands == null) {
            return bean;
        }
        for (Method method : getCandidateMethods(targetClass, commands)) {
            Command command = AnnotationUtils.findAnnotation(method, Command.class);
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
        ReflectionUtils.doWithMethods(beanClass, methods::add, m -> isValidCommandMethod(commands, m));
        return methods;
    }

    private boolean isValidCommandMethod(Commands commands, Method method) {
        if (method.isAnnotationPresent(CommandIgnore.class)) {
            return false;
        }
        switch (commands.mode()) {
            case PUBLIC_METHOD:
                return Modifier.isPublic(method.getModifiers()) && !ReflectionUtils.isObjectMethod(method);
            case ANNOTATED_METHOD:
                return AnnotationUtils.findAnnotation(method, Command.class) != null;
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

    @Override
    public void destroy() throws Exception {
        commandMethodTable.clear();
    }
}
