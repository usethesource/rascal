package org.meta_environment.rascal.interpreter.env;


import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.Names;

public class RascalFunction extends Lambda {

	public RascalFunction(Evaluator eval, FunctionDeclaration func, boolean varargs, Environment env) {
		super(func, eval,
				TE.eval(func.getSignature().getType(), env),
				Names.name(func.getSignature().getName()),
				TE.eval(func.getSignature().getParameters(), env), 
				varargs, func.getBody().getStatements(), env);
	}
}
