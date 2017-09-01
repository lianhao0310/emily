package com.palmaplus.euphoria.module.command.support;

import com.palmaplus.euphoria.module.command.Commands;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Commands(category = "test")
public class TestCommands implements SuperCommand {

    public String date(String cmd) {
        return cmd + " " + new Date();
    }

    public String go(long... ids) {
        return Arrays.toString(ids);
    }

    public String go(String place, long... ids) {
        return place + ": " + Arrays.toString(ids);
    }

    public String go(String place, String... tasks) {
        return place + ": " + Arrays.toString(tasks);
    }

    public String go(String when, String where, String how) {
        return when + ", " + where + ", " + how;
    }

    public String go(String when, String where, String how, double cost) {
        return when + ", " + where + ", " + how + ", " + cost;
    }

    public String id(Long id) {
        return "ID: " + id;
    }

    public Object run(List<String> runners) {
        return runners;
    }

    @Override
    public String stop(String msg) {
        return "Stop: " + msg;
    }

    @Override
    public String toString() {
        return "TestCommands{}";
    }
}