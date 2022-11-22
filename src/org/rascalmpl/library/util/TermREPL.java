package org.rascalmpl.library.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.library.lang.json.internal.JsonValueWriter;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.repl.CompletionResult;
import org.rascalmpl.repl.ILanguageProtocol;
import org.rascalmpl.repl.REPLContentServer;
import org.rascalmpl.repl.REPLContentServerManager;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

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
    private final IRascalValueFactory vf;
    private ILanguageProtocol lang;
    private final OutputStream out;
    private final OutputStream err;
    private final InputStream in;


    public TermREPL(IRascalValueFactory vf, OutputStream out, OutputStream err, InputStream in) {
        this.vf = vf;
        this.out = out;
        this.err = err;
        this.in = in;
    }

    public ITuple newREPL(IConstructor repl, IString title, IString welcome, IString prompt, IString quit,
        ISourceLocation history, IFunction handler, IFunction completor, IFunction stacktrace, IEvaluatorContext eval) {
        lang = new TheREPL(vf, title, welcome, prompt, quit, history, handler, completor, stacktrace, in, err, out);
        BaseREPL baseRepl;
        try {
            baseRepl = new BaseREPL(lang, null, in, err, out, true, true, history, TerminalFactory.get(), null);
        }
        catch (Throwable e) {
            throw RuntimeExceptionFactory.io(e.getMessage());
        }

        TypeFactory tf = TypeFactory.getInstance();
        IFunction run = vf.function(tf.functionType(tf.voidType(), tf.tupleEmpty(), tf.tupleEmpty()), 
            (args, kwargs) -> {
                try {
                    baseRepl.run();
                }
                catch (IOException e) {
                    throw RuntimeExceptionFactory.io(e.getMessage());
                }
                return vf.tuple();
            }); 

        IFunction send = vf.function(tf.functionType(tf.voidType(), tf.tupleType(tf.stringType()), tf.tupleEmpty()), 
            (args, kwargs) -> {
                baseRepl.queueCommand(((IString)args[0]).getValue());
                return vf.tuple();
            });
        
        return vf.tuple(run, send);
    }

    public static class TheREPL implements ILanguageProtocol {
        private final REPLContentServerManager contentManager = new REPLContentServerManager();
        private final TypeFactory tf = TypeFactory.getInstance();
        private OutputStream stdout;
        private OutputStream stderr;
        private InputStream input;
        private String currentPrompt;
        private String quit;
        private final AbstractFunction handler;
        private final AbstractFunction completor;
        private final IValueFactory vf;
        private final AbstractFunction stacktrace;
        private IDEServices services;

        public TheREPL(IValueFactory vf, IString title, IString welcome, IString prompt, IString quit, ISourceLocation history,
            IFunction handler, IFunction completor, IValue stacktrace, InputStream input, OutputStream stderr, OutputStream stdout) {
            this.vf = vf;
            this.input = input;
            this.stderr = stderr;
            this.stdout = stdout;
            
            // TODO: these casts mean that TheRepl only works with functions produced by the
            // interpreter for now. The reason is that the REPL needs access to environment configuration
            // parameters of these functions such as stdout, stdin, etc.
            // TODO: rethink the term repl in the compiled context, based on the compiled REPL for Rascal
            // which does not exist yet.
            this.handler = (AbstractFunction) handler;
            this.completor = (AbstractFunction) completor;
            this.stacktrace = (AbstractFunction) stacktrace;
            this.currentPrompt = prompt.getValue();
            this.quit = quit.getValue();
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
        public void initialize(InputStream input, OutputStream stdout, OutputStream stderr, IDEServices services) {
            this.stdout = stdout;
            this.stderr = stderr;
            this.input = input;
            this.services = services;
        }

        @Override
        public String getPrompt() {
            return currentPrompt;
        }

        @Override
        public void handleInput(String line, Map<String, InputStream> output, Map<String,String> metadata) throws InterruptedException {

            if (line.trim().equals(quit)) {
                throw new InterruptedException(quit);
            }
            else {
                try {
                    handler.getEval().__setInterrupt(false);
                    IConstructor content = (IConstructor) call(handler, new Type[] { tf.stringType() }, new IValue[] { vf.string(line) });

                    if (content.has("id")) {
                        handleInteractiveContent(output, metadata, content);
                    }
                    else {
                        IConstructor response = (IConstructor) content.get("response");
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
                }
                catch (IOException e) {
                    output.put("text/plain", new ByteArrayInputStream(e.getMessage().getBytes()));
                }
                catch (Throwable e) {
                    output.put("text/plain",  new ByteArrayInputStream(e.getMessage() != null ? e.getMessage().getBytes() : e.getClass().getName().getBytes()));
                }
            }
        }
        
        private void handleInteractiveContent(Map<String, InputStream> output, Map<String, String> metadata,
            IConstructor content) throws IOException, UnsupportedEncodingException {
            String id = ((IString) content.get("id")).getValue();
            Function<IValue, IValue> callback = liftProviderFunction(content.get("callback"));
            REPLContentServer server = contentManager.addServer(id, callback);
            
            String URL = "http://localhost:" + server.getListeningPort();
            
            produceHTMLResponse(id, URL, output, metadata);
        }
        
        private void produceHTMLResponse(String id, String URL, Map<String, InputStream> output, Map<String, String> metadata) throws UnsupportedEncodingException{
            String html;
            if (metadata.containsKey("origin") && metadata.get("origin").equals("notebook"))
                html = "<script> \n var "+ id +" = new Salix('"+ id + "', '" + URL + "'); \n google.charts.load('current', {'packages':['corechart']}); google.charts.setOnLoadCallback(function () { registerCharts("+ id +");\n registerDagre(" + id + ");\n registerTreeView("+ id +"); \n"+ id + ".start();\n});\n </script> \n <div id = \"" + id + "\"> \n </div>";
            else
                html = "<iframe class=\"rascal-content-frame\" src=\""+ URL +"\"></iframe>";
            
            metadata.put("url", URL);

            output.put("text/html", new ByteArrayInputStream(html.getBytes("UTF8")));
            
            String message = "Serving visual content at |" + URL + "|";
            output.put("text/plain", new ByteArrayInputStream(message.getBytes("UTF8")));
            
        }

        private Function<IValue, IValue> liftProviderFunction(IValue callback) {
            IFunction func = (IFunction) callback;

            return (t) -> {
                // This function will be called from another thread (the webserver)
                // That is problematic if the current repl is doing something else at that time.
                // The evaluator is already locked by the outer Rascal REPL (if this REPL was started from `startREPL`).
                //              synchronized(eval) {
                return func.call(t);
            };
        }

        private void handleJSONResponse(Map<String, InputStream> output, IConstructor response) throws IOException {
            IValue data = response.get("val");
            IWithKeywordParameters<? extends IConstructor> kws = response.asWithKeywordParameters();

            IValue dtf = kws.getParameter("dateTimeFormat");
            IValue dai = kws.getParameter("dateTimeAsInt");
            
            JsonValueWriter writer = new JsonValueWriter()
                .setCalendarFormat(dtf != null ? ((IString) dtf).getValue() : "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
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

        private IValue call(IFunction f, Type[] types, IValue[] args) {
            if (f instanceof AbstractFunction) {
                Evaluator eval = (Evaluator) ((AbstractFunction) f).getEval();
                synchronized (eval) {
                    try {
                        eval.overrideDefaultWriters(input, stdout, stderr);
                        return f.call(args);
                    }
                    finally {
                        try {
                            stdout.flush();
                            stderr.flush();
                            eval.revertToDefaultWriters();
                        }
                        catch (IOException e) {
                            // ignore
                        }
                    }
                }
            }
            else {
                throw RuntimeExceptionFactory.illegalArgument(f, "term repl only works with interpreter for now");
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
