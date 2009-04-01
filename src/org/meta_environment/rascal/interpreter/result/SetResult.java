package org.meta_environment.rascal.interpreter.result;


import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;

public class SetResult extends SetOrRelationResult<ISet> {

	public SetResult(Type type, ISet set) {
		super(type, set);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result, AbstractAST ast) {
		return result.addSet(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result, AbstractAST ast) {
		return result.subtractSet(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result, AbstractAST ast) {
		return result.multiplySet(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> join(Result<V> that, AbstractAST ast) {
		return that.joinSet(this, ast);
	}
	

	@Override
	public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> result, AbstractAST ast) {
		return result.intersectSet(this, ast);
	}
	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> in(Result<V> result, AbstractAST ast) {
		return result.inSet(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> result, AbstractAST ast) {
		return result.notInSet(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, AbstractAST ast) {
		return that.equalToSet(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, AbstractAST ast) {
		return that.nonEqualToSet(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that, AbstractAST ast) {
		return that.lessThanSet(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that, AbstractAST ast) {
		return that.lessThanOrEqualSet(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that, AbstractAST ast) {
		return that.greaterThanSet(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that, AbstractAST ast) {
		return that.greaterThanOrEqualSet(this, ast);
	}


	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, AbstractAST ast) {
		return result.compareSet(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> joinRelation(RelationResult that, AbstractAST ast) {
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
		return makeResult(resultType, writer.done());
	}

	@Override
	protected <U extends IValue> Result<U> joinSet(SetResult that, AbstractAST ast) {
		// Note the reverse of arguments, we need "that join this"
		// join between sets degenerates to product
		Type tupleType = getTypeFactory().tupleType(that.getType().getElementType(), 
				getType().getElementType());
		return makeResult(getTypeFactory().relTypeFromTuple(tupleType),
				that.getValue().product(getValue()));
	}
}