package org.rascalmpl.repl.rascal;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.jline.jansi.Ansi;
import org.jline.jansi.AnsiColors;
import org.jline.jansi.Ansi.Color;
import org.jline.reader.Completer;
import org.jline.reader.Parser;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;
import org.rascalmpl.repl.IREPLService;
import org.rascalmpl.repl.TerminalProgressBarMonitor;
import org.rascalmpl.repl.completers.RascalCommandCompletion;
import org.rascalmpl.repl.completers.RascalIdentifierCompletion;
import org.rascalmpl.repl.completers.RascalKeywordCompletion;
import org.rascalmpl.repl.completers.RascalLocationCompletion;
import org.rascalmpl.repl.completers.RascalModuleCompletion;
import org.rascalmpl.repl.jline3.RascalLineParser;
import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.repl.streams.ItalicErrorWriter;
import org.rascalmpl.repl.streams.RedErrorWriter;

public class RascalReplServices implements IREPLService {
    private final IRascalLanguageProtocol lang;

    private PrintWriter out;
    private PrintWriter err;

    public RascalReplServices(IRascalLanguageProtocol lang) {
        this.lang=  lang;
    }

    @Override
    public void connect(Terminal term) {
        if (out != null) {
            throw new IllegalStateException("Repl Service is already initialized");
        }
        var monitor = new TerminalProgressBarMonitor(term);
        out = monitor;
        err = generateErrorStream(term, monitor);
        var service = lang.buildIDEService(err, monitor, term);

        lang.initialize(term.reader(), out, err, service);
    }



    private static PrintWriter generateErrorStream(Terminal tm, Writer out) {
        // previously we would alway write errors to System.err, but that tends to mess up terminals
        // and also our own error print
        // so now we try to not write to System.err
        if (supportsColors(tm)) {
            return new PrintWriter(new RedErrorWriter(out), true);
        }
        if (supportsItalic(tm)) {
            return new PrintWriter(new ItalicErrorWriter(out), true);
        }
        return new PrintWriter(System.err, true);
    
    }

    private static boolean supportsColors(Terminal tm) {
        Integer cols = tm.getNumericCapability(Capability.max_colors);
        return cols != null && cols >= 8;
    }

    private static boolean supportsItalic(Terminal tm) {
        String ital = tm.getStringCapability(Capability.enter_italics_mode);
        return ital != null && !ital.equals("");
    }

    @Override
    public Parser inputParser() {
        return new RascalLineParser(lang::parseCommand);
    }

    @Override
    public ICommandOutput handleInput(String input) throws InterruptedException {
        return lang.handleInput(input);
    }


    @Override
    public void handleInterrupt() throws InterruptedException {
        lang.cancelRunningCommandRequested();
    }

    @Override
    public String prompt(boolean ansiColorsSupported, boolean unicodeSupported) {

        if (ansiColorsSupported) {
            return Ansi.ansi().reset().bold() + "rascal>" + Ansi.ansi().reset();
        }
        return "rascal>";
    }

    @Override
    public String parseErrorPrompt(boolean ansiColorsSupported, boolean unicodeSupported) {
        String errorPrompt = (unicodeSupported ? "│" : "|") + "%N %P>";
        if (ansiColorsSupported) {
            return Ansi.ansi().reset().bold() + errorPrompt + Ansi.ansi().reset();
        }
        return errorPrompt;
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
    public String interruptedPrompt(boolean ansiColorsSupported, boolean unicodeSupported) {
        String prompt = ">>>>>>> Interrupted";
        if (unicodeSupported) {
            prompt = prompt.replace(">", "»");
        }
        if (ansiColorsSupported) {
            prompt = Ansi.ansi().reset().fgRed().bold() + prompt + Ansi.ansi().reset();
        }
        return prompt;
    }
    
}
