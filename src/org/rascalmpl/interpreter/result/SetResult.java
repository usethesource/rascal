package org.rascalmpl.interpreter.result;


import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class SetResult extends SetOrRelationResult<ISet> {

	public SetResult(Type type, ISet set, IEvaluatorContext ctx) {
		super(type, set, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result) {
		return result.addSet(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result) {
		return result.subtractSet(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result) {
		return result.multiplySet(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> join(Result<V> that) {
		return that.joinSet(this);
	}
	

	@Override
	public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> result) {
		return result.intersectSet(this);
	}
	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> in(Result<V> result) {
		return result.inSet(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> result) {
		return result.notInSet(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToSet(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToSet(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that) {
		return that.lessThanSet(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that) {
		return that.lessThanOrEqualSet(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that) {
		return that.greaterThanSet(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that) {
		return that.greaterThanOrEqualSet(this);
	}


	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result) {
		return result.compareSet(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> joinRelation(RelationResult that) {
		// Note the reverse of arguments, we need "that join this"
		int arity1 = that.getValue().arity();
		Type eltType = getType().getElementType();
		Type tupleType = that.getType().getElementType();
		Type fieldTypes[] = new Type[arity1 + 1];
		for (int i = 0;  i < arity1; i++) {
			fieldTypes[i] = tupleType.getFieldType(i);
		}
		fieldTypes[arity1] = eltType;
		Type resultTupleType = getTypeFactory().tupleType(fieldTypes);
		ISetWriter writer = getValueFactory().setWriter(resultTupleType);
		IValue fieldValues[] = new IValue[arity1 + 1];
		for (IValue relValue: that.getValue()) {
			for (IValue setValue: this.getValue()) {
				for (int i = 0; i < arity1; i++) {
					fieldValues[i] = ((ITuple)relValue).get(i);
				}
				fieldValues[arity1] = setValue;
				writer.insert(getValueFactory().tuple(fieldValues));
			}
		}
		Type resultType = getTypeFactory().relTypeFromTuple(resultTupleType);
		return makeResult(resultType, writer.done(), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> joinSet(SetResult that) {
		// Note the reverse of arguments, we need "that join this"
		// join between sets degenerates to product
		Type tupleType = getTypeFactory().tupleType(that.getType().getElementType(), 
				getType().getElementType());
		return makeResult(getTypeFactory().relTypeFromTuple(tupleType),
				that.getValue().product(getValue()), ctx);
	}
}