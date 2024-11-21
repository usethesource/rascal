package org.rascalmpl.repl;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jline.reader.Completer;
import org.jline.reader.Parser;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;

public interface IREPLService {

    String MIME_PLAIN = "text/plain";
    String MIME_ANSI = "text/x-ansi";

    /**
     * Does this language support completion
     * @return
     */
    default boolean supportsCompletion() {
        return false;
    }

    /**
     * Supply completers for this REPL.
     * Note that a completor is only triggered on a per word basis, so you might want to overwrite {@see #completionParser()}
     */
    default List<Completer> completers() {
        return Collections.emptyList();
    }

    /**
     * This parser is respossible for multi-line support, as well as word splitting for completion.
     */
    default Parser inputParser() {
        return new DefaultParser();
    }


    default boolean storeHistory() {
        return false;
    }

    default boolean historyIgnoreDuplicates() {
        return true;
    }

    default Path historyFile() {
        throw new IllegalAccessError("Not implemented if storeHistory is false");
    }

    /**
     * Name of the REPL, no ansi allowed
     */
    default String name() { return "Rascal REPL"; }


    /**
     * Check if an input is valid, for multi-line support
     */
    boolean isInputComplete(String input);


    // todo see if we really need the meta-data
    void handleInput(String input, Map<String, IOutputPrinter> output, Map<String, String> metadata) throws InterruptedException;


    // todo see if we really need the meta-data
    void handleReset(Map<String, IOutputPrinter> output, Map<String, String> metadata) throws InterruptedException;

    /**
     * Default prompt
     */
    String prompt(boolean ansiSupported, boolean unicodeSupported);
    /**
     * Continuation prompt
     */
    String parseErrorPrompt(boolean ansiSupported, boolean unicodeSupported);

    void connect(Terminal term);

    PrintWriter errorWriter();
    PrintWriter outputWriter();

    void flush();

}
