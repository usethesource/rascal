package org.meta_environment.rascal.interpreter;

import java.util.List;

import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.control_exceptions.FailedTestError;
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
	public List<FailedTestError> runTests();
	public List<FailedTestError> runTests(String module);
	public String report(List<FailedTestError> failedTests);
}
