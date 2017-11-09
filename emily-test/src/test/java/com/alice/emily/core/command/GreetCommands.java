package com.alice.emily.core.command;

import com.alice.emily.annotation.Command;
import com.alice.emily.annotation.Commands;
import com.alice.emily.annotation.Commands.DiscoverMode;

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

    @Override
    public String stop(String msg) {
        return "Stop: " + msg;
    }
}