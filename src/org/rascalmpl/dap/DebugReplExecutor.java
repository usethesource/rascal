package org.rascalmpl.dap;

import java.util.Objects;
import java.util.function.Function;

import org.rascalmpl.debug.DebugHandler;
import org.rascalmpl.debug.AbstractInterpreterEventTrigger;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.IRascalResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.exceptions.RascalStackOverflowError;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.repl.rascal.RascalValuePrinter;
import org.rascalmpl.values.functions.IFunction;
import io.usethesource.vallang.ISourceLocation;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.IValue;

/**
 * Helper that encapsulates REPL-like evaluation semantics for use inside the debugger.
 * It performs the evaluator environment switching and converts results/errors into ICommandOutput
 * using the shared RascalValuePrinter.
 */
public class DebugReplExecutor {

    public static final ISourceLocation DEBUGGER_PROMPT_LOCATION = URIUtil.rootLocation("debugger");

    private final Evaluator evaluator;
    private final DebugHandler debugHandler;
    // no-op for now; retained constructor parameter omitted from field
    private final RascalValuePrinter printer;

    public static class EvalResult {
        public final ICommandOutput output;
        public final Result<IValue> result;

        public EvalResult(ICommandOutput output, Result<IValue> result) {
            this.output = output;
            this.result = result;
        }
    }

    public DebugReplExecutor(Evaluator evaluator, DebugHandler debugHandler) {
        this.evaluator = Objects.requireNonNull(evaluator);
        this.debugHandler = debugHandler;

        // create a printer that knows how to lift provider functions by delegating into the evaluator lock
        this.printer = new RascalValuePrinter() {
            @Override
            protected Function<IValue, IValue> liftProviderFunction(IFunction func) {
                return v -> {
                    Objects.requireNonNull(evaluator, "Not initialized yet");
                    synchronized (evaluator) {
                        return func.call(v);
                    }
                };
            }
        };
    }

    /**
     * Evaluate the given command in the provided environment and return both a printable
     * ICommandOutput and the raw Result (if any). ParseError is propagated to the caller
     * (as in the normal REPL behavior) so callers can decorate/format parse errors differently.
     */
    public EvalResult evaluate(String command, Environment evalEnv) throws ParseError, InterruptedException {
        String expr = command.endsWith(";") ? command : command + ";";
        synchronized (evaluator) {
            // Save old state
            AbstractInterpreterEventTrigger oldTrigger = evaluator.getEventTrigger();
            Environment oldEnvironment = evaluator.getCurrentEnvt();
            try {
                // disable suspend triggers while evaluating expressions from the debugger
                if (debugHandler != null) {
                    evaluator.removeSuspendTriggerListener(debugHandler);
                }
                evaluator.setEventTrigger(AbstractInterpreterEventTrigger.newNullEventTrigger());
                evaluator.setCurrentEnvt(evalEnv);

                Result<IValue> result = evaluator.eval(evaluator.getMonitor(), expr, DEBUGGER_PROMPT_LOCATION);
                ICommandOutput out = printer.outputResult((IRascalResult) result);
                return new EvalResult(out, result);
            }
            catch (InterruptException ex) {
                return new EvalResult(printer.outputError((w, sw, u) -> {
                    w.println((u ? "»» " : ">> ") + "Interrupted");
                    ex.getRascalStackTrace().prettyPrintedString(w, sw);
                }), null);
            }
            catch (RascalStackOverflowError e) {
                return new EvalResult(printer.outputError((w, sw, _u) -> {
                    // use throwMessage from ReadEvalPrintDialogMessages style, but keep simple here
                    w.println(e.makeThrow().toString());
                }), null);
            }
            catch (StaticError e) {
                return new EvalResult(printer.outputError((w, sw, _u) -> {
                    w.println(String.format("%s: %s", e.getLocation(), e.getMessage()));
                }), null);
            }
            catch (Throw e) {
                return new EvalResult(printer.outputError((w, sw, _u) -> {
                    w.println(e.toString());
                }), null);
            }
            catch (QuitException q) {
                // signal caller the REPL should stop -- propagate as interrupted
                throw new InterruptedException("Quit requested");
            }
            catch (ParseError pe) {
                // propagate parse errors to allow caller to format them like the REPL
                throw pe;
            }
            catch (Throwable e) {
                return new EvalResult(printer.outputError((w, sw, _u) -> {
                    w.println(e.toString());
                }), null);
            }
            finally {
                // Restore old state
                evaluator.setCurrentEnvt(oldEnvironment);
                evaluator.setEventTrigger(oldTrigger);
                if (debugHandler != null) {
                    evaluator.addSuspendTriggerListener(debugHandler);
                }
            }
        }
    }
}
