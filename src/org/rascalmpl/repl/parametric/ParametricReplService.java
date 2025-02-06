/*
 * Copyright (c) 2023-2025, NWO-I CWI and Swat.engineering
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
import org.rascalmpl.repl.streams.StreamUtil;

/**
 * Setup a REPL for any DSL.
 */
public class ParametricReplService implements IREPLService {

    private final ILanguageProtocol lang;
    private final @Nullable Path historyFile;
    private final IDEServices ide;
    private PrintWriter out;
    private PrintWriter err;

    public ParametricReplService(ILanguageProtocol lang, IDEServices ide, @Nullable Path historyFile) {
        this.lang = lang;
        this.historyFile = historyFile;
        this.ide = ide;
    }

    @Override
    public String name() {
        return "Parametric REPL";
    }


    @Override
    public IDEServices connect(Terminal term, boolean ansiColorSupported, boolean unicodeSupported) {
        out = term.writer();
        err = StreamUtil.generateErrorStream(term, term.writer());
        lang.initialize(term.reader(), out, err, ide);
        return ide;
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
        return lang.handleInput(input);
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
        // In the future we could allow for a user to provide regexes for the 
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
