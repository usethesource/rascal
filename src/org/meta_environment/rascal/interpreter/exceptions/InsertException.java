package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.interpreter.env.EvalResult;

/**
 * Warning: this is not a thread safe implementation. The idea however is
 * to not create a stack trace every time a Return exception is needed.
 *
 */
/*package*/ class InsertException extends RuntimeException {
	private static final long serialVersionUID = -6601026099925601817L;
    private EvalResult fValue;
	
    private InsertException() { };
	
	private static class InstanceHolder {
		public static InsertException sInstance = new InsertException();
	}
	
	public static synchronized InsertException getInstance(EvalResult value) {
		return InstanceHolder.sInstance.setValue(value);
	}
	
	private synchronized InsertException setValue(EvalResult value) {
		fValue = value;
		return this;
	}
	
	public EvalResult getValue() {
		return fValue;
	}
}
