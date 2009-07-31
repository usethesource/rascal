package org.meta_environment.rascal.interpreter;

import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;

public interface IEvaluatorContext {
	public  AbstractAST getCurrentAST();
	public String getStackTrace();
	public Evaluator getEvaluator();
	public Environment getCurrentEnvt();
	public void goodPushEnv();
	public void unwind(Environment old);
	public GlobalEnvironment getHeap();
	public void setCurrentEnvt(Environment environment);
}
