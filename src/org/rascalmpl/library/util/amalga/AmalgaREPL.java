/** 
 * Copyright (c) 2017, Mauricio Verano Merino, Centrum Wiskunde & Informatica (CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.library.util.amalga;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Map;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.repl.CompletionResult;
import org.rascalmpl.repl.ILanguageProtocol;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeFactory;
import jline.TerminalFactory;

public class AmalgaREPL {
    
    private IValueFactory vf;
    
    public AmalgaREPL(IValueFactory valueFactory){
        vf = valueFactory;
    }
    
    public IString example(IString name, IEvaluatorContext ctx){
        return name;
    }
    
    public IValue sum( IInteger n1, IInteger n2, IEvaluatorContext ctx){
        return n1.add(n2);
    }

    public void startREPL(IConstructor repl, IEvaluatorContext ctx) {
        try {
            // TODO: this used to get a repl from the IEvaluatorContext but that was wrong. Need to fix later. 
            new BaseREPL(new Amalga(repl, ctx), null, System.in, System.out, true, true, ((ISourceLocation)repl.get("history")), TerminalFactory.get(), null).run();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace(ctx.getStdErr());
        }
    }
    class Amalga implements ILanguageProtocol{
        private final TypeFactory tf = TypeFactory.getInstance();
        private PrintWriter stdout;
        private PrintWriter stderr;
        private String currentPrompt;
        private final ICallableValue handler;
        private final IEvaluatorContext ctx;
        private final ICallableValue completor;
        
        public Amalga(IConstructor repl, IEvaluatorContext ctx) throws IOException, URISyntaxException {
            this.ctx = ctx;
            this.handler = (ICallableValue)repl.get("handler");
            this.completor = (ICallableValue)repl.get("completor");
            this.currentPrompt = ((IString)repl.get("prompt")).getValue();
            stdout = new PrintWriter(System.out);
            assert stdout != null;
            stdout.println(((IString)repl.get("welcome")).getValue());
        }

        @Override
        public void initialize(Writer stdout, Writer stderr) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String getPrompt() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void handleInput(String line, Map<String, String> output, Map<String, String> metadata)
            throws InterruptedException {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void handleReset(Map<String, String> output, Map<String, String> metadata) throws InterruptedException {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean supportsCompletion() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean printSpaceAfterFullCompletion() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public CompletionResult completeFragment(String line, int cursor) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void cancelRunningCommandRequested() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void terminateRequested() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void stackTraceRequested() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isStatementComplete(String command) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void stop() {
            // TODO Auto-generated method stub
            
        }
    }
}
