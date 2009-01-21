package org.meta_environment.rascal.interpreter.exceptions;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

/**
 * This class is for representing user exceptions in Rascal. I.e. not to be
 * thrown by the implementation of Rascal, but by Rascal code. Embedded Java
 * code that throws exceptions can also use this exception class.
 * 
 * Warning: this is not a thread safe implementation. The idea however is
 * to not create a stack trace every time a Return exception is needed.
 *
 */
public class RascalException extends RuntimeException {
	private static final long serialVersionUID = -7290501865940548332L;
	
	private IValue exception;
	
	public RascalException(IValueFactory factory, String message) {
		this(factory.string(message));
	}
	
    public RascalException(IValue value) {
    	this.exception = value;
    };
	
	public IValue getException() {
		return exception;
	}
	
	@Override
	public String getMessage() {
		return exception.toString();
	}
}
