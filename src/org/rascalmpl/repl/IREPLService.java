/*
 * Copyright (c) 2024-2025, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.repl;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.jline.reader.Completer;
import org.jline.reader.EndOfFileException;
import org.jline.reader.Parser;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.rascalmpl.repl.output.ICommandOutput;

/**
 * This interface implements the behavior of a REPL, without caring about jline or terminals
 */
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
    String name();


    /**
     * Givin a succesfull command (the {@see #inputParser()} returned no errors) execute the input
     * @param input full string of the input terminiated by \n
     * @return a result that can be printed/displayed, depending on the context
     * @throws InterruptedException the thread got getting interrupted, exit, don't print anything
     * @throws StopREPLException the user requested to stop the REPL, clean exit the REPL, no new commands should be send
     */
    ICommandOutput handleInput(String input) throws InterruptedException, StopREPLException;

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
     * Continuation prompt in case of an error
     */
    String parseErrorPrompt(boolean ansiColorSupported, boolean unicodeSupported);

    /**
     * What to print when a command was interrupted (for example by CTRL+C)
     */
    String interruptedPrompt(boolean ansiColorsSupported, boolean unicodeSupported);

    /**
     * Connect the REPL to the Terminal, most likely want to take a copy of at least the {@link Terminal#writer()}.
     * @param term
     */
    void connect(Terminal term, boolean ansiColorSupported, boolean unicodeSupported );

    /**
     * The REPL is getting terminated/disconnected
     */
    void disconnect();

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


}
