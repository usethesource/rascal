package org.meta_environment.rascal.interpreter.env;


import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.Names;

public class RascalFunction extends Lambda {

	public RascalFunction(Evaluator eval, FunctionDeclaration func, boolean varargs, Environment env) {
		super(eval, func.getSignature().getType().accept(TE),
				Names.name(func.getSignature().getName()),
				func.getSignature().getParameters().accept(TE),
				varargs, 
				func.getBody().getStatements(), env);
	}
}
