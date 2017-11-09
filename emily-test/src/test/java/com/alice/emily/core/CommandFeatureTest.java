package com.alice.emily.core;

import com.alice.emily.command.CMD;
import com.alice.emily.command.CommandConsole;
import com.alice.emily.core.command.CommandTestConfiguration;
import lombok.SneakyThrows;
import org.apache.commons.io.input.ReaderInputStream;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by lianhao on 2017/2/6.
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, properties = {
        "emily.command.enabled=true",
        "logging.pattern.console=%clr{[%5p] %m%n%xwEx}",
        "endpoints.actuator.enabled=false" })
@Import(CommandTestConfiguration.class)
public class CommandFeatureTest extends AbstractJUnit4SpringContextTests {

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Test
    public void testCommandHelp() {
        CMD.help().forEach(System.out::println);
    }

    @Test
    public void testCommand() throws ExecutionException, InterruptedException {
        System.out.print(CMD.sync("greet", "hello", "hello"));
        assertThat(capture.toString()).isEqualTo("main: hello");
        System.out.println();
        capture.reset();

        System.out.print(CMD.async("greet", "hello", "hi").get());
        assertThat(capture.toString()).endsWith(": hi");
        System.out.println();
        capture.reset();

        System.out.print(CMD.async("greeting", "say", "O(∩_∩)O哈哈~").get());
        assertThat(capture.toString()).isEqualTo("said O(∩_∩)O哈哈~");
        System.out.println();
        capture.reset();

        System.out.print(CMD.async("greet", "stop", "from greet").get());
        assertThat(capture.toString()).isEqualTo("Stop: from greet");
        System.out.println();
        capture.reset();

        System.out.print(CMD.sync("test", "date", "haha"));
        assertThat(capture.toString()).startsWith("haha ");
        System.out.println();
        capture.reset();

        System.out.print(CMD.sync("test", "fromParent"));
        assertThat(capture.toString()).isEqualTo("From Parent");
        System.out.println();
        capture.reset();
    }

    @Test
    public void testEvalCommand() {
        System.out.print(CMD.eval("test", "date"));
        assertThat(capture.toString()).contains("null ");
        System.out.println();
        capture.reset();

        // last parameter is vararg
        System.out.print(CMD.eval("test", "go", "1", "2", "3"));
        assertThat(capture.toString()).isEqualTo("[1, 2, 3]");
        System.out.println();
        capture.reset();

        System.out.print(CMD.eval("test", "go1", "here"));
        assertThat(capture.toString()).isEqualTo("here: null");
        System.out.println();
        capture.reset();

        // last parameter is vararg
        System.out.print(CMD.eval("test", "go2", "here", "eat", "sleep", "hit dou dou"));
        assertThat(capture.toString()).isEqualTo("here: [eat, sleep, hit dou dou]");
        System.out.println();
        capture.reset();

        System.out.print(CMD.eval("test", "go3", "Today", "City Park", "Take Subway"));
        assertThat(capture.toString()).isEqualTo("Today, City Park, Take Subway");
        System.out.println();
        capture.reset();

        System.out.print(CMD.eval("test", "go4", "Today", "City Park", "Take Subway", "350"));
        assertThat(capture.toString()).isEqualTo("Today, City Park, Take Subway, 350.0");
        System.out.println();
        capture.reset();

        // last parameter is List
        System.out.print(CMD.eval("test", "run", "1", "2", "3"));
        assertThat(capture.toString()).isEqualTo("[1, 2, 3]");
        System.out.println();
        capture.reset();

        System.out.print(CMD.eval("test", "id", "4"));
        assertThat(capture.toString()).isEqualTo("ID: 4");
        System.out.println();
        capture.reset();

        System.out.print(CMD.eval("test", "stop", "from test"));
        assertThat(capture.toString()).isEqualTo("Stop: from test");
        System.out.println();
        capture.reset();
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
