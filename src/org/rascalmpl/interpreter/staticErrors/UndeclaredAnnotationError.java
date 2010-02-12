package org.rascalmpl.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;


public class UndeclaredAnnotationError extends StaticError {
	private static final long serialVersionUID = -7406667412199993333L;
	
	/**
	 * Note that for constructors this is a static error, but there is also
	 * a dynamic exception related to non-existence of an annotation.
	 */
	public UndeclaredAnnotationError(String name, Type on, AbstractAST node) {
		super("Undeclared annotation: " + name + " on " + on, node);
	}
	
}
