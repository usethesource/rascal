package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.Result;

public class ContextualStrategy extends Strategy {

	protected IContextualVisitable v;

	public ContextualStrategy(IContextualVisitable v, AbstractFunction function) {
		super(function);
		this.v = v;
	}
	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		Result<IValue> res = function.call(argTypes, argValues);
		v.updateContext(argValues[0], res.getValue());
		return res;
	}

	public IContextualVisitable getVisitable() {
		return v;
	}

}
