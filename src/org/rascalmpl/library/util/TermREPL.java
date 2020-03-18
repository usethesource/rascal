package org.rascalmpl.library.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages;
import org.rascalmpl.library.lang.json.io.JsonValueWriter;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.repl.CompletionResult;
import org.rascalmpl.repl.ILanguageProtocol;
import org.rascalmpl.uri.URIResolverRegistry;

import com.google.gson.stream.JsonWriter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import jline.TerminalFactory;

public class TermREPL {
    private final IValueFactory vf;
    private ILanguageProtocol lang;
    
    public TermREPL(IValueFactory vf) {
        this.vf = vf;
    }

    public void startREPL(IConstructor repl, IString title, IString welcome, IString prompt,
        ISourceLocation history, IValue handler, IValue completor, IValue stacktrace, IEvaluatorContext ctx) {
        try {
            lang = new TheREPL(vf, title, welcome, prompt, history, handler, completor, stacktrace, ctx.getInput(), ctx.getStdErr(), ctx.getStdOut());
            new BaseREPL(lang, null, ctx.getInput(), System.out, true, true, history , TerminalFactory.get(), null).run();
        } catch (Throwable e) {
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
        private final ICallableValue stacktrace;
        
        public TheREPL(IValueFactory vf, IString title, IString welcome, IString prompt, ISourceLocation history,
            IValue handler, IValue completor, IValue stacktrace, InputStream input, PrintWriter stderr, PrintWriter stdout) {
            this.vf = vf;
            this.input = input;
            this.stderr = new PrintWriter(stderr);
            this.stdout = new PrintWriter(stdout);
            this.handler = (ICallableValue) handler;
            this.completor = (ICallableValue) completor;
            this.stacktrace = (ICallableValue) stacktrace;
            this.currentPrompt = prompt.getValue();
        }

        @Override
        public void cancelRunningCommandRequested() {
            handler.getEval().interrupt();
            handler.getEval().__setInterrupt(false);
        }
        
        @Override
        public void terminateRequested() {
            handler.getEval().interrupt();
        }
        
        @Override
        public void stop() {
            handler.getEval().interrupt();
        }
        
        @Override
        public void stackTraceRequested() {
            stacktrace.call(new Type[0], new IValue[0], null);
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
                return;
            } 
            else {
                try {
                    handler.getEval().__setInterrupt(false);
                    IConstructor response = (IConstructor)call(handler, new Type[] { tf.stringType() }, new IValue[] { vf.string(line) });
               
                    switch (response.getName()) {
                        case "response":
                            handlePlainTextResponse(output, response);
                            break;
                        case "fileResponse":
                            handleFileResponse(output, response);
                            break;
                        case "jsonResponse":
                            handleJSONResponse(output, response);
                    }
                }
                catch (Throw e) {
                    if (e.getException().toString().equals("interrupt()")) {
                        throw new InterruptedException();
                    }
                    else {
                        throw e;
                    }
                }
                catch (IOException e) {
                    output.put("text/plain", new ByteArrayInputStream(e.getMessage().getBytes()));
                }
            }
        }

        private void handleJSONResponse(Map<String, InputStream> output, IConstructor response) throws IOException {
            IValue data = response.get("val");
            IWithKeywordParameters<? extends IConstructor> kws = response.asWithKeywordParameters();
            
            IValue dtf = kws.getParameter("dateTimeFormat");
            IValue ics = kws.getParameter("implicitConstructors");
            IValue ipn = kws.getParameter("implicitNodes");
            IValue dai = kws.getParameter("dateTimeAsInt");
            
            JsonValueWriter writer = new JsonValueWriter()
                .setCalendarFormat(dtf != null ? ((IString) dtf).getValue() : "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
                .setConstructorsAsObjects(ics != null ? ((IBool) ics).getValue() : true)
                .setNodesAsObjects(ipn != null ? ((IBool) ipn).getValue() : true)
                .setDatesAsInt(dai != null ? ((IBool) dai).getValue() : true);

              final ByteArrayOutputStream baos = new ByteArrayOutputStream();
              
              JsonWriter out = new JsonWriter(new OutputStreamWriter(baos, Charset.forName("UTF8")));
              
              writer.write(out, data);
              out.flush();
              out.close();
              
              output.put("application/json",  new ByteArrayInputStream(baos.toByteArray()));
        }

        private void handleFileResponse(Map<String, InputStream> output, IConstructor response)
            throws UnsupportedEncodingException {
            IString fileMimetype = (IString) response.get("mimeType");
            ISourceLocation file = (ISourceLocation) response.get("file");
            try {
                output.put(fileMimetype.getValue(), URIResolverRegistry.getInstance().getInputStream(file));
            }
            catch (IOException e) {
                output.put("text/plain", new ByteArrayInputStream(e.getMessage().getBytes("UTF8")));
            }
        }

        private void handlePlainTextResponse(Map<String, InputStream> output, IConstructor response)
            throws UnsupportedEncodingException {
            IString content = (IString) response.get("content");
            IString contentMimetype = (IString) response.get("mimeType");
                  
            output.put(contentMimetype.getValue(), new ByteArrayInputStream(content.getValue().getBytes("UTF8")));
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
            handleInput("", output, metadata);
        }

        @Override
        public boolean isStatementComplete(String command) {
            return true;
        }
    }
}
