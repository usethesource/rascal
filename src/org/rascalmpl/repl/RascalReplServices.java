package org.rascalmpl.repl;

import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.parseErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.staticErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwableMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;

import org.jline.jansi.Ansi;
import org.jline.reader.Completer;
import org.jline.reader.EndOfFileException;
import org.jline.reader.Parser;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.result.IRascalResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.repl.completers.RascalCommandCompletion;
import org.rascalmpl.repl.completers.RascalIdentifierCompletion;
import org.rascalmpl.repl.completers.RascalKeywordCompletion;
import org.rascalmpl.repl.completers.RascalLocationCompletion;
import org.rascalmpl.repl.completers.RascalModuleCompletion;
import org.rascalmpl.repl.http.REPLContentServer;
import org.rascalmpl.repl.http.REPLContentServerManager;
import org.rascalmpl.repl.jline3.RascalLineParser;
import org.rascalmpl.repl.output.IAnsiCommandOutput;
import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.repl.output.IErrorCommandOutput;
import org.rascalmpl.repl.output.IHtmlCommandOutput;
import org.rascalmpl.repl.output.IOutputPrinter;
import org.rascalmpl.repl.output.IWebContentOutput;
import org.rascalmpl.repl.output.MimeTypes;
import org.rascalmpl.repl.output.impl.StringOutputPrinter;
import org.rascalmpl.repl.streams.ItalicErrorWriter;
import org.rascalmpl.repl.streams.LimitedLineWriter;
import org.rascalmpl.repl.streams.LimitedWriter;
import org.rascalmpl.repl.streams.RedErrorWriter;
import org.rascalmpl.repl.streams.ReplTextWriter;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.type.Type;

public class RascalReplServices implements IREPLService {
    private final Function<Terminal, Evaluator> buildEvaluator;
    private Evaluator eval;
    private final REPLContentServerManager contentManager = new REPLContentServerManager();
    private static final StandardTextWriter ansiIndentedPrinter = new ReplTextWriter(true);
    private static final StandardTextWriter plainIndentedPrinter = new StandardTextWriter(true);
    private final static int LINE_LIMIT = 200;
    private final static int CHAR_LIMIT = LINE_LIMIT * 20;
    private String newline = System.lineSeparator();
    


    public RascalReplServices(Function<Terminal, Evaluator> buildEvaluator) {
        super();
        this.buildEvaluator = buildEvaluator;
    }

    @Override
    public void connect(Terminal term) {
        if (eval != null) {
            throw new IllegalStateException("REPL is already initialized");
        }
        newline = term.getStringCapability(Capability.newline);
        if (newline == null) {
            newline = System.lineSeparator();
        }
        this.eval = buildEvaluator.apply(term);
    }

    public static PrintWriter generateErrorStream(Terminal tm, Writer out) {
        // previously we would alway write errors to System.err, but that tends to mess up terminals
        // and also our own error print
        // so now we try to not write to System.err
        if (supportsColors(tm)) {
            return new PrintWriter(new RedErrorWriter(out), true);
        }
        if (supportsItalic(tm)) {
            return new PrintWriter(new ItalicErrorWriter(out), true);
        }
        return new PrintWriter(System.err, true);
    
    }

    private static boolean supportsColors(Terminal tm) {
        Integer cols = tm.getNumericCapability(Capability.max_colors);
        return cols != null && cols >= 8;
    }

    private static boolean supportsItalic(Terminal tm) {
        String ital = tm.getStringCapability(Capability.enter_italics_mode);
        return ital != null && !ital.equals("");
    }


    private static final ISourceLocation PROMPT_LOCATION = URIUtil.rootLocation("prompt");

    @Override
    public Parser inputParser() {
        return new RascalLineParser(prompt -> {
            synchronized(eval) {
                return eval.parseCommand(new NullRascalMonitor(), prompt, PROMPT_LOCATION);
            }
        });
    }

    @Override
    public ICommandOutput handleInput(String input) throws InterruptedException {
        synchronized(eval) {
            Objects.requireNonNull(eval, "Not initialized yet");
            try {
                Result<IValue> value = eval.eval(eval.getMonitor(), input, URIUtil.rootLocation("prompt"));
                return outputResult(value);
            }
            catch (InterruptException ex) {
                return reportError((w, sw) -> {
                    w.println("Interrupted");
                    ex.getRascalStackTrace().prettyPrintedString(w, sw);
                });
            }
            catch (ParseError pe) {
                return reportError((w, sw) -> {
                    parseErrorMessage(w, input, "prompt", pe, sw);
                });
            }
            catch (StaticError e) {
                return reportError((w, sw) -> {
                    staticErrorMessage(w, e, sw);
                });
            }
            catch (Throw e) {
                return reportError((w, sw) -> {
                    throwMessage(w,e, sw);
                });
            }
            catch (QuitException q) {
                throw new EndOfFileException("Quiting REPL");
                /* 
                return reportError((w, sw) -> {
                    w.println("Quiting REPL");
                });
                */
            }
            catch (Throwable e) {
                return reportError((w, sw) -> {
                    throwableMessage(w, e, eval.getStackTrace(), sw);
                });
            }
        }
    }

