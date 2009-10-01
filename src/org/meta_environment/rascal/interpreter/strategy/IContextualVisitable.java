package org.meta_environment.rascal.interpreter.strategy;


public interface IContextualVisitable extends IVisitable {
		
	public IStrategyContext getContext();

	public void setContext(IStrategyContext context);

}
