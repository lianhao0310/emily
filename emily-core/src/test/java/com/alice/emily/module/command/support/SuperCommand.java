package com.alice.emily.module.command.support;

import com.alice.emily.module.command.Command;

public interface SuperCommand {
    @Command
    String stop(String msg);

    default String fromParent() {
        return "From Parent";
    }
}