    private ICommandOutput outputResult(IRascalResult result) {
        if (result == null || result.getValue() == null) {
            return () -> new StringOutputPrinter("ok", newline, MimeTypes.PLAIN_TEXT);
        }
        IValue value = result.getValue();
        Type type = result.getStaticType();

        if (type.isSubtypeOf(RascalValueFactory.Content) && !type.isBottom()) {
            return serveContent((IConstructor)value);
        }

        ThrowingWriter resultWriter;
        if (type.isAbstractData() && type.isStrictSubtypeOf(RascalValueFactory.Tree) && !type.isBottom()) {
            resultWriter = (w, sw) -> {
                w.write("(" + type.toString() +") `");
                TreeAdapter.yield((IConstructor)value, sw == ansiIndentedPrinter, w);
                w.write("`");
            };
        }
        else if (type.isString()) {
            resultWriter = (w, sw) -> {
                // TODO: do something special for the reader version of IString, when that is released
                // for now, we only support write

                try (Writer wrt = new LimitedWriter(new LimitedLineWriter(w, LINE_LIMIT), CHAR_LIMIT)) {
                    sw.write(value, wrt);
                }
                catch (/*IOLimitReachedException*/ RuntimeException e) {
                    // ignore since this is what we wanted
                    // if we catch IOLimitReachedException we get an IllegalArgument exception instead
                    // "Self-suppression not permitted"
                }
                w.println();
                w.println("---");
                try (Writer wrt = new LimitedWriter(new LimitedLineWriter(w, LINE_LIMIT), CHAR_LIMIT)) {
                    ((IString) value).write(wrt);
                }
                catch (/*IOLimitReachedException*/ RuntimeException e) {
                    // ignore since this is what we wanted
                    // if we catch IOLimitReachedException we get an IllegalArgument exception instead
                    // "Self-suppression not permitted"
                }
                w.println();
                w.print("---");
            };
        }
        else {
            resultWriter = (w, sw) -> {
                try (Writer wrt = new LimitedWriter(new LimitedLineWriter(w, LINE_LIMIT), CHAR_LIMIT)) {
                    sw.write(value, wrt);
                }
                catch (/*IOLimitReachedException*/ RuntimeException e) {
                    // ignore since this is what we wanted
                    // if we catch IOLimitReachedException we get an IllegalArgument exception instead
                    // "Self-suppression not permitted"
                }
            };
        }

        ThrowingWriter typePrefixed = (w, sw) -> {
            w.write(type.toString());
            w.write(": ");
            resultWriter.write(w, sw);
            w.println();
        };

        return new DoubleOutput(typePrefixed);
    }

    private static class DoubleOutput implements IAnsiCommandOutput {
        private ThrowingWriter writer;
        
        DoubleOutput(ThrowingWriter writer) {
            this.writer = writer;
        }

        @Override
        public IOutputPrinter asAnsi() {
            return new ParameterizedPrinterOutput(writer, ansiIndentedPrinter, MimeTypes.ANSI);
        }

        @Override
        public IOutputPrinter asPlain() {
            return new ParameterizedPrinterOutput(writer, plainIndentedPrinter, MimeTypes.PLAIN_TEXT);
        }
    }

    private Function<IValue, IValue> addEvalLock(IFunction func) {
        return a -> {
            synchronized(eval) {
                return func.call(a);
            }
        };
    }

    private ICommandOutput serveContent(IConstructor provider) {
        String id;
        Function<IValue, IValue> target;
        
        if (provider.has("id")) {
            id = ((IString) provider.get("id")).getValue();
            target = addEvalLock(((IFunction) provider.get("callback")));
        }
        else {
            id = "*static content*";
            target = (r) -> provider.get("response");
        }

        try {
            // this installs the provider such that subsequent requests are handled.
            REPLContentServer server = contentManager.addServer(id, target);

            // now we need some HTML to show
            
            IWithKeywordParameters<? extends IConstructor> kp = provider.asWithKeywordParameters();
            String title = kp.hasParameter("title") ? ((IString) kp.getParameter("title")).getValue() : id;
            int viewColumn = kp.hasParameter("viewColumn") ? ((IInteger)kp.getParameter("viewColumn")).intValue() : 1;
            URI serverUri = new URI("http", null, "localhost", server.getListeningPort(), "/", null, null);

            return new HostedWebContentOutput(id, serverUri, title, viewColumn);

        }
        catch (IOException e) {
            return reportError((w, sw) -> {
                w.println("Could not start webserver to render html content: ");
                w.println(e.getMessage());
            });
        }
        catch (URISyntaxException e) {
            return reportError((w, sw) -> {
                w.println("Could not start build the uri: ");
                w.println(e.getMessage());
            });
        }
    }

