package org.meta_environment.rascal.interpreter.result;


import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class SetResult extends SetOrRelationResult<ISet> {

	public SetResult(Type type, ISet set, IEvaluatorContext ctx) {
		super(type, set, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result, IEvaluatorContext ctx) {
		return result.addSet(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result, IEvaluatorContext ctx) {
		return result.subtractSet(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result, IEvaluatorContext ctx) {
		return result.multiplySet(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> join(Result<V> that, IEvaluatorContext ctx) {
		return that.joinSet(this, ctx);
	}
	

	@Override
	public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> result, IEvaluatorContext ctx) {
		return result.intersectSet(this, ctx);
	}
	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> in(Result<V> result, IEvaluatorContext ctx) {
		return result.inSet(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> result, IEvaluatorContext ctx) {
		return result.notInSet(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, IEvaluatorContext ctx) {
		return that.equalToSet(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, IEvaluatorContext ctx) {
		return that.nonEqualToSet(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that, IEvaluatorContext ctx) {
		return that.lessThanSet(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that, IEvaluatorContext ctx) {
		return that.lessThanOrEqualSet(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that, IEvaluatorContext ctx) {
		return that.greaterThanSet(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that, IEvaluatorContext ctx) {
		return that.greaterThanOrEqualSet(this, ctx);
	}


	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, IEvaluatorContext ctx) {
		return result.compareSet(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> joinRelation(RelationResult that, IEvaluatorContext ctx) {
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
	protected <U extends IValue> Result<U> joinSet(SetResult that, IEvaluatorContext ctx) {
		// Note the reverse of arguments, we need "that join this"
		// join between sets degenerates to product
		Type tupleType = getTypeFactory().tupleType(that.getType().getElementType(), 
				getType().getElementType());
		return makeResult(getTypeFactory().relTypeFromTuple(tupleType),
				that.getValue().product(getValue()), ctx);
	}
}