package org.meta_environment.rascal.interpreter;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.env.Environment;

/**
 * The MatchPattern  interface describes the standard way of applying a pattern to a subject:
 * 1. Create the MatchPattern
 * 2. Initialize the pattern with the subject to be matched.
 * 3. While hasNext() returns true: call match() do perform the actual pattern match.
 *
 */
public interface MatchPattern {
	/**
	 * @param env: the module scope
	 * @return the Rascal type of this MatchPattern
	 */
	public Type getType(Environment env);
	
	/**
	 * @param subject to be matched
	 * @param env the current evaluator
	 */
	public void initMatch(IValue subject, Environment env);
	
	/**
	 * @param subject
	 * @param env
	 */
	public boolean mayMatch(IValue subject, Environment env);
	
	/**
	 * @return true if this MatchPattern has more matches available
	 */
	public boolean hasNext();
	
	/**
	 * @return true if the MatchPattern matches the subject
	 */
	public boolean next();
	
	/**
	 * @return the variables that are bound in the pattern
	 */
	public java.util.List<String> getVariables();
}