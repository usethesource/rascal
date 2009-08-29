package org.meta_environment.rascal.interpreter.result;

import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.types.FunctionType;

abstract public class NamedFunction extends AbstractFunction {
	protected String name;
	
	public NamedFunction(AbstractAST ast, Evaluator eval, FunctionType functionType, String name,
			boolean varargs, Environment env) {
		super(ast, eval, functionType, varargs, env);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
