package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;

public class All implements Strategy {

	private Strategy s;

	public All(Strategy s) {
		this.s = s;
	}

	public Visitable apply(Visitable v) {
		Visitable result = v;
		for (int i = 0; i < v.arity(); i++) {
			result = result.set(i, s.apply(v.get(i)));
		}
		return result;
	}

	public static IValue applyAll(IValue arg, IValue i) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			Strategy s = new All(new StrategyFunction(function));
			Visitable v = s.apply(VisitableFactory.make(i));
			return v.getValue();
		}	
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}

}
