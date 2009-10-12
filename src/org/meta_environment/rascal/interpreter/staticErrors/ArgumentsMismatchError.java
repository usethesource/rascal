package org.meta_environment.rascal.interpreter.staticErrors;

import java.util.Set;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;

public class ArgumentsMismatchError extends StaticError {
	private static final long serialVersionUID = -641438732779898646L;

	public ArgumentsMismatchError(String name,
			Set<AbstractFunction> candidates, Type[] argTypes,
			AbstractAST ast) {
		super(computeMessage(name, candidates, argTypes), ast);
	}

	private static String computeMessage(String name,
			Set<AbstractFunction> candidates, Type[] argTypes) {
		StringBuilder b = new StringBuilder();
		
		b.append("The called signature: " + name);
		b.append('(');
		argumentTypes(TypeFactory.getInstance().tupleType(argTypes), b);
		b.append(')');
		if (candidates.size() == 1) {
			b.append(",\ndoes not match the declared signature:");
		}
		else {
			b.append(",\ndoes not match any of the declared (overloaded) signatures:\n");
		}
		for (AbstractFunction c : candidates) {
			b.append('\t');
			b.append(c.getName());
			b.append('(');
			argumentTypes(c.getFunctionType().getArgumentTypes(), b);
			b.append(')');
			b.append('\n');
		}
		
		return b.toString();
		
	}

	private static void argumentTypes(Type argTypes, StringBuilder b) {
		int i = 0;
		for (Type arg : argTypes) {
			if (i != 0) b.append(',');
			b.append(arg);
		}
	}

}
