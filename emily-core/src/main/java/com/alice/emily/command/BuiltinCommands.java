package com.alice.emily.command;

import com.google.common.base.Strings;
import com.alice.emily.annotation.Command;
import com.alice.emily.annotation.Commands;
import com.alice.emily.annotation.Commands.DiscoverMode;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * Created by lianhao on 2017/1/7.
 */
@Commands(category = "emily", mode = DiscoverMode.ANNOTATED_METHOD)
public class BuiltinCommands implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Command
    public void help(String category) {
        List<String> help = Strings.isNullOrEmpty(category) ? CMD.help() : CMD.help(category);
        for (int i = 0; i < help.size(); i++) {
            String h = help.get(i);
            System.out.println(String.format("       %3d. %s", i, h));
        }
    }

    @Command
    public void quit() {
        try {
            SpringApplication.exit(applicationContext, () -> 0);
        } finally {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
