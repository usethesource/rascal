package org.rascalmpl.repl;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.jline.reader.Completer;
import org.jline.reader.Parser;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.rascalmpl.repl.output.ICommandOutput;

public interface IREPLService {
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


    /**
     * Should the history of the REPL be stored
     * @return
     */
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


    ICommandOutput handleInput(String input) throws InterruptedException;

    /**
     * Will be called from a different thread then the one that called `handleInput`
     * Should try to stop the running command 
     */
    void handleInterrupt() throws InterruptedException;

    /**
     * Default prompt
     */
    String prompt(boolean ansiColorsSupported, boolean unicodeSupported);

    /**
     * Continuation prompt
     */
    String parseErrorPrompt(boolean ansiColorSupported, boolean unicodeSupported);

    /**
     * Connect the REPL to the Terminal, most likely want to take a copy of at least the {@link Terminal#writer()}.
     * @param term
     */
    void connect(Terminal term);

    /**
     * if a REPL service has wrapped the writer for error output, return that instance
     * @return
     */
    PrintWriter errorWriter();
    /**
     * if a REPL service has wrapped the writer for regular output, return that instance
     * @return
     */
    PrintWriter outputWriter();

    /**
     * Flush the streams, will be triggered at the end of execution, and before showing the prompt.
     */
    void flush();

    String interruptedPrompt(boolean ansiColorsSupported, boolean unicodeSupported);

}
