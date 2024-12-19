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
package org.rascalmpl.repl.rascal;

import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.values.parsetrees.ITree;


public interface IRascalLanguageProtocol {


    IDEServices buildIDEService(PrintWriter err, IRascalMonitor monitor);

    /**
     * During the constructor call initialize is called after the REPL is setup enough to have a stdout and std err to write to.
     * @param stdout the output stream to write normal output to.
     * @param stderr the error stream to write error messages on, depending on the environment and options passed, will print in red.
     */
    void initialize(Reader input, PrintWriter stdout, PrintWriter stderr, IDEServices services);


    /**
     * Try and parse a command, it's used for the REPL to decide if the command is complete
     * @param command
     * @return
     */
    ITree parseCommand(String command);

    /**
     * After a command has succesfully parsed, this function is called to execute the command
     * @param command command entered.
     * @throws InterruptedException throw this exception to stop the REPL (instead of calling .stop())
     */
    ICommandOutput handleInput(String command) throws InterruptedException;

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


    void flush();

    /**
     * Lookup modules that have a certain path prefix (for example <code>util</code>)
     * @param modulePrefix a module prefix, can be empty, or contain a full subdirectory path like <code>util</code>
     * @return A list of direct siblings of the prefix, either a single module, or a sub directory. It should not be fully qualified, so for the <code>util</code> request it should return <code>Reflective</code> not <code>util::Reflective</code> and directories should be returned as: <code>sub::</code>
     */
    List<String> lookupModules(String modulePrefix);

    /**
     * complete identifiers
     * @param qualifier optionally empty qualitifer (for example <code>IO::</code>)
     * @param partial part to be completed (for example <code>pri</code>)
     * @return map from identifiers to their category (used for grouping in the REPL)
     */
    Map<String, String> completePartialIdentifier(String qualifier, String partial);

    /**
     * which <code>:set</code> options are available on the repl
     * @return map from command line option to description of it
     */
    Map<String, String> availableCommandLineOptions();
}
