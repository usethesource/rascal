package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.UndeclaredFieldException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFieldError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptArityError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptError;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

public class TupleResult extends ElementResult<ITuple> {
	
	public TupleResult(Type type, ITuple tuple, IEvaluatorContext ctx) {
		super(type, tuple, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> that, IEvaluatorContext ctx) {
		return that.addTuple(this, ctx);
	}
	
	@Override
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store, IEvaluatorContext ctx) {
			if (!getType().hasFieldNames()) {
				throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
			}
			try {
				int index = getType().getFieldIndex(name);
				Type type = getType().getFieldType(index);
				return makeResult(type, getValue().get(index), ctx);
			} 
			catch (UndeclaredFieldException e){
				throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
			}
	}
		
	@Override
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store, IEvaluatorContext ctx) {
		if (!getType().hasFieldNames()) {
			throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
		}

		try {
			int index = getType().getFieldIndex(name);
			Type type = getType().getFieldType(index);
			if(!type.isSubtypeOf(repl.getType())){
				throw new UnexpectedTypeError(type, repl.getType(), ctx.getCurrentAST());
			}
			return makeResult(getType(), getValue().set(index, repl.getValue()), ctx);
		} catch (UndeclaredFieldException e) {
			throw new UndeclaredFieldError(name, getType(), ctx.getCurrentAST());
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts, IEvaluatorContext ctx) {
		if (subscripts.length > 1) {
			throw new UnsupportedSubscriptArityError(getType(), subscripts.length, ctx.getCurrentAST());
		}
		Result<IValue> subsBase = (Result<IValue>)subscripts[0];
		if(subsBase == null)
			/*
			 * Wild card not allowed as tuple subscript
			 */
			throw new UnsupportedSubscriptError(type, null, ctx.getCurrentAST());
		if (!subsBase.getType().isIntegerType()){
			throw new UnsupportedSubscriptError(getTypeFactory().integerType(), subsBase.getType(), ctx.getCurrentAST());
		}
		IInteger index = (IInteger)subsBase.getValue();
		if (index.intValue() >= getValue().arity()) {
			throw RuntimeExceptionFactory.indexOutOfBounds(index, ctx.getCurrentAST(), null);
		}
		
		Type elementType = getType().getFieldType(index.intValue());
		IValue element = getValue().get(index.intValue());
		return makeResult(elementType, element, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, IEvaluatorContext ctx) {
		return result.compareTuple(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, IEvaluatorContext ctx) {
		return that.equalToTuple(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, IEvaluatorContext ctx) {
		return that.nonEqualToTuple(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result, IEvaluatorContext ctx) {
		return result.lessThanTuple(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result, IEvaluatorContext ctx) {
		return result.lessThanOrEqualTuple(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result, IEvaluatorContext ctx) {
		return result.greaterThanTuple(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result, IEvaluatorContext ctx) {
		return result.greaterThanOrEqualTuple(this, ctx);
	}
	
	///

	@Override
	protected <U extends IValue> Result<U> addTuple(TupleResult that, IEvaluatorContext ctx) {
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
		return makeResult(newTupleType, getValueFactory().tuple(fieldValues), ctx);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> addRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.insertTuple(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToTuple(TupleResult that, IEvaluatorContext ctx) {
		return that.equalityBoolean(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToTuple(TupleResult that, IEvaluatorContext ctx) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareTuple(TupleResult that, IEvaluatorContext ctx) {
		// Note reversed args
		ITuple left = that.getValue();
		ITuple right = this.getValue();
		int compare = Integer.valueOf(left.arity()).compareTo(Integer.valueOf(right.arity()));
		if (compare != 0) {
			return makeIntegerResult(compare, ctx);
		}
		for (int i = 0; i < left.arity(); i++) {
			compare = compareIValues(left.get(i), right.get(i), ctx);
			if (compare != 0) {
				return makeIntegerResult(compare, ctx);
			}
		}
		return makeIntegerResult(0, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanTuple(TupleResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that < this
		return bool(that.comparisonInts(this, ctx) < 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualTuple(TupleResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that <= this
		return bool(that.comparisonInts(this, ctx) <= 0);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanTuple(TupleResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that > this
		return bool(that.comparisonInts(this, ctx) > 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualTuple(TupleResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that >= this
		return bool(that.comparisonInts(this, ctx) >= 0);
	}
}
