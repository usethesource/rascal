package org.rascalmpl.repl;

import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.parseErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.staticErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwableMessage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.rascalmpl.exceptions.RascalStackOverflowError;
import org.rascalmpl.exceptions.StackTrace;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.result.IRascalResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IValue;

public abstract class RascalInterpreterREPL extends BaseRascalREPL {
    protected Evaluator eval;
    private boolean measureCommandTime;

    public RascalInterpreterREPL(boolean prettyPrompt, boolean allowColors, File persistentHistory)
                    throws IOException, URISyntaxException {
        super(prettyPrompt, allowColors);
    }
    
    public void cleanEnvironment() {
        eval.getCurrentModuleEnvironment().reset();
    }
    
    public RascalInterpreterREPL() throws IOException, URISyntaxException{
        super(true, true);
    }
    
    @Override
    protected Function<IValue, IValue> liftProviderFunction(IFunction callback) {
        return (t) -> {
            synchronized(eval) {
                return callback.call(t);
            }
        };
    }
    
    public void setMeasureCommandTime(boolean measureCommandTime) {
        this.measureCommandTime = measureCommandTime;
    }

    public boolean getMeasureCommandTime() {
        return measureCommandTime;
    }

    @Override
    public void initialize(InputStream input, OutputStream stdout, OutputStream stderr, IDEServices ideServices) {
        eval = constructEvaluator(input, stdout, stderr, ideServices);
    }

    protected abstract Evaluator constructEvaluator(InputStream input, OutputStream stdout, OutputStream stderr, IDEServices ideServices);

    @Override
    public PrintWriter getErrorWriter() {
        return eval.getErrorPrinter();
    }

    @Override
    public InputStream getInput() {
        return eval.getInput();
    }
    
    @Override
    public PrintWriter getOutputWriter() {
        return eval.getOutPrinter();
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
            trace.prettyPrintedString(err, indentedPrettyPrinter);
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
                value = eval.eval(eval.getMonitor(), statement, URIUtil.rootLocation("prompt"));
                duration = tm.duration();
            }
            if (measureCommandTime) {
                eval.getErrorPrinter().println("\nTime: " + duration + "ms");
            }
            return value;
        }
        catch (InterruptException ie) {
            eval.getErrorPrinter().println("Interrupted");
            try {
                ie.getRascalStackTrace().prettyPrintedString(eval.getErrorPrinter(), indentedPrettyPrinter);
            }
            catch (IOException e) {
            }
            return null;
        }
        catch (ParseError pe) {
            parseErrorMessage(eval.getErrorPrinter(), lastLine, "prompt", pe, indentedPrettyPrinter);
            return null;
        }
        catch (RascalStackOverflowError e) {
            throwMessage(eval.getErrorPrinter(), e.makeThrow(), indentedPrettyPrinter);
            return null;
        }
        catch (StaticError e) {
            staticErrorMessage(eval.getErrorPrinter(),e, indentedPrettyPrinter);
            return null;
        }
        catch (Throw e) {
            throwMessage(eval.getErrorPrinter(),e, indentedPrettyPrinter);
            return null;
        }
        catch (QuitException q) {
            eval.getErrorPrinter().println("Quiting REPL");
            throw new InterruptedException();
        }
        catch (Throwable e) {
            throwableMessage(eval.getErrorPrinter(), e, eval.getStackTrace(), indentedPrettyPrinter);
            return null;
        }
    }

    @Override
    public boolean isStatementComplete(String command) {
        try {
            eval.parseCommand(eval.getMonitor(), command, URIUtil.rootLocation("prompt"));
        }
        catch (ParseError pe) {
            String[] commandLines = command.split("\n");
            int lastLine = commandLines.length;
            int lastColumn = commandLines[lastLine - 1].length();

            if (pe.getEndLine() == lastLine && lastColumn <= pe.getEndColumn()) { 
                return false;
            }
        }
        catch (Throwable e) {
            getErrorWriter().println("Unexpected failure during parsing of current command: ");
            getErrorWriter().println(e.getMessage());
            e.printStackTrace(getErrorWriter());
            return false;
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

    private static final SortedSet<String> commandLineOptions = new TreeSet<>();
    static {
        commandLineOptions.add(Configuration.GENERATOR_PROFILING_PROPERTY.substring("rascal.".length()));
        commandLineOptions.add(Configuration.PROFILING_PROPERTY.substring("rascal.".length()));
        commandLineOptions.add(Configuration.ERRORS_PROPERTY.substring("rascal.".length()));
        commandLineOptions.add(Configuration.TRACING_PROPERTY.substring("rascal.".length()));
    }
    @Override
    protected SortedSet<String> getCommandLineOptions() {
        return commandLineOptions;
    }
}
