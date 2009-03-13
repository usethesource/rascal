package org.meta_environment.rascal.interpreter.control_exceptions;

import org.eclipse.imp.pdb.facts.IValue;

/**
 * This class is for representing all run-time exceptions in Rascal.
 * These exceptions can be caught by Rascal code. 
 * These exceptions can be thrown by either the Rascal interpreter - 
 * in case of a run-time error, or by Rascal code - using the throw statement,
 * or by Java "built-in" methods, or by Java "embedded" methods.
 * <br>
 * Note that this class is <code>final</code> to emphasize the fact that
 * different kinds of Rascal exceptions need to be modeled by different kind
 * of exception values, not different kind of Java classes.
 */
public final class Throw extends RuntimeException {
	private static final long serialVersionUID = -7290501865940548332L;
	private final IValue exception;
	
	// TODO add support for Rascal stack trace, which will make these errors locatable
	// it is not the idea that these exceptions get references to AbstractAST's!!
	public Throw(IValue value) {
		this.exception = value;
	};
	
	public IValue getException() {
		return exception;
	}
	
}
