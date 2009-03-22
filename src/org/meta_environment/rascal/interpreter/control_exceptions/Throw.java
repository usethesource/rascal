package org.meta_environment.rascal.interpreter.control_exceptions;

import java.net.URL;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.ast.AbstractAST;

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
	private ISourceLocation loc;
	
	// TODO add support for Rascal stack trace, which will make these errors locatable
	// it is not the idea that these exceptions get references to AbstractAST's!!
	public Throw(IValue value, ISourceLocation loc) {
		super(value.toString());
		this.exception = value;
		this.loc = loc;
		if (loc == null) {
			System.err.println("TODO: provide error location");
		}
	};
	
	public Throw(IValue value, AbstractAST ast) {
		this(value, ast != null ? ast.getLocation() : null);
	}
	
	public IValue getException() {
		return exception;
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
		else {
			// TODO remove once all errors have locations
			return super.getMessage();
		}
	}
	
}
