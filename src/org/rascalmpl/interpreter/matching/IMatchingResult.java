/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

/**
 * The IBacktrackingExpression  interface describes the standard way of applying a pattern to a subject:
 * 1. Create the Expression
 * 2. Initialize the pattern with the subject to be matched.
 * 3. While hasNext() returns true: call match() do perform the actual pattern match.
 *
 */
public interface IMatchingResult extends IBooleanResult {
	/**
	 * @param env: the module scope
	 * @return the Rascal type of this MatchPattern
	 */
	public Type getType(Environment env);
	
	/**
	 * @param ctx current evaluator
	 * @param subject to be matched is stored in the matching result, which initialized the state for lazy backtracking behavior.
	 */
	public void initMatch(IEvaluatorContext ctx, Result<IValue> subject);
	
	/**
	 * returns false if the static type of the pattern is incomparable with the given subject type.
	 * @param subject
	 * @param env
	 */
	public boolean mayMatch(Type subjectType, Environment env);
	
	/**
	 * @return the variables that are bound in the pattern
	 */
	public java.util.List<String> getVariables();

	public AbstractAST getAST();
	
	public IValue toIValue();
}