package org.rascalmpl.repl;

import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.parseErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.staticErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwableMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
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
import org.rascalmpl.repl.completers.RascalModuleCompletion;
import org.rascalmpl.repl.completers.RascalKeywordCompletion;
import org.rascalmpl.repl.completers.RascalLocationCompletion;
import org.rascalmpl.repl.jline3.RascalLineParser;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.type.Type;

public class RascalReplServices implements IREPLService {
    private final Function<Terminal, Evaluator> buildEvaluator;
    private Evaluator eval;
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
    public boolean isInputComplete(String input) {
        throw new UnsupportedOperationException("Unimplemented method 'isInputComplete'");
    }


    @Override
    public void handleInput(String input, Map<String, IOutputPrinter> output, Map<String, String> metadata)
        throws InterruptedException {
        synchronized(eval) {
            Objects.requireNonNull(eval, "Not initialized yet");
            try {
                Result<IValue> value;
                    value = eval.eval(eval.getMonitor(), input, URIUtil.rootLocation("prompt"));
                outputResult(output, value);
            }
            catch (InterruptException ex) {
                reportError(output, (w, sw) -> {
                    w.println("Interrupted");
                    ex.getRascalStackTrace().prettyPrintedString(w, sw);
                });
            }
            catch (ParseError pe) {
                reportError(output, (w, sw) -> {
                    parseErrorMessage(w, input, "prompt", pe, sw);
                });
            }
            catch (StaticError e) {
                reportError(output, (w, sw) -> {
                    staticErrorMessage(w, e, sw);
                });
            }
            catch (Throw e) {
                reportError(output, (w, sw) -> {
                    throwMessage(w,e, sw);
                });
            }
            catch (QuitException q) {
                reportError(output, (w, sw) -> {
                    w.println("Quiting REPL");
                });
                throw new EndOfFileException("Quiting REPL");
            }
            catch (Throwable e) {
                reportError(output, (w, sw) -> {
                    throwableMessage(w, e, eval.getStackTrace(), sw);
                });
            }
        }
    }

    private void outputResult(Map<String, IOutputPrinter> output, IRascalResult result) {
        if (result == null || result.getValue() == null) {
            output.put(MIME_PLAIN, new StringOutputPrinter("ok", newline));
            return;
        }
        IValue value = result.getValue();
        Type type = result.getStaticType();

        if (type.isSubtypeOf(RascalValueFactory.Content) && !type.isBottom()) {
            output.put(MIME_PLAIN, new StringOutputPrinter("Serving content", newline));
            // TODO: serve content!
            return;
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

        output.put(MIME_PLAIN, new ExceptionPrinter(typePrefixed, plainIndentedPrinter));
        output.put(MIME_ANSI, new ExceptionPrinter(typePrefixed, ansiIndentedPrinter));

    }

    private static void reportError(Map<String, IOutputPrinter> output, ThrowingWriter writer) {
        output.put(MIME_PLAIN, new ExceptionPrinter(writer, plainIndentedPrinter));
        output.put(MIME_ANSI, new ExceptionPrinter(writer, ansiIndentedPrinter));
    }

    @FunctionalInterface
    private static interface ThrowingWriter {
        void write(PrintWriter writer, StandardTextWriter prettyPrinter) throws IOException;
    }

    private static class ExceptionPrinter implements IOutputPrinter {
        private final ThrowingWriter internalWriter;
        private final StandardTextWriter prettyPrinter;

        public ExceptionPrinter(ThrowingWriter internalWriter, StandardTextWriter prettyPrinter) {
            this.internalWriter = internalWriter;
            this.prettyPrinter = prettyPrinter;
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

    private static class StringOutputPrinter implements IOutputPrinter {
        private final String value;
        private final String newline;

        public StringOutputPrinter(String value, String newline) {
            this.value = value;
            this.newline = newline;
        }

        @Override
        public void write(PrintWriter target) {
            target.println(value);
        }

        @Override
        public Reader asReader() {
            return new StringReader(value + newline);
        }
    }

    @Override
    public void handleInterrupt() throws InterruptedException {
        eval.interrupt();
    }

    @Override
    public String prompt(boolean ansiSupported, boolean unicodeSupported) {
        if (ansiSupported) {
            return Ansi.ansi().reset().bold() + "rascal>" + Ansi.ansi().reset();
        }
        return "rascal>";
    }

    @Override
    public String parseErrorPrompt(boolean ansiSupported, boolean unicodeSupported) {
        String errorPrompt = (unicodeSupported ? "│" : "|") + "%N %P>";
        if (ansiSupported) {
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
        // TODO figure out why this function is called?
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
