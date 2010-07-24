package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
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
	public java.util.List<String> getVariables();

	/**
	 * Compute an IValue for this pattern, which is only possible if getVariables returns an empty lost (i.e. its a constant pattern)
	 * @param env
	 * @return an ivalue representing this pattern
	 */
	public IValue toIValue(Environment env);

	public AbstractAST getAST();
}