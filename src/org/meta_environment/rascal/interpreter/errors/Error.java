package org.meta_environment.rascal.interpreter.errors;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.ValueFactoryFactory;
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
public class Error extends RuntimeException {
	private static final String ERROR_DATA_TYPE_NAME = "Error";

	private static final long serialVersionUID = -7290501865940548332L;

	private final IValue exception;
	private  ISourceLocation loc;
	
	public Error(IValue value) {
		this.exception = value;
		this.loc = null;
	};
	
	public Error(IValue value, AbstractAST node) {
		this.exception = value;
		if(node != null){
			loc = node.getLocation();
		} else {
			loc = null;
		}
	};
	
	public void setAst(AbstractAST node){
		// Set only if not previously defined.
		if(loc != null){
			loc = node.getLocation();
		}
	}
	
	private static INode makeNode(String errorCons, String message){
		IValueFactory VF = ValueFactoryFactory.getValueFactory();
		TypeFactory TF = TypeFactory.getInstance();
		TypeStore hiddenStore = new TypeStore(); // TODO this may not work since other will not be able to see the declaration
		Type adt = TF.abstractDataType(hiddenStore, ERROR_DATA_TYPE_NAME);
		Type type = TF.constructor(hiddenStore, adt, errorCons, TF.stringType());
		if(message == null) {
			message = "null";
		}
		
		return (INode) type.make(VF, VF.string(message));
	}

	public Error(String errorCons, String message) {
		this(makeNode(errorCons, message), null);
	}

	public Error(String errorCons, String message, AbstractAST node) {
		this(makeNode(errorCons, message), node);
	}
	
	public Error(String message, Throwable cause) {
		super(message, cause);
		this.exception = makeNode(ERROR_DATA_TYPE_NAME, message);
		loc = null;
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
			if (loc.getStartLine() != loc.getEndLine()) {
				message += " from line " + loc.getStartLine() + ", column "
						+ loc.getStartColumn() + " to line "
						+ loc.getEndLine() + "," + " column "
						+ loc.getEndColumn();
			} else {
				message += " at line " + loc.getStartLine() + ", column "
						+ loc.getStartColumn() + " to "
						+ loc.getEndColumn();
			}
		}

		if (hasPath()) {
			message += " in " + loc.getURL().getPath();
		}

		return message;
	}

	public boolean hasRange() {
		return loc != null;
	}

	public boolean hasPath() {
		return loc != null && !loc.getURL().getPath().equals("-");

	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
