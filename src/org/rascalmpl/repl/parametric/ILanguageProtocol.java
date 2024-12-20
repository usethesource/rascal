/**
 * Copyright (c) 2017, Jurgen J. Vinju, Mauricio Verano, Centrum Wiskunde & Informatica (CWI) All
 * rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.repl.parametric;

import java.io.PrintWriter;
import java.io.Reader;
import java.util.Map;

import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.repl.output.ICommandOutput;


public interface ILanguageProtocol {

    /**
     * During the constructor call initialize is called after the REPL is setup enough to have a stdout and std err to write to.
     * @param stdout the output stream to write normal output to.
     * @param stderr the error stream to write error messages on, depending on the environment and options passed, will print in red.
     */
    void initialize(Reader input, PrintWriter stdout, PrintWriter stderr, IDEServices services);

    /**
     * Will be called everytime a new prompt is printed.
     * @return The string representing the prompt.
     */
    String getPrompt();

    /**
     * After a newline is pressed, the current line is handed to this method.
     * @param line the current line entered.
     * @param output is a map from mime-type string to output string (this is the result of a computation)
     * @param metadata is a map to encode a plain object with meta-data encoded as strings
     * @throws InterruptedException throw this exception to stop the REPL (instead of calling .stop())
     */
    ICommandOutput handleInput(String line) throws InterruptedException;
    
    /**
     * Test if completion of statement in the current line is supported
     * @return true if the completeFragment method can provide completions
     */
    boolean supportsCompletion();

    /**
     * If a user hits the TAB key, the current line and the word the cursor is at is provided, you can only provide completions for the current word.
     * @param line The current line.
     * @param word which word in the line the user pressed TAB on
     * @return suggestions for the word (key: completion, value: category)
     */
    Map<String, String> completeFragment(String line, String word);

    /**
     * This method gets called from another thread, and indicates the user pressed CTLR-C during a call to handleInput.
     * 
     * Interrupt the handleInput code as soon as possible, but leave stuff in a valid state.
     */
    void cancelRunningCommandRequested();

    /**
     * This method gets called from another thread, indicates a user pressed CTRL+\ during a call to handleInput.
     * 
     * If possible, print the current stack trace.
     */
    void stackTraceRequested();
    
}
