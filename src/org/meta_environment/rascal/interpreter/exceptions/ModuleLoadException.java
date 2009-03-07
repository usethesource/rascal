package org.meta_environment.rascal.interpreter.exceptions;


public class ModuleLoadException extends Exception {
	private static final long serialVersionUID = 4541540663859196204L;

	public ModuleLoadException(String message) {
		super(message);
	}
	
	public ModuleLoadException(String string, Throwable e) {
		super(string, e);
	}
}
