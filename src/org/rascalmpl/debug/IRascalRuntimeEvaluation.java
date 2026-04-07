package org.rascalmpl.debug;

import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.repl.output.ICommandOutput;
import io.usethesource.vallang.IValue;

/**
 * Contract for runtime evaluation support from the debugger/runtime.
 * Implementations must run the given expression in the provided Environment
 * and return a pair consisting of the raw evaluation result (when available)
 * and the REPL-style {@link ICommandOutput} that represents textual or rich
 * output produced by the evaluation.
 */
public interface IRascalRuntimeEvaluation {

    /**
     * Result of a runtime evaluation.
     * - <code>result</code> may be null if only textual output was produced or in error situations.
     * - <code>output</code> may be null when no textual/rich output was produced.
     *
     * Semantics: when <code>result == null</code>, the {@link ICommandOutput} should be
     * treated as the canonical output to display (an error or diagnostic). When
     * <code>result != null</code>, the adapter may prefer presenting the structured
     * value and avoid duplicating textual output.
     */
    public static final class EvalResult {
        public final Result<IValue> result;
        public final ICommandOutput output;

        public EvalResult(Result<IValue> result, ICommandOutput output) {
            this.result = result;
            this.output = output;
        }
    }

    /**
     * Evaluate expression in the given environment/frame.
     *
     * @param expression the Rascal expression to evaluate
     * @param env the Environment (stack frame) in which to evaluate, may be null for global scope
     * @return EvalResult containing optional raw result and/or printed output
     * @throws ParseError if the expression is syntactically incomplete/invalid (caller can format/present this)
     * @throws InterruptedException when evaluation was interrupted
     */
    EvalResult evaluate(String expression, Environment env) throws ParseError, InterruptedException;
}
