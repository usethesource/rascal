/*******************************************************************************
 * Copyright (c) 2024 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.exceptions;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;

/**
 * This class captures the runtime state of the interpreter at the moment
 * we detect a java StackOverflowError. Since we can not use any stack depth
 * at that moment to create a real Throw exception, we only copy the deepest
 * environment and wrap it in here. Later on the level of the REPL, when the
 * stack is completely unrolled, we can convert the stack trace to a Rascal trace.
 */
public class RascalStackOverflowError extends RuntimeException {
    private static final long serialVersionUID = -3947588548271683963L;
    private final Environment deepestEnvironment;
    private final AbstractAST currentAST;
 
    public RascalStackOverflowError(AbstractAST current, Environment deepest) {
        this.deepestEnvironment = deepest;
        this.currentAST = current;
    }

    public Throw makeThrow() {
        StackTrace trace = new StackTrace();
        Environment env = deepestEnvironment;

        while (env != null) {
            trace.add(env.getLocation(), env.getName());
            env = env.getCallerScope();
        }

        return RuntimeExceptionFactory.stackOverflow(currentAST, trace);
    }
}
