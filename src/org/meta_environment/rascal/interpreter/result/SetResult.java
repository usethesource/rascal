package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.type.Type;

public class SetResult extends CollectionResult {
	private ISet set;

	public SetResult(Type type, ISet set) {
		super(type, set);
		this.set = set;
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

	@Override
	public AbstractResult intersect(AbstractResult result) {
		return result.intersectSet(this);
	}
	
	
	@Override
	public AbstractResult in(AbstractResult result) {
		return result.inSet(this);
	}
	
	@Override
	public AbstractResult notIn(AbstractResult result) {
		return result.notInSet(this);
	}
	
	//////
	
	@Override
	protected SetResult addSet(SetResult s) {
		return new SetResult(type, getValue().union(s.getValue()));
	}
	
	@Override
	protected SetResult subtractSet(SetResult s) {
		// note the reverse subtract
		return new SetResult(type, s.getValue().subtract(getValue()));
	}

	@Override
	protected RelationResult multiplySet(SetResult s) {
		// Note the reverse in .product
		return new RelationResult(type, s.getValue().product(getValue()));
	}
	
	
	@Override 
	protected SetResult intersectSet(SetResult s) {
		return new SetResult(type, getValue().intersect(s.getValue()));
	}
	
	
	SetResult insertElement(ValueResult valueResult) {
		return addElement(valueResult);
	}
	
	SetResult addElement(ValueResult valueResult) {
		return new SetResult(type, getValue().insert(valueResult.getValue()));
	}

	SetResult removeElement(ValueResult valueResult) {
		return new SetResult(type, getValue().delete(valueResult.getValue()));
	}

	public BoolResult elementOf(ValueResult elementResult) {
		return new BoolResult(getValue().contains(elementResult.getValue()));
	}

	public BoolResult notElementOf(ValueResult elementResult) {
		return new BoolResult(!getValue().contains(elementResult.getValue()));
	}
	
}
