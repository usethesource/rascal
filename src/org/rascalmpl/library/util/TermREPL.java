package org.rascalmpl.library.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages;
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
            lang = new TheREPL(vf, repl, ctx.getInput(), ctx.getStdErr(), ctx.getStdOut());
            // TODO: this used to get a repl from the IEvaluatorContext but that was wrong. Need to fix later.
            ISourceLocation history = repl.has("history") ? (ISourceLocation) repl.get("history") : null;
            new BaseREPL(lang, null, ctx.getInput(), System.out, true, true, history , TerminalFactory.get(), null).run();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace(ctx.getStdErr());
        }
    }

    public static class TheREPL implements ILanguageProtocol {
        private final TypeFactory tf = TypeFactory.getInstance();
        private PrintWriter stdout;
        private PrintWriter stderr;
        private InputStream input;
        private String currentPrompt;
        private final ICallableValue handler;
        private final ICallableValue completor;
        private final IValueFactory vf;
        
        public TheREPL(IValueFactory vf, IConstructor repl, InputStream input, Writer stderr, Writer stdout) throws IOException, URISyntaxException {
            this.vf = vf;
            this.input = input;
            this.stderr = new PrintWriter(stderr);
            this.stdout = new PrintWriter(stdout);
            this.handler = (ICallableValue)repl.get("handler");
            this.completor = (ICallableValue)repl.get("completor");
            
            if (repl.has("prompt")) {
                this.currentPrompt = ((IString)repl.get("prompt")).getValue();
            }
        }
        
        @Override
        public void cancelRunningCommandRequested() {
            handler.getEval().interrupt();
        }
        
        @Override
        public void terminateRequested() {
            handler.getEval().interrupt();
        }
        
        @Override
        public void stop() {
            cancelRunningCommandRequested();
        }
        
        @Override
        public void stackTraceRequested() {
            // TODO: add this to the REPL callbacks. It should be a DSL stacktrace. 
        }


        @Override
        public void initialize(InputStream input, Writer stdout, Writer stderr) {
            this.stdout = new PrintWriter(stdout);
            this.stderr = new PrintWriter(stderr);
            this.input = input;
        }

        @Override
        public String getPrompt() {
            return currentPrompt;
        }

        @Override
        public void handleInput(String line, Map<String, InputStream> output, Map<String,String> metadata) throws InterruptedException {
            
            if (line.trim().length() == 0) {
                // cancel command
                // TODO: after doing calling this, the repl gets an unusual behavior, needs to be fixed
                this.stderr.println(ReadEvalPrintDialogMessages.CANCELLED);
                currentPrompt = ReadEvalPrintDialogMessages.PROMPT;
                return;
            } 
            else {
                IConstructor result = (IConstructor)call(handler, new Type[] { tf.stringType() }, new IValue[] { vf.string(line) });
                if (result.has("result") && result.get("result") != null) {
                    String str = ((IString)result.get("result")).getValue() + "\n";
                    output.put("text/plain", new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
                }
            }
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
            synchronized (f.getEval()) {
                Evaluator eval = (Evaluator) f.getEval();
                PrintWriter prevErr = eval.getStdErr();
                PrintWriter prevOut = eval.getStdOut();
                try {
                    eval.overrideDefaultWriters(input, stdout, stderr);
                    return f.call(types, args, null).getValue();
                }
                finally {
                    stdout.flush();
                    stderr.flush();
                    eval.overrideDefaultWriters(eval.getInput(), prevOut, prevErr);
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
        public void handleReset(Map<String, InputStream> output, Map<String, String> metadata) throws InterruptedException {
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