    private class HostedWebContentOutput implements IWebContentOutput, IHtmlCommandOutput {
        private final String id;
        private final URI uri;
        private final String title;
        private final int viewColumn;

        HostedWebContentOutput(String id, URI uri, String title, int viewColumn) {
            this.id = id;
            this.uri = uri;
            this.title = title;
            this.viewColumn = viewColumn;
        }

        @Override
        public IOutputPrinter asPlain() {
            return new StringOutputPrinter("Serving \'" + id + "\' at |" + uri + "|", newline, MimeTypes.PLAIN_TEXT);
        }

        @Override
        public IOutputPrinter asHtml() {
            return new StringOutputPrinter(
                "<iframe class=\"rascal-content-frame\" style=\"display: block; width: 100%; height: 100%; resize: both\" src=\""+ uri +"\"></iframe>", 
                newline, MimeTypes.HTML
            );
        }

        @Override
        public URI webUri() {
            return uri;
        }

        @Override
        public String webTitle() {
            return title;
        }

        @Override
        public int webviewColumn() {
            return viewColumn;
        }
    }

    private static IErrorCommandOutput reportError(ThrowingWriter writer) {
        return new IErrorCommandOutput() {
            @Override
            public ICommandOutput getError() {
                return new DoubleOutput(writer);
            }

            @Override
            public IOutputPrinter asPlain() {
                return new ParameterizedPrinterOutput(writer, plainIndentedPrinter, MimeTypes.PLAIN_TEXT);
            }
        };
    }

    @FunctionalInterface
    private static interface ThrowingWriter {
        void write(PrintWriter writer, StandardTextWriter prettyPrinter) throws IOException;
    }

    private static class ParameterizedPrinterOutput implements IOutputPrinter {
        private final ThrowingWriter internalWriter;
        private final StandardTextWriter prettyPrinter;
        private final String mimeType;

        public ParameterizedPrinterOutput(ThrowingWriter internalWriter, StandardTextWriter prettyPrinter, String mimeType) {
            this.internalWriter = internalWriter;
            this.prettyPrinter = prettyPrinter;
            this.mimeType = mimeType;
        }

        @Override
        public String mimeType() {
            return mimeType;
        }

        @Override
        public void write(PrintWriter target) {
            try {
                internalWriter.write(target, prettyPrinter);
            }
            catch (IOException e) {
                target.println("Internal failure: printing exception failed with:");
                target.println(e.toString());
                e.printStackTrace(target);
            }
        }
    }

    @Override
    public void handleInterrupt() throws InterruptedException {
        eval.interrupt();
    }

    @Override
    public String prompt(boolean ansiColorsSupported, boolean unicodeSupported) {
        if (ansiColorsSupported) {
            return Ansi.ansi().reset().bold() + "rascal>" + Ansi.ansi().reset();
        }
        return "rascal>";
    }

    @Override
    public String parseErrorPrompt(boolean ansiColorsSupported, boolean unicodeSupported) {
        String errorPrompt = (unicodeSupported ? "│" : "|") + "%N %P>";
        if (ansiColorsSupported) {
            return Ansi.ansi().reset().bold() + errorPrompt + Ansi.ansi().reset();
        }
        return errorPrompt;
    }


    @Override
    public PrintWriter errorWriter() {
        return eval.getStdErr();
    }

    @Override
    public PrintWriter outputWriter() {
        return eval.getStdOut();
    }

    @Override
    public void flush() {
        eval.getStdErr().flush();
        eval.getStdOut().flush();
    }

    private static final NavigableMap<String,String> commandLineOptions = new TreeMap<>();
    static {
        commandLineOptions.put(Configuration.GENERATOR_PROFILING_PROPERTY.substring("rascal.".length()), "enable sampling profiler for generator");
        commandLineOptions.put(Configuration.PROFILING_PROPERTY.substring("rascal.".length()), "enable sampling profiler" );
        commandLineOptions.put(Configuration.ERRORS_PROPERTY.substring("rascal.".length()), "print raw java errors");
        commandLineOptions.put(Configuration.TRACING_PROPERTY.substring("rascal.".length()), "trace all function calls (warning: a lot of output will be generated)");
    }

    @Override
    public boolean supportsCompletion() {
        return true;
    }

    @Override
    public List<Completer> completers() {
        var result = new ArrayList<Completer>();
        var moduleCompleter = new RascalModuleCompletion(m -> eval.getRascalResolver().listModuleEntries(m));
        var idCompleter = new RascalIdentifierCompletion((q, i) -> eval.completePartialIdentifier(q, i));
        result.add(new RascalCommandCompletion(
            commandLineOptions, 
            idCompleter::completePartialIdentifier, 
            (s, c) -> moduleCompleter.completeModuleNames(s, c, false)
        ));
        result.add(moduleCompleter);
        result.add(idCompleter);
        result.add(new RascalKeywordCompletion());
        result.add(new RascalLocationCompletion());
        return result;
    }
    
}
