package org.rascalmpl.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class UndeclaredFunctionError extends StaticError {
	private static final long serialVersionUID = -3215674987633177L;
	
	public UndeclaredFunctionError(String name, Type[] argTypes, IEvaluatorContext ctx, AbstractAST node) {
		super("Undeclared function: " + toString(name, argTypes), node);
	}

	private static String toString(String name, Type[] argTypes) {
		StringBuilder b = new StringBuilder();
		b.append(name);
		b.append('(');
		int i = 0;
		for (Type arg : argTypes) {
			if (i > 0) b.append(',');
			b.append(arg);
		}
		b.append(')');
		return b.toString();
	}
}
