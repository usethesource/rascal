package org.meta_environment.rascal.interpreter.staticErrors;

import java.net.URL;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.meta_environment.rascal.ast.AbstractAST;

/**
 * A static error represents all errors that are detected by the interpreter
 * in 'static type check' mode. These errors can NOT be caught in Rascal code.
 * In the future, they may be thrown by an actual type checker.
 */
public abstract class StaticError extends RuntimeException {
	private static final long serialVersionUID = 2730379050952564623L;
	private ISourceLocation loc;
	
	public StaticError(String message, ISourceLocation loc) {
		super(message);
		this.loc = loc;
		if (loc == null) {
			System.err.println("TODO: provide error location");
			printStackTrace();
		}
	}
	
	public StaticError(String message, AbstractAST ast) {
		this(message, ast != null ? ast.getLocation() : null);
	}
	
	public ISourceLocation getLocation() {
		return loc;
	}
	
	public void setLocation(ISourceLocation loc) {
		this.loc = loc;
	}

	@Override
	public String getMessage() {
		if (loc != null) {
			URL url = loc.getURL();
			
			return (url.getProtocol().equals("file") ? (url.getAuthority() + url.getPath()) : url) 
					+ ":" + loc.getBeginLine() 
					+ "," + loc.getBeginColumn() 
					+ ": " + super.getMessage();
		}
		
		// TODO remove once all errors have locations
		return super.getMessage();
	}
}
