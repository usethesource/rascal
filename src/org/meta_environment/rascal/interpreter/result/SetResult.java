package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

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
		return makeResult(type.lub(s.type), getValue().union(s.getValue()));
	}
	
	@Override
	protected SetResult subtractSet(SetResult s) {
		// note the reverse subtract
		return makeResult(type, s.getValue().subtract(getValue()));
	}

	@Override
	protected RelationResult multiplySet(SetResult s) {
		Type resultType = TypeFactory.getInstance().tupleType(s.type.getElementType(), type.getElementType());
		// Note the reverse in .product
		return makeResult(resultType, s.getValue().product(getValue()));
	}
	
	
	@Override 
	protected SetResult intersectSet(SetResult s) {
		return makeResult(type.lub(s.type), getValue().intersect(s.getValue()));
	}
	
	
	SetResult insertElement(ValueResult valueResult) {
		return addElement(valueResult);
	}
	
	SetResult addElement(ValueResult that) {
		return makeResult(resultTypeWhenAddingElement(that), getValue().insert(that.getValue()));
	}

	SetResult removeElement(ValueResult valueResult) {
		return makeResult(type, getValue().delete(valueResult.getValue()));
	}

	public BoolResult elementOf(ValueResult elementResult) {
		return makeResult(TypeFactory.getInstance().boolType(), iboolOf(getValue().contains(elementResult.getValue())));
	}

	public BoolResult notElementOf(ValueResult elementResult) {
		return makeResult(TypeFactory.getInstance().boolType(), iboolOf(!getValue().contains(elementResult.getValue())));
	}
	
	private IBool iboolOf(boolean b) {
		return ValueFactoryFactory.getValueFactory().bool(b);
	}
	
}
