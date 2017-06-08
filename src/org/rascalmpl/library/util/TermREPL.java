package org.rascalmpl.library.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.StackTrace;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.repl.CompletionResult;
import org.rascalmpl.repl.ILanguageProtocol;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import jline.TerminalFactory;

public class TermREPL {

    private final IValueFactory vf;

    private ILanguageProtocol lang;
    public TermREPL(IValueFactory vf) {
        this.vf = vf;
    }

    public void startREPL(IConstructor repl, IEvaluatorContext ctx) {
        try {
            lang = new TheREPL(vf, repl, ctx);
            // TODO: this used to get a repl from the IEvaluatorContext but that was wrong. Need to fix later. 
            new BaseREPL(lang, null, System.in, System.out, true, true, ((ISourceLocation)repl.get("history")), TerminalFactory.get(), null).run();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace(ctx.getStdErr());
        }
    }

    public static class TheREPL implements ILanguageProtocol {
        private final TypeFactory tf = TypeFactory.getInstance();
        private PrintWriter stdout;
        private PrintWriter stderr;
        private String currentPrompt;
        private final ICallableValue handler;
        private final IEvaluatorContext ctx;
        private final ICallableValue completor;
        private final IValueFactory vf;

        public TheREPL(IValueFactory vf, IConstructor repl, IEvaluatorContext ctx) throws IOException, URISyntaxException {
            this.ctx = ctx;
            this.vf = vf;
            this.handler = (ICallableValue)repl.get("handler");
            this.completor = (ICallableValue)repl.get("completor");
            this.currentPrompt = ((IString)repl.get("prompt")).getValue();
            stdout = ctx.getStdOut();
            assert stdout != null;
            stdout.println(((IString)repl.get("welcome")).getValue());
        }
        
        @Override
        public void cancelRunningCommandRequested() {
            ctx.interrupt();
        }
        
        @Override
        public void terminateRequested() {
            ctx.interrupt();
        }
        
        @Override
        public void stop() {
            ctx.interrupt();
        }
        
        
        @Override
        public void stackTraceRequested() {
            StackTrace trace = ctx.getStackTrace();
            Writer err = ctx.getStdErr();
            try {
                err.write("Current stack trace:\n");
                err.write(trace.toLinkedString());
                err.flush();
            }
            catch (IOException e) {
            } 
        }


        @Override
        public void initialize(Writer stdout, Writer stderr) {
            this.stdout = new PrintWriter(stdout);
            this.stderr = new PrintWriter(stderr);
        }

        @Override
        public String getPrompt() {
            return currentPrompt;
        }

        @Override
        public void handleInput(String line, Map<String,String> output, Map<String,String> metadata) throws InterruptedException {
            ITuple result = (ITuple)call(handler, new Type[] { tf.stringType() }, new IValue[] { vf.string(line) });
            String str = ((IString)result.get(0)).getValue();
            
            // TODO: change the signature of the handler
            // TODO: decode the resulting map with Rascal values to text/html or text/json
            output.put("text/html", str);

            IList errors = (IList)result.get(1);
            for (IValue v: errors) {
                IConstructor msg = (IConstructor)v;
                stderr.write(msg.toString() + "\n");
            }

            currentPrompt = ((IString)result.get(2)).getValue();
        }

        @Override
        public boolean supportsCompletion() {
            return true;
        }

        @Override
        public boolean printSpaceAfterFullCompletion() {
            return false;
        }

        private IValue call(ICallableValue f, Type[] types, IValue[] args) {
            synchronized (ctx) {
                Evaluator eval = (Evaluator)ctx;
                PrintWriter prevErr = eval.getStdErr();
                PrintWriter prevOut = eval.getStdOut();
                try {
                    eval.overrideDefaultWriters(stdout, stderr);
                    return f.call(types, args, null).getValue();
                }
                finally {
                    stdout.flush();
                    stderr.flush();
                    eval.overrideDefaultWriters(prevOut, prevErr);
                }
            }
        }

        @Override
        public CompletionResult completeFragment(String line, int cursor) {
            ITuple result = (ITuple)call(completor, new Type[] { tf.stringType(), tf.integerType() },
                            new IValue[] { vf.string(line), vf.integer(cursor) }); 

            List<String> suggestions = new ArrayList<>();

            for (IValue v: (IList)result.get(1)) {
                suggestions.add(((IString)v).getValue());
            }

            if (suggestions.isEmpty()) {
                return null;
            }

            int offset = ((IInteger)result.get(0)).intValue();

            return new CompletionResult(offset, suggestions);
        }
        
        @Override
        public void handleReset(Map<String,String> output, Map<String,String> metadata) throws InterruptedException {
            // TODO: add a rascal callback for this?
            handleInput("", output, metadata);
        }

        @Override
        public boolean isStatementComplete(String command) {
            // TODO Auto-generated method stub
            return true;
        }

    }



}
