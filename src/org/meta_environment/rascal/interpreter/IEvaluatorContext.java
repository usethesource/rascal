package org.meta_environment.rascal.interpreter;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;

public interface IEvaluatorContext {
	public AbstractAST getCurrentAST();
	public void setCurrentAST(AbstractAST ast);
	public String getStackTrace();
	public Evaluator getEvaluator();
	public Environment getCurrentEnvt();
	public void pushEnv();
	public void unwind(Environment old);
	public GlobalEnvironment getHeap();
	public void setCurrentEnvt(Environment environment);
	public void runTests();
	public IValueFactory getValueFactory();
}