package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

public class InternalCompilerError extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InternalCompilerError(String msg) {
		super(msg);
	}
	
	public InternalCompilerError(String msg, Throwable cause) {
		super(msg, cause);
	}

}
