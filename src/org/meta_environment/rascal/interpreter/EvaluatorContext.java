package org.meta_environment.rascal.interpreter;

import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.env.Environment;

public class EvaluatorContext {

	
	private Evaluator evaluator;
	private AbstractAST ast;

	public EvaluatorContext(Evaluator eval, AbstractAST ast) {
		this.evaluator = eval;
		this.ast = ast;
	}

	public  AbstractAST getCurrentAST() {
		return ast;
	}
	
	public String getStackTrace() {
		return evaluator.getStackTrace();
	}

	public Evaluator getEvaluator() {
		return evaluator;
	}

	public Environment getCurrentEnvt() {
		return evaluator.getCurrentEnvt();
	}
	
}
