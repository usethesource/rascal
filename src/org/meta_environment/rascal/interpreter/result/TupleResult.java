package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.UndeclaredFieldException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.interpreter.RuntimeExceptionFactory;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFieldError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptArityError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptError;
import org.meta_environment.rascal.ast.AbstractAST;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

public class TupleResult extends ElementResult<ITuple> {
	
	public TupleResult(Type type, ITuple tuple, AbstractAST ast) {
		super(type, tuple, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> that, AbstractAST ast) {
		return that.addTuple(this, ast);
	}
	
	@Override
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store, AbstractAST ast) {
			if (!getType().hasFieldNames()) {
				throw new UndeclaredFieldError(name, getType(), ast);
			}
			try {
				int index = getType().getFieldIndex(name);
				Type type = getType().getFieldType(index);
				return makeResult(type, getValue().get(index), ast);
			} 
			catch (UndeclaredFieldException e){
				throw new UndeclaredFieldError(name, getType(), ast);
			}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts, AbstractAST ast) {
		if (subscripts.length > 1) {
			throw new UnsupportedSubscriptArityError(getType(), subscripts.length, ast);
		}
		Result<IValue> subsBase = (Result<IValue>)subscripts[0];
		if (!subsBase.getType().isIntegerType()){
			throw new UnsupportedSubscriptError(getTypeFactory().integerType(), subsBase.getType(), ast);
		}
		IInteger index = (IInteger)subsBase.getValue();
		if (index.intValue() >= getValue().arity()) {
			throw RuntimeExceptionFactory.indexOutOfBounds(index, ast, null);
		}
		
		Type elementType = getType().getFieldType(index.intValue());
		IValue element = getValue().get(index.intValue());
		return makeResult(elementType, element, ast);
	};
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, AbstractAST ast) {
		return result.compareTuple(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, AbstractAST ast) {
		return that.equalToTuple(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, AbstractAST ast) {
		return that.nonEqualToTuple(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result, AbstractAST ast) {
		return result.lessThanTuple(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result, AbstractAST ast) {
		return result.lessThanOrEqualTuple(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result, AbstractAST ast) {
		return result.greaterThanTuple(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result, AbstractAST ast) {
		return result.greaterThanOrEqualTuple(this, ast);
	}
	
	///

	@Override
	protected <U extends IValue> Result<U> addTuple(TupleResult that, AbstractAST ast) {
		// Note reversed args
		TupleResult left = that;
		TupleResult right = this;
		Type leftType = left.getType();
		Type rightType = right.getType();
		
		int leftArity = leftType.getArity();
		int rightArity = rightType.getArity();
		int newArity = leftArity + rightArity;
		
		Type fieldTypes[] = new Type[newArity];
		String fieldNames[] = new String[newArity];
		IValue fieldValues[] = new IValue[newArity];
		
		for(int i = 0; i < leftArity; i++){
			fieldTypes[i] = leftType.getFieldType(i);
			fieldNames[i] = leftType.getFieldName(i);
			fieldValues[i] = left.getValue().get(i);
		}
		
		for(int i = 0; i < rightArity; i++){
			fieldTypes[leftArity + i] = rightType.getFieldType(i);
			fieldNames[leftArity + i] = rightType.getFieldName(i);
			fieldValues[leftArity + i] = right.getValue().get(i);
		}
		
		// TODO: avoid null fieldnames
		for(int i = 0; i < newArity; i++){
			if(fieldNames[i] == null){
				fieldNames[i] = "f" + String.valueOf(i);
			}
		}
		Type newTupleType = getTypeFactory().tupleType(fieldTypes, fieldNames);
		return makeResult(newTupleType, getValueFactory().tuple(fieldValues), ast);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> addRelation(RelationResult that, AbstractAST ast) {
		return that.insertTuple(this, ast);
	};
	
	@Override
	protected <U extends IValue> Result<U> equalToTuple(TupleResult that, AbstractAST ast) {
		return that.equalityBoolean(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToTuple(TupleResult that, AbstractAST ast) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareTuple(TupleResult that, AbstractAST ast) {
		// Note reversed args
		ITuple left = that.getValue();
		ITuple right = this.getValue();
		int compare = new Integer(left.arity()).compareTo(right.arity());
		if (compare != 0) {
			return makeIntegerResult(compare, ast);
		}
		for (int i = 0; i < left.arity(); i++) {
			compare = compareIValues(left.get(i), right.get(i), ast);
			if (compare != 0) {
				return makeIntegerResult(compare, ast);
			}
		}
		return makeIntegerResult(0, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanTuple(TupleResult that, AbstractAST ast) {
		// note reversed args: we need that < this
		return bool(that.comparisonInts(this, ast) < 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualTuple(TupleResult that, AbstractAST ast) {
		// note reversed args: we need that <= this
		return bool(that.comparisonInts(this, ast) <= 0);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanTuple(TupleResult that, AbstractAST ast) {
		// note reversed args: we need that > this
		return bool(that.comparisonInts(this, ast) > 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualTuple(TupleResult that, AbstractAST ast) {
		// note reversed args: we need that >= this
		return bool(that.comparisonInts(this, ast) >= 0);
	}
}
