package com.alice.emily.core.command;

import com.alice.emily.annotation.Command;

public interface SuperCommand {
    @Command
    String stop(String msg);

    default String fromParent() {
        return "From Parent";
    }
}