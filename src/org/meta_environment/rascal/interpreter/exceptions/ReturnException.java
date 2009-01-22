package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.interpreter.env.EvalResult;

/**
 * 
 * Warning: this is not a thread safe implementation. The idea however is
 * to not create a stack trace every time a Return exception is needed.
 *
 */
public class ReturnException extends RuntimeException {
	private static final long serialVersionUID = -6601026099925601817L;
    private EvalResult fValue;
	
    private ReturnException() { };
	
	private static class InstanceHolder {
		public static ReturnException sInstance = new ReturnException();
	}
	
	public static synchronized ReturnException getInstance(EvalResult value) {
		return InstanceHolder.sInstance.setValue(value);
	}
	
	private synchronized ReturnException setValue(EvalResult value) {
		fValue = value;
		return this;
	}
	
	public EvalResult getValue() {
		return fValue;
	}
}
