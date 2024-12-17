package org.rascalmpl.repl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
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
import org.jline.utils.InfoCmp.Capability;

public class BaseREPL2 {
    
    private final IREPLService replService;
    private final Terminal term;
    private final LineReader reader;
    private volatile boolean keepRunning = true;
    private final @MonotonicNonNull DefaultHistory history;
    private String currentPrompt;
    private static final String FALLBACK_MIME_TYPE = "text/plain";
    private static final String ANSI_MIME_TYPE = "text/x-ansi";
    private final boolean ansiSupported;
    private final boolean unicodeSupported;
    private final String mimeType;

    public BaseREPL2(IREPLService replService, Terminal term) {
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

        switch (term.getType()) {
            case Terminal.TYPE_DUMB:
                this.ansiSupported = false;
                this.mimeType = FALLBACK_MIME_TYPE;
                break;
            case Terminal.TYPE_DUMB_COLOR:
                this.ansiSupported = false;
                this.mimeType = ANSI_MIME_TYPE;
                break;
            default:
                this.ansiSupported = true;
                this.mimeType = ANSI_MIME_TYPE;
                break;
        }
        this.unicodeSupported = term.encoding().newEncoder().canEncode("💓");
        this.currentPrompt = replService.prompt(ansiSupported, unicodeSupported);
        reader.variable(LineReader.SECONDARY_PROMPT_PATTERN, replService.parseErrorPrompt(ansiSupported, unicodeSupported));

        this.reader = reader.build();



        // todo:
        // - ctrl + / support (might not be possible)
        // - highlighting in the prompt? (future work, as it also hurts other parts)
        // - nested REPLs
        // - support for html results 
        // - measure time
        // - history?
        // - possible to tee output
    }

    public void run() throws IOException {
        try {
            replService.connect(term);
            var running = setupInterruptHandler();

            while (keepRunning) {
                try {
                    replService.flush();
                    String line = reader.readLine(this.currentPrompt);

                    if (line == null) {
                        // EOF
                        break;
                    }
                    running.set(true);
                    handleInput(line);
                }
                catch (UserInterruptException u) {
                    // only thrown while `readLine` is active
                    reader.printAbove(">>>>>>> Interrupted");
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
        var result = new HashMap<String, IOutputPrinter>();
        var meta = new HashMap<String, String>();
        replService.handleInput(line, result, meta);
        writeResult(result);
    }

    private void writeResult(HashMap<String, IOutputPrinter> result) {
        var writer = result.get(this.mimeType);
        if (writer == null) {
            writer = result.get(FALLBACK_MIME_TYPE);
        }
        if (writer == null) {
            replService.outputWriter().println("Ok");
        }
        else {
            writer.write(replService.outputWriter());
        }
    }
}
