package org.rascalmpl.repl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jline.reader.LineReader;
import org.jline.reader.LineReader.Option;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.utils.ShutdownHooks;

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
        this.unicodeSupported = term.encoding().newEncoder().canEncode("👍🏽");
        this.currentPrompt = replService.prompt(ansiSupported, unicodeSupported);
        reader.variable(LineReader.SECONDARY_PROMPT_PATTERN, replService.parseErrorPrompt(ansiSupported, unicodeSupported));

        this.reader = reader.build();



        // todo:
        // - ctrl + c support
        // - ctrl + / support
        // - multi-line input
        // - highlighting in the prompt?
        // - 
        
    }

    public void run() throws IOException {
        try {
            replService.connect(term);
            while (keepRunning) {
                try {
                    String line = reader.readLine(this.currentPrompt);
                    if (line == null) {
                        // EOF
                        break;
                    }
                    handleInput(line);
                }
                catch (UserInterruptException u) {
                    reader.printAbove("> interrupted");
                    term.flush();
                    var out = new HashMap<String, IOutputPrinter>();
                    replService.handleReset(out, new HashMap<>());
                    writeResult(out);
                }
            }
        }
        catch (InterruptedException _e) {
            // closing the runner
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
