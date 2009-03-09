package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;

public class SetResult extends CollectionResult<ISet> {

	public SetResult(Type type, ISet set) {
		super(type, set);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> add(AbstractResult<V> result) {
		return result.addSet(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> subtract(AbstractResult<V> result) {
		return result.subtractSet(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> multiply(AbstractResult<V> result) {
		return result.multiplySet(this);
	}

	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> intersect(AbstractResult<V> result) {
		return result.intersectSet(this);
	}
	
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> in(AbstractResult<V> result) {
		return result.inSet(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> notIn(AbstractResult<V> result) {
		return result.notInSet(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> compare(AbstractResult<V> result) {
		return result.compareSet(this);
	}
	
	//////
	
	protected <U extends IValue, V extends IValue> AbstractResult<U> elementOf(ValueResult<V> elementResult) {
		return makeResult(TypeFactory.getInstance().boolType(), iboolOf(getValue().contains(elementResult.getValue())));
	}

	protected <U extends IValue, V extends IValue> AbstractResult<U> notElementOf(ValueResult<V> elementResult) {
		return makeResult(TypeFactory.getInstance().boolType(), iboolOf(!getValue().contains(elementResult.getValue())));
	}


	@Override
	protected <U extends IValue> AbstractResult<U> addSet(SetResult s) {
		return makeResult(type.lub(s.type), getValue().union(s.getValue()));
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> subtractSet(SetResult s) {
		// note the reverse subtract
		return makeResult(type, s.getValue().subtract(getValue()));
	}

	@Override
	protected <U extends IValue> AbstractResult<U> multiplySet(SetResult s) {
		Type resultType = TypeFactory.getInstance().tupleType(s.type.getElementType(), type.getElementType());
		// Note the reverse in .product
		return makeResult(resultType, s.getValue().product(getValue()));
	}
	
	
	@Override 
	protected <U extends IValue> AbstractResult<U> intersectSet(SetResult s) {
		return makeResult(type.lub(s.type), getValue().intersect(s.getValue()));
	}
	
	
	@Override
	<U extends IValue, V extends IValue> AbstractResult<U> insertElement(ValueResult<V> valueResult) {
		return addElement(valueResult);
	}
	
	<U extends IValue, V extends IValue> AbstractResult<U> addElement(ValueResult<V> that) {
		return makeResult(resultTypeWhenAddingElement(that), getValue().insert(that.getValue()));
	}

	<U extends IValue, V extends IValue> AbstractResult<U> removeElement(ValueResult<V> valueResult) {
		return makeResult(type, getValue().delete(valueResult.getValue()));
	}

	@Override
	protected <U extends IValue> AbstractResult<U> compareSet(SetResult that) {
		// Note reversed args
		ISet left = that.getValue();
		ISet right = this.getValue();
		if (left.isEqual(right)) {
			return makeIntegerResult(0);
		}
		if (left.isSubsetOf(right)) {
			return makeIntegerResult(-1);
		}
		return makeIntegerResult(1);
	}
	
		
	private IBool iboolOf(boolean b) {
		return ValueFactoryFactory.getValueFactory().bool(b);
	}
	
}
