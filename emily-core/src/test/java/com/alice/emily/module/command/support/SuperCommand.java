package com.palmaplus.euphoria.module.command.support;

import com.palmaplus.euphoria.module.command.Command;

public interface SuperCommand {
    @Command
    String stop(String msg);

    default String fromParent() {
        return "From Parent";
    }
}