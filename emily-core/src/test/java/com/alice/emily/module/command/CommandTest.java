package com.palmaplus.euphoria.module.command;

import lombok.SneakyThrows;
import org.apache.commons.io.input.ReaderInputStream;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

/**
 * Created by liupin on 2017/2/6.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "euphoria.command.enabled=true",
                "logging.pattern.console=%clr{[%5p] %m%n%xwEx}",
                "endpoints.actuator.enabled=false"
        }
)
public class CommandTest extends AbstractJUnit4SpringContextTests {

    @Test
    public void testCommandHelp() {
        CMD.help().forEach(System.out::println);
    }

    @Test
    public void testCommand() throws ExecutionException, InterruptedException {
        System.out.println(CMD.sync("greet", "hello", "hello"));
        System.out.println(CMD.async("greet", "hello", "hi").get());
        System.out.println(CMD.eval("greet", "cmd", "1", "haha"));
        System.out.println(CMD.async("greeting", "say", "O(∩_∩)O哈哈~").get());
        System.out.println(CMD.async("greet", "stop", "from greet").get());

        System.out.println(CMD.sync("test", "date", "haha"));
        System.out.println(CMD.eval("test", "date"));
        System.out.println(CMD.eval("test", "go", "1", "2", "3"));
        System.out.println(CMD.eval("test", "go1", "here"));
        System.out.println(CMD.eval("test", "go2", "here", "eat", "sleep", "hit dou dou"));
        System.out.println(CMD.eval("test", "go3", "Today", "City Park", "Take Subway"));
        System.out.println(CMD.eval("test", "go4", "Today", "City Park", "Take Subway", "350"));
        System.out.println(CMD.eval("test", "run", "1", "2", "3"));
        System.out.println(CMD.eval("test", "id", "4"));
        System.out.println(CMD.eval("test", "stop", "from test"));
        System.out.println(CMD.sync("test", "fromParent"));
    }

    @Test
    @Ignore
    public void testConsole() {
        StringBuilder sb = new StringBuilder()
                .append("help").append("\n")
                .append("test go 1 2 3").append("\n")
                .append("quit").append("\n");
        ReaderInputStream inputStream = new ReaderInputStream(new StringReader(sb.toString()), StandardCharsets.UTF_8);
        new CommandConsole("Euphoria Test", "Test") {
            @Override
            @SneakyThrows
            protected synchronized void initialize() {
                Terminal terminal = TerminalBuilder.builder()
                        .system(true)
//                        .streams(inputStream, System.out)
                        .build();
                setTerminal(terminal);
                super.initialize();
            }
        }.run();
    }
}
