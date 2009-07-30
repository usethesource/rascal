package org.meta_environment.rascal.interpreter;

import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.env.Environment;

public interface IEvaluatorContext {
	public  AbstractAST getCurrentAST();
	public String getStackTrace();
	public Evaluator getEvaluator();
	public Environment getCurrentEnvt();
	public void goodPushEnv();
	public void unwind(Environment old);
	
}
