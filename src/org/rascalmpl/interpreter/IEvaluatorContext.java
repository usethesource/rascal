package org.rascalmpl.interpreter;

import java.util.Stack;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.strategy.IStrategyContext;

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
	public boolean runTests();
	public IValueFactory getValueFactory();
	public IStrategyContext getStrategyContext();
	public void pushStrategyContext(IStrategyContext strategyContext);
	public void popStrategyContext();
	public void setAccumulators(Stack<Accumulator> accumulators);
	public Stack<Accumulator> getAccumulators();
}