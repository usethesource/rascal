package org.meta_environment.rascal.interpreter.exceptions;

import org.eclipse.imp.pdb.facts.ISourceRange;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.meta_environment.rascal.ast.AbstractAST;

/**
 * This class is for representing user exceptions in Rascal. I.e. not to be
 * thrown by the implementation of Rascal, but by Rascal code. Embedded Java
 * code that throws exceptions can also use this exception class.
 * 
 * Warning: this is not a thread safe implementation. The idea however is to not
 * create a stack trace every time a Return exception is needed.
 * 
 */
public class RascalException extends RuntimeException {
	private static final long serialVersionUID = -7290501865940548332L;

	private IValue exception;
	private ISourceRange range;
	private String path;
	
	public RascalException(IValue value) {
		this.exception = value;
		this.range = null;
		this.path = null;
	};
	
	public RascalException(IValue value, AbstractAST node) {
		this.exception = value;
		range = node.getSourceRange();
		path = node.getSourcePath();
	};

	public RascalException(String message) {
		this(ValueFactory.getInstance().string(message));
	}

	public RascalException(String message, AbstractAST node) {
		this(ValueFactory.getInstance().string(message), node);
	}
	
	public RascalException(String message, Throwable cause) {
		super(message, cause);
		this.exception = ValueFactory.getInstance().string(message);
		range = null;
		path = null;
	}

	public IValue getException() {
		return exception;
	}

	/*
	 * @Override public String getMessage() { return exception.toString(); }
	 */

	@Override
	public String getMessage() {
		//String message = super.getMessage();
		String message = exception.toString();

		if (hasRange()) {
			if (range.getStartLine() != range.getEndLine()) {
				message += " from line " + range.getStartLine() + ", column "
						+ range.getStartColumn() + " to line "
						+ range.getEndLine() + "," + " column "
						+ range.getEndColumn();
			} else {
				message += " at line " + range.getStartLine() + ", column "
						+ range.getStartColumn() + " to "
						+ range.getEndColumn();
			}
		}

		if (hasPath()) {
			message += " in " + path;
		}

		return message;
	}

	public boolean hasRange() {
		return range != null;
	}

	public boolean hasPath() {
		return path != null && !path.equals("-");

	}
	
	public boolean hasCause() {
		// TODO Auto-generated method stub
		return getCause() != null;
	}

}
