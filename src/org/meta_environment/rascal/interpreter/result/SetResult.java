package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.ISet;

public class SetResult extends AbstractResult {
	private ISet set;

	public SetResult(ISet set) {
		this.setSet(set);
	}
	
	@Override
	public ISet getValue() {
		return set;
	}

	@Override
	public AbstractResult add(AbstractResult result) {
		return result.addSet(this);
	}
	
	@Override
	public AbstractResult subtract(AbstractResult result) {
		return result.subtractSet(this);
	}
	
	@Override
	public AbstractResult multiply(AbstractResult result) {
		return result.multiplySet(this);
	}
	
	/////
	
	@Override
	protected SetResult addInteger(IntegerResult n) {
		return new SetResult(getSet().insert(n.getInteger()));
	}
	
	@Override
	protected SetResult addReal(RealResult n) {
		return new SetResult(getSet().insert(n.getReal()));
	}
	
	@Override
	protected SetResult addString(StringResult n) {
		return new SetResult(getSet().insert(n.getString()));
	}
	
	@Override
	protected SetResult addBool(BoolResult n) {
		return new SetResult(getSet().insert(n.getBool()));
	}

	@Override
	protected SetResult addSet(SetResult s) {
		return new SetResult(getSet().union(s.getSet()));
	}
	
	@Override
	protected SetResult subtractSet(SetResult s) {
		// note the reverse subtract
		return new SetResult(s.getSet().subtract(getSet()));
	}

	@Override
	protected RelationResult multiplySet(SetResult s) {
		// Note the reverse in .product
		return new RelationResult(s.getSet().product(getSet()));
	}
	
	private void setSet(ISet set) {
		this.set = set;
	}

	public ISet getSet() {
		return set;
	}
	
}
