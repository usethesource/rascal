package org.rascalmpl.repl.rascal;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jline.jansi.Ansi;
import org.jline.reader.Completer;
import org.jline.reader.Parser;
import org.jline.terminal.Terminal;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.repl.IREPLService;
import org.rascalmpl.repl.TerminalProgressBarMonitor;
import org.rascalmpl.repl.completers.RascalCommandCompletion;
import org.rascalmpl.repl.completers.RascalIdentifierCompletion;
import org.rascalmpl.repl.completers.RascalKeywordCompletion;
import org.rascalmpl.repl.completers.RascalLocationCompletion;
import org.rascalmpl.repl.completers.RascalModuleCompletion;
import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.repl.streams.StreamUtil;

public class RascalReplServices implements IREPLService {
    private final IRascalLanguageProtocol lang;
    private final @Nullable Path historyFile;

    private boolean unicodeSupported = false;
    private boolean ansiSupported = false;
    private PrintWriter out;
    private PrintWriter err;
    

    public RascalReplServices(IRascalLanguageProtocol lang, @Nullable Path historyFile) {
        this.lang=  lang;
        this.historyFile = historyFile;
    }


    @Override
    public void connect(Terminal term, boolean ansiColorsSupported, boolean unicodeSupported) {
        if (out != null) {
            throw new IllegalStateException("Repl Service is already initialized");
        }
        this.unicodeSupported = unicodeSupported;
        this.ansiSupported = ansiColorsSupported;
        var monitor = new TerminalProgressBarMonitor(term);
        out = monitor;
        err = StreamUtil.generateErrorStream(term, monitor);
        lang.initialize(term.reader(), out, err, monitor, term);
    }

    public void disconnect() {
        if (err != null) {
            err.close();
        }
        if (out != null) {
            out.close();
        }
    }

    @Override
    public Parser inputParser() {
        return new RascalLineParser(lang::parseCommand);
    }

    @Override
    public ICommandOutput handleInput(String input) throws InterruptedException {
        try {
            return lang.handleInput(input);
        }
        catch (ParseError pe) {
            return ParseErrorPrinter.parseErrorMaybePrompt(pe, lang.promptRootLocation(), input, out, ansiSupported, prompt(false, unicodeSupported).length() + 1);
        }
    }


    @Override
    public void handleInterrupt() throws InterruptedException {
        lang.cancelRunningCommandRequested();
    }

    @Override
    public String prompt(boolean ansiColorsSupported, boolean unicodeSupported) {
        String prompt = "rascal>";

        if (ansiColorsSupported) {
            return Ansi.ansi()
                .reset()
                .bold()
                .a(prompt)
                .reset()
                .toString();
        }
        return prompt;
    }

    @Override
    public String parseErrorPrompt(boolean ansiColorsSupported, boolean unicodeSupported) {
        String errorPrompt = (unicodeSupported ? "│" : "|") + "%N %P>";
        if (ansiColorsSupported) {
            return Ansi.ansi()
                .reset()
                .bold()
                .a(errorPrompt)
                .reset()
                .toString();
        }
        return errorPrompt;
    }

    @Override
    public String interruptedPrompt(boolean ansiColorsSupported, boolean unicodeSupported) {
        String prompt = ">>>>>>> Interrupted";
        if (unicodeSupported) {
            prompt = prompt.replace(">", "»");
        }
        if (ansiColorsSupported) {
            return Ansi.ansi()
                .reset()
                .fgRed()
                .bold()
                .a(prompt)
                .reset()
                .toString();
        }
        return prompt;
    }

    @Override
    public PrintWriter errorWriter() {
        return err;
    }

    @Override
    public PrintWriter outputWriter() {
        return out;
    }

    @Override
    public void flush() {
        lang.flush();
        err.flush();
        out.flush();
    }


    @Override
    public boolean supportsCompletion() {
        return true;
    }

    @Override
    public List<Completer> completers() {
        var result = new ArrayList<Completer>();
        var moduleCompleter = new RascalModuleCompletion(lang::lookupModules);
        var idCompleter = new RascalIdentifierCompletion(lang::completePartialIdentifier);
        result.add(new RascalCommandCompletion(
            new TreeMap<>(lang.availableCommandLineOptions()),
            idCompleter::completePartialIdentifier, 
            (s, c) -> moduleCompleter.completeModuleNames(s, c, false)
        ));
        result.add(moduleCompleter);
        result.add(idCompleter);
        result.add(new RascalKeywordCompletion());
        result.add(new RascalLocationCompletion());
        return result;
    }


    @Override
    public boolean storeHistory() {
        return this.historyFile != null;
    }

    @Override
    public Path historyFile() {
        return historyFile;
    }
}
