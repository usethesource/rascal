package org.rascalmpl.library.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.jline.reader.EndOfFileException;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.library.lang.json.internal.JsonValueWriter;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.repl.http.REPLContentServer;
import org.rascalmpl.repl.http.REPLContentServerManager;
import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.repl.output.IErrorCommandOutput;
import org.rascalmpl.repl.output.INotebookOutput;
import org.rascalmpl.repl.output.IOutputPrinter;
import org.rascalmpl.repl.output.ISourceLocationCommandOutput;
import org.rascalmpl.repl.output.IWebContentOutput;
import org.rascalmpl.repl.output.MimeTypes;
import org.rascalmpl.repl.output.impl.AsciiStringOutputPrinter;
import org.rascalmpl.repl.parametric.ILanguageProtocol;
import org.rascalmpl.repl.parametric.ParametricReplService;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import com.google.gson.stream.JsonWriter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class TermREPL {
    private final IRascalValueFactory vf;
    private final IDEServices service;
    private final PrintWriter err;
    private ILanguageProtocol lang;


    public TermREPL(IRascalValueFactory vf, IDEServices service, PrintWriter _ignoredOut, PrintWriter err) {
        this.vf = vf;
        this.service = service;
        this.err = err;
    }

    private Path resolveHistoryFile(ISourceLocation historyFile) {
        try {
            ISourceLocation result = URIResolverRegistry.getInstance().logicalToPhysical(historyFile);
            if (result == null || !result.getScheme().equals("file")) {
                err.println("Cannot resolve history file to file on disk");
                return null;
            }
            return Path.of(result.getPath());
        }
        catch (IOException e) {
            return null;
        }
    }

    public ITuple newREPL(IConstructor repl, IString title, IString welcome, IString prompt, IString quit,
        ISourceLocation history, IFunction handler, IFunction completor, IFunction stacktrace, IEvaluatorContext eval) {
        var term = service.activeTerminal();
        if (term == null) {
            throw RuntimeExceptionFactory.io("No terminal found in IDE service, we cannot allocate a REPL");
        }
        lang = new TheREPL(vf, title, welcome, prompt, quit, history, handler, completor, stacktrace);

        BaseREPL baseRepl;
        try {
            baseRepl = new BaseREPL(new ParametricReplService(lang, service, resolveHistoryFile(history)), term);
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
        private PrintWriter stdout;
        private PrintWriter stderr;
        private Reader input;
        private final String currentPrompt;
        private String quit;
        private final AbstractFunction handler;
        private final AbstractFunction completor;
        private final IValueFactory vf;
        private final AbstractFunction stacktrace;

        public TheREPL(IValueFactory vf, IString title, IString welcome, IString prompt, IString quit, ISourceLocation history,
            IFunction handler, IFunction completor, IValue stacktrace) {
            this.vf = vf;
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
        public void initialize(Reader input, PrintWriter stdout, PrintWriter stderr, IDEServices services) {
            this.input = input;
            this.stdout = stdout;
            this.stderr = stdout;
        }

        @Override
        public void cancelRunningCommandRequested() {
            handler.getEval().interrupt();
        }

        @Override
        public void stackTraceRequested() {
            var stack = stacktrace.call(new Type[0], new IValue[0], null);
            if (stack != null && stack.getDynamicType() == tf.stringType()) {
                stderr.println(((IString)stack.getValue()).getValue());
            }
        }

        @Override
        public String getPrompt() {
            return currentPrompt;
        }

        @Override
        public ICommandOutput handleInput(String line) throws InterruptedException {
            if (line.trim().equals(quit)) {
                throw new EndOfFileException();
            }
            try {
                handler.getEval().__setInterrupt(false);
                IConstructor content = (IConstructor) call(handler, new Type[] { tf.stringType() }, new IValue[] { vf.string(line) });

                if (content.has("id")) {
                    return handleInteractiveContent(content);
                }
                else {
                    IConstructor response = (IConstructor) content.get("response");
                    switch (response.getName()) {
                        case "response":
                            return handlePlainTextResponse(response);
                        case "fileResponse":
                            return handleFileResponse(response);
                        case "jsonResponse":
                            return handleJSONResponse(response);
                        default: 
                            return errorResponse("Unexpected constructor: " + response.getName());
                    }
                }
            }
            catch (IOException e) {
                return errorResponse(e.getMessage());
            }
            catch (Throwable e) {
                return errorResponse(e.getMessage());
            }
        }

        private ICommandOutput errorResponse(String message) {
            return new IErrorCommandOutput() {
                @Override
                public ICommandOutput getError() {
                    return () -> new AsciiStringOutputPrinter(message);
                }
                @Override
                public IOutputPrinter asPlain() {
                    return new AsciiStringOutputPrinter(message);
                }
            };
        }

        
        private ICommandOutput handleInteractiveContent(IConstructor content) throws IOException, UnsupportedEncodingException {
            String id = ((IString) content.get("id")).getValue();
            Function<IValue, IValue> callback = liftProviderFunction(content.get("callback"));
            REPLContentServer server = contentManager.addServer(id, callback);
            
            return produceHTMLResponse(id, URIUtil.assumeCorrect("http", "localhost:" + server.getListeningPort(), ""));
        }

        abstract class NotebookWebContentOutput implements INotebookOutput, IWebContentOutput {}
        
        private ICommandOutput produceHTMLResponse(String id, URI URL) throws UnsupportedEncodingException{
            return new NotebookWebContentOutput() {
                @Override
                public IOutputPrinter asNotebook() {
                    return new IOutputPrinter() {
                        @Override
                        public void write(PrintWriter target, boolean unicodeSupported) {
                            target.println("<script>");
                            target.println(" var "+ id +" = new Salix('"+ id + "', '" + URL + "');");
                            target.println(" google.charts.load('current', {'packages':['corechart']});");
                            target.println(" google.charts.setOnLoadCallback(function () { ");
                            target.println("    registerCharts("+ id +");");

                            target.println("    registerDagre(" + id + ");");
                            target.println("    registerTreeView("+ id +");");
                            target.println("    " + id + ".start();");
                            target.println("});");
                            target.println("<script>");
                            target.println("<div id = \"" + id + "\"> </div>");
                        }

                        @Override
                        public String mimeType() {
                            return MimeTypes.HTML;
                        }
                        
                    };
                }

                @Override
                public IOutputPrinter asHtml() {
                    return new AsciiStringOutputPrinter( "<iframe class=\"rascal-content-frame\" src=\""+ URL +"\"></iframe>", MimeTypes.HTML);
                }

                @Override
                public IOutputPrinter asPlain() {
                    return new AsciiStringOutputPrinter("Serving visual content at |" + URL + "|", MimeTypes.PLAIN_TEXT);
                }

                @Override
                public URI webUri() {
                    return URL;
                }

                @Override
                public String webTitle() {
                    // TODO: extract from ADT
                    return id;
                }

                @Override
                public int webviewColumn() {
                    // TODO: extract from ADT
                    return 1;
                }
                
            };
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

        private ICommandOutput handleJSONResponse(IConstructor response) {
            IValue data = response.get("val");
            IWithKeywordParameters<? extends IConstructor> kws = response.asWithKeywordParameters();

            IValue dtf = kws.getParameter("dateTimeFormat");
            IValue dai = kws.getParameter("dateTimeAsInt");
            
            JsonValueWriter writer = new JsonValueWriter()
                .setCalendarFormat(dtf != null ? ((IString) dtf).getValue() : "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
                .setDatesAsInt(dai != null ? ((IBool) dai).getValue() : true);

            return new ICommandOutput() {
                @Override
                public IOutputPrinter asPlain() {
                    return new IOutputPrinter() {
                        @Override
                        public void write(PrintWriter target, boolean unicodeSupported) {
                            try (var json = new JsonWriter(target)) {
                                writer.write(json, data);
                            }
                            catch (IOException ex) {
                                target.println("Unexpected IO exception: " + ex);
                            }
                        }
                        @Override
                        public String mimeType() {
                            return "application/json";
                        }
                    };
                }
                
            };
        }

        private ICommandOutput handleFileResponse(IConstructor response)
            throws UnsupportedEncodingException {
            IString fileMimetype = (IString) response.get("mimeType");
            ISourceLocation file = (ISourceLocation) response.get("file");
            return new ISourceLocationCommandOutput() {
                @Override
                public ISourceLocation asLocation() {
                    return file;
                }
                @Override
                public String locationMimeType() {
                    return fileMimetype.getValue();
                }

                @Override
                public IOutputPrinter asPlain() {
                    return new AsciiStringOutputPrinter("Direct file returned, REPL doesn't support file results", MimeTypes.PLAIN_TEXT);
                }
                
            };
        }

        private ICommandOutput handlePlainTextResponse(IConstructor response)
            throws UnsupportedEncodingException {
            String content = ((IString) response.get("content")).getValue();
            String contentMimetype = ((IString) response.get("mimeType")).getValue();
            return () -> new AsciiStringOutputPrinter(content, contentMimetype);
        }

        @Override
        public boolean supportsCompletion() {
            return true;
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
                        stdout.flush();
                        stderr.flush();
                        eval.revertToDefaultWriters();
                    }
                }
            }
            else {
                throw RuntimeExceptionFactory.illegalArgument(f, "term repl only works with interpreter for now");
            }
        }

        @Override
        public Map<String, String> completeFragment(String line, String word) {
            IMap result = (IMap)call(completor, new Type[] { tf.stringType(), tf.stringType() },
                new IValue[] { vf.string(line), vf.string(word) }); 

            var resultMap = new HashMap<String, String>();
            var it = result.entryIterator();
            while (it.hasNext()) {
                var c = it.next();
                resultMap.put(((IString)c.getKey()).getValue(), ((IString)c.getValue()).getValue());
            }
            return resultMap;
        }
    }
}
