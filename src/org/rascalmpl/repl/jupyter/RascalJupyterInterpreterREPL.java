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
package org.rascalmpl.repl.jupyter;

import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.parseErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.staticErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwableMessage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.StackTrace;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.result.IRascalResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.IValue;


public abstract class RascalJupyterInterpreterREPL extends JupyterRascalREPL {

    protected Evaluator eval;
    private boolean measureCommandTime;
    
    public RascalJupyterInterpreterREPL()throws IOException, URISyntaxException {
        super();
    }

    public void setMeasureCommandTime(boolean measureCommandTime) {
        this.measureCommandTime = measureCommandTime;
    }

    public boolean getMeasureCommandTime() {
        return measureCommandTime;
    }

    @Override
    public void initialize(Writer stdout, Writer stderr) {
        eval = constructEvaluator(stdout, stderr);
    }

    protected abstract Evaluator constructEvaluator(Writer stdout, Writer stderr);

    @Override
    protected PrintWriter getErrorWriter() {
        return eval.getStdErr();
    }

    @Override
    protected PrintWriter getOutputWriter() {
        return eval.getStdOut();
    }

    @Override
    public void stop() {
        eval.interrupt();
    }

    @Override
    public void cancelRunningCommandRequested() {
        eval.interrupt();
    }

    @Override
    public void terminateRequested() {
        eval.interrupt();
    }

    @Override
    public void stackTraceRequested() {
        StackTrace trace = eval.getStackTrace();
        Writer err = getErrorWriter();
        try {
            err.write("Current stack trace:\n");
            err.write(trace.toLinkedString());
            err.flush();
        }
        catch (IOException e) {
        }
    }

    @Override
    public IRascalResult evalStatement(String statement, String lastLine) throws InterruptedException {
        try {
            Result<IValue> value;
            long duration;

            synchronized(eval) {
                Timing tm = new Timing();
                tm.start();
                value = eval.eval(null, statement, URIUtil.rootLocation("prompt"));
                duration = tm.duration();
            }
            if (measureCommandTime) {
                eval.getStdErr().println("\nTime: " + duration + "ms");
            }
            return value;
        }
        catch (InterruptException ie) {
            eval.getStdErr().println("Interrupted");
            eval.getStdErr().println(ie.getRascalStackTrace().toLinkedString());
            return null;
        }
        catch (ParseError pe) {
            eval.getStdErr().println(parseErrorMessage(lastLine, "prompt", pe));
            return null;
        }
        catch (StaticError e) {
            eval.getStdErr().println(staticErrorMessage(e));
            return null;
        }
        catch (Throw e) {
            eval.getStdErr().println(throwMessage(e));
            return null;
        }
        catch (QuitException q) {
            eval.getStdErr().println("Quiting REPL");
            throw new InterruptedException();
        }
        catch (Throwable e) {
            eval.getStdErr().println(throwableMessage(e, eval.getStackTrace()));
            return null;
        }
    }

    @Override
    public boolean isStatementComplete(String command) {
        System.out.println("follow me 1");
        try {
            eval.parseCommand(null, command, URIUtil.rootLocation("prompt"));
        }
        catch (ParseError pe) {
            System.out.println("not following me");
            String[] commandLines = command.split("\n");
            int lastLine = commandLines.length;
            int lastColumn = commandLines[lastLine - 1].length();

            if (pe.getEndLine() + 1 == lastLine && lastColumn <= pe.getEndColumn()) { 
                return false;
            }
            pe.printStackTrace();
        }
        return true;
    }

    @Override
    protected Collection<String> completePartialIdentifier(String line, int cursor, String qualifier, String term) {
        return eval.completePartialIdentifier(qualifier, term);
    }

    @Override
    protected Collection<String> completeModule(String qualifier, String partialModuleName) {
        List<String> entries = eval.getRascalResolver().listModuleEntries(qualifier);
        if (entries != null && entries.size() > 0) {
            if (entries.contains(partialModuleName)) {
                // we have a full directory name (at least the option)
                List<String> subEntries = eval.getRascalResolver().listModuleEntries(qualifier + "::" + partialModuleName);
                if (subEntries != null) {
                    entries.remove(partialModuleName);
                    subEntries.forEach(e -> entries.add(partialModuleName + "::" + e));
                }
            }
            return entries.stream()
                            .filter(m -> m.startsWith(partialModuleName))
                            .map(s -> qualifier.isEmpty() ? s : qualifier + "::" + s)
                            .sorted()
                            .collect(Collectors.toList());

        }
        return null;
    }
}
