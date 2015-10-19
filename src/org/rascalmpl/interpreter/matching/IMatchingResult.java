/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.HashMap;
import java.util.List;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

/**
 * The IBacktrackingExpression  interface describes the standard way of applying a pattern to a subject:
 * 1. Create the Expression
 * 2. Initialize the pattern with the subject to be matched.
 * 3. While hasNext() returns true: call match() do perform the actual pattern match.
 *
 */
public interface IMatchingResult extends IBooleanResult {
	/**
	 * @param patternVars TODO
	 * @param env: the module scope
	 * @param patternVars: the variable introduced earlier in the pattern.
	 * @return the Rascal type of this MatchPattern
	 */
	public Type getType(Environment env, HashMap<String,IVarPattern> patternVars);
	
	
	/**
	 * @param subject to be matched is stored in the matching result, which initialized the state for lazy backtracking behavior.
	 */
	public void initMatch(Result<IValue> subject);
	
	/**
	 * returns false if the static type of the pattern is incomparable with the given subject type.
	 * @param subject
	 * @param env
	 */
	public boolean mayMatch(Type subjectType, Environment env);
	
	/**
	 * @return the variables that are bound in the pattern
	 */
	public List<IVarPattern> getVariables();

	public AbstractAST getAST();
	
	public IValue toIValue();
	
	/**
	 * For pushing type information of outer patterns down to the children once
	 * a match has succeeded.
	 * 
	 * This method should be removed once we have a type checker.
	 *  
	 * Since some patterns can give us specific type contexts, an inferred type
   * for a child pattern variable should get this specific type. A type
   * checker would have inferred that already, but now we just push
   * the information down to all children of the constructor.
	 */
	public void updateType(Type type);
}
