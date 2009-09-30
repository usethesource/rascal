package org.meta_environment.rascal.interpreter.strategy;

import org.meta_environment.rascal.interpreter.result.AbstractFunction;

public class ContextualStrategy extends Strategy {

	protected IContextualVisitable v;

	public ContextualStrategy(IContextualVisitable v, AbstractFunction function) {
		super(function);
		this.v = v;
	}

	public IContextualVisitable getVisitable() {
		return v;
	}

}
