package org.rascalmpl.repl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReader.Option;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.Terminal.Signal;
import org.jline.terminal.Terminal.SignalHandler;
import org.jline.utils.ShutdownHooks;
import org.rascalmpl.repl.output.IAnsiCommandOutput;
import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.repl.output.IErrorCommandOutput;
import org.rascalmpl.repl.output.IOutputPrinter;

public class BaseREPL {
    
    private final IREPLService replService;
    private final Terminal term;
    private final LineReader reader;
    private volatile boolean keepRunning = true;
    private final @MonotonicNonNull DefaultHistory history;
    private final String normalPrompt;
    private final boolean ansiColorsSupported;
    private final boolean unicodeSupported;

    public BaseREPL(IREPLService replService, Terminal term) {
        this.replService = replService;
        this.term = term;

        var reader = LineReaderBuilder.builder()
            .appName(replService.name())
            .terminal(term)
            .parser(replService.inputParser())
            ;

        if (replService.storeHistory()) {
            reader.variable(LineReader.HISTORY_FILE, replService.historyFile());
            this.history = new DefaultHistory();
            reader.history(this.history);
            ShutdownHooks.add(this.history::save);
        } else {
            this.history = null;
        }
        reader.option(Option.HISTORY_IGNORE_DUPS, replService.historyIgnoreDuplicates());
        reader.option(Option.DISABLE_EVENT_EXPANSION, true); // stop jline expending escaped characters in the input


        if (replService.supportsCompletion()) {
            reader.completer(new AggregateCompleter(replService.completers()));
        }
        this.ansiColorsSupported = !term.getType().equals(Terminal.TYPE_DUMB);
        this.unicodeSupported = term.encoding().newEncoder().canEncode("💓");
        this.normalPrompt = replService.prompt(ansiColorsSupported, unicodeSupported);
        reader.variable(LineReader.SECONDARY_PROMPT_PATTERN, replService.parseErrorPrompt(ansiColorsSupported, unicodeSupported));
        this.reader = reader.build();


        // todo:
        // - highlighting in the prompt? (future work, as it also hurts other parts)
        // - measure time
        // - possible to tee output (future work)
        // - check if the REPL close properly closes the right streams
        // - fix progress bar speed & proper recovering from intermediate prints
    }

    public void run() throws IOException {
        try {
            replService.connect(term, ansiColorsSupported, unicodeSupported);
            var running = setupInterruptHandler();

            while (keepRunning) {
                try {
                    replService.flush();
                    String line = reader.readLine(this.normalPrompt);

                    if (line == null) {
                        // EOF
                        break;
                    }
                    running.set(true);
                    handleInput(line);
                }
                catch (UserInterruptException u) {
                    // only thrown while `readLine` is active
                    reader.printAbove(replService.interruptedPrompt(ansiColorsSupported, unicodeSupported));
                    term.flush();
                }
                finally {
                    running.set(false);
                }
            }
        }
        catch (InterruptedException _e) {
            // closing the runner
        }
        catch (EndOfFileException e) {
            // user pressed ctrl+d or the terminal :quit command was given
            // so exit cleanly
            replService.errorWriter().println("Quiting REPL");
        }
        catch (Throwable e) {
            
            var err = replService.errorWriter();
            if (err.checkError()) {
                err = new PrintWriter(System.err, false);
            }
            
            err.println("Unexpected (uncaught) exception, closing the REPL: ");
            err.print(e.toString());
            e.printStackTrace(err);
            
            err.flush();
    
            throw e;
        }
        finally {
            try {
                replService.flush();
            } catch (Throwable _t) { /* ignore */ }
            try {
                replService.disconnect();
            } catch (Throwable _t) { /* ignore */ }
            term.flush();
            if (this.history != null) {
                ShutdownHooks.remove(this.history::save);
                this.history.save();
            }
        }
    }

    /**
     * Queue a command (separated by newlines) to be "entered"
     * No support for multi-line input
     */
    public void queueCommand(String command) {
        reader.addCommandsInBuffer(Arrays.asList(command.split("[\\n\\r]")));
    }

    private AtomicBoolean setupInterruptHandler() {
        var running = new AtomicBoolean(false);
        var original = new AtomicReference<SignalHandler>(null);
        original.set(term.handle(Signal.INT, (s) -> {
            if (running.get()) {
                try {
                    replService.handleInterrupt();
                }
                catch (InterruptedException e) {
                    return;
                }
            }
            else {
                var fallback = original.get();
                if (fallback != null) {
                    fallback.handle(s);
                }
            }
        }));

        return running;
    }


    private void handleInput(String line) throws InterruptedException {
        writeResult(replService.handleInput(line));
    }

    private void writeResult(ICommandOutput result) {
        PrintWriter target = replService.outputWriter();
        if (result instanceof IErrorCommandOutput) {
            target = replService.errorWriter();
            result = ((IErrorCommandOutput)result).getError();
        }

        IOutputPrinter writer;
        if (ansiColorsSupported && result instanceof IAnsiCommandOutput) {
            writer = ((IAnsiCommandOutput)result).asAnsi();
        }
        else {
            writer = result.asPlain();
        }

        writer.write(target, unicodeSupported);
    }
}
