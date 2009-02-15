package org.meta_environment.rascal.interpreter.errors;

import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISourceRange;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
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
	private static final long serialVersionUID = -7290501865940548332L;

	private IValue exception;
	private ISourceRange range;
	private String path;
	
	public Error(IValue value) {
		this.exception = value;
		this.range = null;
		this.path = null;
	};
	
	public Error(IValue value, AbstractAST node) {
		this.exception = value;
		range = node.getSourceRange();
		path = node.getSourcePath();
	};
	
	private static INode makeNode(String errorCons, String message){
		//System.err.println("makeNode(" + exceptionCons + ", " + message + ")");
		ValueFactory VF = ValueFactory.getInstance();
		TypeFactory TF = TypeFactory.getInstance();
		Type adt = TF.lookupAbstractDataType("Error");
		List<Type> types = TF.lookupConstructor(adt, errorCons);
		if(types.size() > 0){
			// The Error ADT is defined
			Type Cons = types.get(0);
			return VF.constructor(Cons, VF.string(message));
		} else {
			// The Error ADT is not defined, return just a node
			return VF.node(errorCons, VF.string(message));
		}
	}

	public Error(String exceptionCons, String message) {
		this(makeNode(exceptionCons, message));
	}

	public Error(String message, String exceptionCons, AbstractAST node) {
		this(makeNode(exceptionCons, message));
	}
	
	public Error(String message, Throwable cause) {
		super(message, cause);
		this.exception = makeNode("RascalException", message);
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
