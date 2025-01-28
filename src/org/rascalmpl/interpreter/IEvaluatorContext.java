/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Emilie Balland - (CWI)
 *   * Anya Helene Bagge - (UiB)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter;

import java.io.PrintWriter;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;
import java.util.Stack;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.StackTrace;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.values.RascalFunctionValueFactory;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public interface IEvaluatorContext extends IRascalMonitor {
	/** for error messaging */
	public AbstractAST getCurrentAST();
	public void setCurrentAST(AbstractAST ast);
	public StackTrace getStackTrace();
	
	/** for standard IO */
	public PrintWriter getOutPrinter();
	public PrintWriter getErrorPrinter();

	public Reader getInput();
	
	/** for "internal use" */
	public IEvaluator<Result<IValue>> getEvaluator();
	public boolean isInterrupted();
	public void interrupt();
	public Environment getCurrentEnvt();
	public void setCurrentEnvt(Environment environment);
	
	public int getCallNesting();
	public void incCallNesting();
	public void decCallNesting();
	public boolean getCallTracing();

	public void pushEnv();
	public void pushEnv(String name);
	public void unwind(Environment old);
	
	public GlobalEnvironment getHeap();
	public Configuration getConfiguration();
	
	public boolean runTests(IRascalMonitor monitor);
	
	public IValueFactory getValueFactory();
	public RascalFunctionValueFactory getFunctionValueFactory();
	
	public void setAccumulators(Stack<Accumulator> accumulators);
	public Stack<Accumulator> getAccumulators();
	
	
	/** Given a (possibly empty) qualifier and a partial identifier, look in the current root environment if there are names defined that could match the partial names
	 * @return identifiers and their category (variable, function, etc) */
	public Map<String, String> completePartialIdentifier(String qualifier, String partialIdentifier);
}
