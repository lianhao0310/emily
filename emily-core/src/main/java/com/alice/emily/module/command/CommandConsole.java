package com.alice.emily.module.command;

import com.google.common.base.Strings;
import com.alice.emily.utils.Errors;
import com.alice.emily.utils.LOG;
import lombok.Getter;
import lombok.Setter;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.IOException;
import java.util.List;

/**
 * Created by lianhao on 2017/1/7.
 */
@Getter
@Setter
public abstract class CommandConsole implements Runnable {

    private Terminal terminal;
    private LineReader lineReader;
    private final String prompt;
    private final String name;

    public CommandConsole(String prompt, String name) {
        this.prompt = new AttributedStringBuilder()
                .append(prompt)
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA))
                .toAnsi();
        this.name = name;
    }

    protected synchronized void initialize() {
        try {
            if (terminal == null) {
                terminal = TerminalBuilder.builder().system(true).build();
            }
            if (lineReader == null) {
                lineReader = LineReaderBuilder.builder()
                        .terminal(terminal)
                        .appName(name)
                        .completer(initCompleter())
                        .build();
            }
        } catch (IOException e) {
            throw Errors.service(e, "Failed to build terminal {}", name);
        }
    }

    protected synchronized void destroy() {
        try {
            if (terminal != null) terminal.close();
        } catch (IOException e) {
            LOG.CMD.error("Failed to destroy terminal {}", name, e);
        }
    }

    @Override
    public void run() {
        try {
            initialize();
            while (!Thread.interrupted()) {
                String line = lineReader.readLine(prompt);
                line = line.trim();
                if (Strings.isNullOrEmpty(line)) continue;
                String[] args = line.split(" ");
                try {
                    Object result = CMD.eval(args);
                    if (result != null) {
                        System.out.println("[RESULT]: " + result);
                        System.out.println();
                    }
                } catch (Exception e) {
                    LOG.CMD.error("Failed to execute cmd {}: ", line, e);
                }
            }
        } catch (Throwable e) {
            LOG.SYSTEM.error("Error occurred when run console {}[{}]", name, prompt, e);
        } finally {
            destroy();
        }
    }

    protected Completer initCompleter() {
        // categories completer
        List<String> categories = CMD.categories();
        StringsCompleter categoryCompleter = new StringsCompleter(categories);

        // actions completer
        Completer actionCompleter = (lineReader, parsedLine, candidates) -> {
            String word = parsedLine.words().get(Math.max(parsedLine.wordIndex() - 1, 0));
            for (String action : CMD.actions(word)) {
                Candidate candidate = new Candidate(
                        AttributedString.stripAnsi(action), action, null, null, null, null, true);
                candidates.add(candidate);
            }
        };

        // rest completer
        NullCompleter nullCompleter = new NullCompleter();
        return new ArgumentCompleter(categoryCompleter, actionCompleter, nullCompleter);
    }
}
