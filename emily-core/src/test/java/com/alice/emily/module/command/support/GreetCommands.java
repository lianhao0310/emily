package com.palmaplus.euphoria.module.command.support;

import com.palmaplus.euphoria.module.command.Command;
import com.palmaplus.euphoria.module.command.Commands;
import com.palmaplus.euphoria.module.command.DiscoverMode;

@Commands(category = "greet", mode = DiscoverMode.ANNOTATED_METHOD)
public class GreetCommands implements SuperCommand {

    @Command
    private String hello(String cmd) {
        return Thread.currentThread().getName() + ": " + cmd;
    }

    @Command(category = "greeting")
    public String say(String word) {
        return "said " + word;
    }

    @Command
    public String cmd(CmdObject cmdObject) {
        return "cmdObject: " + cmdObject;
    }

    @Override
    public String stop(String msg) {
        return "Stop: " + msg;
    }
}