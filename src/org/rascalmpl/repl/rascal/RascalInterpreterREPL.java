package org.rascalmpl.repl.rascal;


import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.parseErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.staticErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwableMessage;

import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.jline.reader.EndOfFileException;
import org.jline.terminal.Terminal;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.StackTrace;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.ideservices.BasicIDEServices;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.repl.output.IWebContentOutput;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

/**
 * In most cases you might want to override the {@link #buildIDEService(PrintWriter, IRascalMonitor, Terminal)} and the {@link #buildEvaluator(Reader, PrintWriter, PrintWriter, IDEServices)} functions.
 */
public abstract class RascalInterpreterREPL implements IRascalLanguageProtocol {
    protected Evaluator eval;
    
    private final RascalValuePrinter printer;

    private static final ISourceLocation PROMPT_LOCATION = URIUtil.rootLocation("prompt");

    @Override
    public ITree parseCommand(String command) {
        Objects.requireNonNull(eval, "Not initialized yet");
        synchronized(eval) {
            return eval.parseCommand(new NullRascalMonitor(), command, PROMPT_LOCATION);
        }
    }

    public RascalInterpreterREPL() {
        this.printer = new RascalValuePrinter() {
            @Override
            protected Function<IValue, IValue> liftProviderFunction(IFunction func) {
                return v -> {
                    Objects.requireNonNull(eval, "Not initialized yet");
                    synchronized(eval) {
                        return func.call(v);
                    }
                };
            }
        };
    }

    /**
     * Build an IDE service, in most places you want to override this function to construct a specific one for the setting you are in.
     * @param err
     * @param monitor
     * @param term
     * @return
     */
    protected IDEServices buildIDEService(PrintWriter err, IRascalMonitor monitor, Terminal term) {
        return new BasicIDEServices(err, monitor, term);
    }

    /**
     * You might want to override this function for different cases of where we're building a REPL (possible only extend on the result of it)
     * @param input
     * @param stdout
     * @param stderr
     * @param monitor
     * @param services
     * @return
     */
    protected Evaluator buildEvaluator(Reader input, PrintWriter stdout, PrintWriter stderr, IDEServices services) {
        GlobalEnvironment heap = new GlobalEnvironment();
        ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
        IValueFactory vf = ValueFactoryFactory.getValueFactory();
        Evaluator evaluator = new Evaluator(vf, input, stderr, stdout, root, heap, services);
        evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
        return evaluator;
    }

    protected abstract void openWebContent(IWebContentOutput webContent);

    @Override
    public void initialize(Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor,
        Terminal term) {
        var services = buildIDEService(stderr, monitor, term);
        if (eval != null) {
            throw new IllegalStateException("Already initialized");
        }
        eval = buildEvaluator(input, stdout, stderr, services);
    }


    @Override
    public ICommandOutput handleInput(String command) throws InterruptedException {
        Objects.requireNonNull(eval, "Not initialized yet");
        synchronized(eval) {
            try {
                Result<IValue> value = eval.eval(eval.getMonitor(), command, PROMPT_LOCATION);
                var result = printer.outputResult(value);
                if (result instanceof IWebContentOutput) {
                    try {
                        openWebContent((IWebContentOutput)result);
                    } catch (Throwable _ignore) {}
                }
                return result;
            }
            catch (InterruptException ex) {
                return printer.outputError((w, sw, u) -> {
                    if (u) {
                        w.print("»» ");
                    }
                    w.println("Interrupted");
                    ex.getRascalStackTrace().prettyPrintedString(w, sw);
                });
            }
            catch (ParseError pe) {
                return printer.outputError((w, sw, _u) -> {
                    parseErrorMessage(w, command, "prompt", pe, sw);
                });
            }
            catch (StaticError e) {
                return printer.outputError((w, sw, _u) -> {
                    staticErrorMessage(w, e, sw);
                });
            }
            catch (Throw e) {
                return printer.outputError((w, sw, _u) -> {
                    throwMessage(w,e, sw);
                });
            }
            catch (QuitException q) {
                throw new EndOfFileException("Quiting REPL");
            }
            catch (Throwable e) {
                return printer.outputError((w, sw, _u) -> {
                    throwableMessage(w, e, eval.getStackTrace(), sw);
                });
            }
        }
    }

    @Override
    public void cancelRunningCommandRequested() {
        Objects.requireNonNull(eval, "Not initialized yet");
        eval.interrupt();
        eval.endAllJobs();
    }

    @Override
    public ICommandOutput stackTraceRequested() {
        StackTrace trace = eval.getStackTrace();
        return printer.prettyPrinted((w, sw, u) -> {
            w.println("Current stack trace:");
            trace.prettyPrintedString(w, sw);
            w.flush();
        });
    }

    @Override
    public List<String> lookupModules(String modulePrefix) {
        Objects.requireNonNull(eval, "Not initialized yet");
        return eval.getRascalResolver().listModuleEntries(modulePrefix);
    }

    @Override
    public Map<String, String> completePartialIdentifier(String qualifier, String partial) {
        Objects.requireNonNull(eval, "Not initialized yet");
        return eval.completePartialIdentifier(qualifier, partial);
    }

    @Override
    public Map<String, String> availableCommandLineOptions() {
        var commandLineOptions = new HashMap<String, String>();
        commandLineOptions.put(Configuration.GENERATOR_PROFILING_PROPERTY.substring("rascal.".length()), "enable sampling profiler for generator");
        commandLineOptions.put(Configuration.PROFILING_PROPERTY.substring("rascal.".length()), "enable sampling profiler" );
        commandLineOptions.put(Configuration.ERRORS_PROPERTY.substring("rascal.".length()), "print raw java errors");
        commandLineOptions.put(Configuration.TRACING_PROPERTY.substring("rascal.".length()), "trace all function calls (warning: a lot of output will be generated)");
        return commandLineOptions;
    }

    @Override
    public void flush() {
        eval.getStdErr().flush();
        eval.getStdOut().flush();
    }

}
