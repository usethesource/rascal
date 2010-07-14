package org.rascalmpl.interpreter.result;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.types.FunctionType;

abstract public class NamedFunction extends AbstractFunction {
	protected String name;
	
	public NamedFunction(AbstractAST ast, Evaluator eval, FunctionType functionType, String name,
			boolean varargs, Environment env, boolean isStatic) {
		super(ast, eval, functionType, varargs, env, isStatic);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
