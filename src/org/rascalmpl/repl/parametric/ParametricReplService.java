package org.rascalmpl.repl.parametric;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jline.reader.Completer;
import org.jline.reader.Parser;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.repl.IREPLService;
import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.repl.output.IWebContentOutput;
import org.rascalmpl.repl.streams.StreamUtil;

public class ParametricReplService implements IREPLService {

    private final ILanguageProtocol lang;
    private final IDEServices ide;
    private final @Nullable Path historyFile;
    private PrintWriter out;
    private PrintWriter err;

    public ParametricReplService(ILanguageProtocol lang, IDEServices ide, @Nullable Path historyFile) {
        this.lang = lang;
        this.ide = ide;
        this.historyFile = historyFile;
    }


    @Override
    public void connect(Terminal term, boolean ansiColorSupported, boolean unicodeSupported) {
        out = term.writer();
        err = StreamUtil.generateErrorStream(term, term.writer());
        lang.initialize(term.reader(), out, err, ide);
    }

    @Override
    public void disconnect() {
        if (err != null) {
            err.close();
        }
    }

    @Override
    public String prompt(boolean ansiColorsSupported, boolean unicodeSupported) {
        return lang.getPrompt();
    }

    @Override
    public ICommandOutput handleInput(String input) throws InterruptedException {
        var result = lang.handleInput(input);
        if (result instanceof IWebContentOutput) {
            try {
                var webResult = (IWebContentOutput)result;
                ide.browse(webResult.webUri(), webResult.webTitle(), webResult.webviewColumn());
            } catch (Throwable _ignored) {}
        }
        return result;
    }

    @Override
    public void handleInterrupt() throws InterruptedException {
        lang.cancelRunningCommandRequested();
    }


    @Override
    public String parseErrorPrompt(boolean ansiColorSupported, boolean unicodeSupported) {
        return ">> Parse error";
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
        err.flush();
        out.flush();
    }

    @Override
    public String interruptedPrompt(boolean ansiColorsSupported, boolean unicodeSupported) {
        return ">> Interrupted";
    }


    @Override
    public boolean supportsCompletion() {
        return lang.supportsCompletion();
    }

    @Override
    public List<Completer> completers() {
        return Collections.singletonList(new ParametricCompleter(lang));
    }
    
    @Override
    public Parser inputParser() {
        // TODO: allow for a user to provide regexes for the 
        // input parser, to get better completor support and options for multiline
        return new DefaultParser();
    }

    @Override
    public boolean storeHistory() {
        return this.historyFile == null;
    }

    @Override
    public Path historyFile() {
        return this.historyFile;
    }

}
