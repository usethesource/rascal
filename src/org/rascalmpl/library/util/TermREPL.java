package org.rascalmpl.library.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import jline.TerminalFactory;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.repl.CompletionResult;

public class TermREPL {

    private final IValueFactory vf;

    public TermREPL(IValueFactory vf) {
        this.vf = vf;
    }

    public void startREPL(IConstructor repl, IEvaluatorContext ctx) {
        try {
            new TheREPL(repl, ctx).run();
        } catch (IOException e) {
            e.printStackTrace(ctx.getStdErr());
        }
    }

    class TheREPL extends BaseREPL {
        private final TypeFactory tf = TypeFactory.getInstance();
        private PrintWriter stdout;
        private PrintWriter stderr;
        private String currentPrompt;
        private final ICallableValue handler;
        private final IEvaluatorContext ctx;
        private final ICallableValue completor;

        public TheREPL(IConstructor repl, IEvaluatorContext ctx) throws IOException {
            super(System.in, System.out, true, true, ((ISourceLocation)repl.get("history")), TerminalFactory.get());
            this.ctx = ctx;
            this.handler = (ICallableValue)repl.get("handler");
            this.completor = (ICallableValue)repl.get("completor");
            this.currentPrompt = ((IString)repl.get("prompt")).getValue();
            assert stdout != null;
            stdout.println(((IString)repl.get("welcome")).getValue());
        }


        @Override
        protected void initialize(Writer stdout, Writer stderr) {
            this.stdout = new PrintWriter(stdout);
            this.stderr = new PrintWriter(stderr);
        }

        @Override
        protected String getPrompt() {
            return currentPrompt;
        }

        @Override
        protected void handleInput(String line) throws InterruptedException {
            ITuple result = (ITuple)call(handler, new Type[] { tf.stringType() }, new IValue[] { vf.string(line) });
            String str = ((IString)result.get(0)).getValue();
            if (!str.isEmpty()) {
                stdout.write(str + "\n");
            }

            IList errors = (IList)result.get(1);
            for (IValue v: errors) {
                IConstructor msg = (IConstructor)v;
                stderr.write(msg.toString() + "\n");
            }

            currentPrompt = ((IString)result.get(2)).getValue();
        }

        @Override
        protected boolean supportsCompletion() {
            return true;
        }

        @Override
        protected boolean printSpaceAfterFullCompletion() {
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
        protected CompletionResult completeFragment(String line, int cursor) {
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

    }



}
