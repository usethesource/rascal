package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class BoolResult extends ElementResult<IBool> {

	public BoolResult(Type type, IBool bool, IEvaluatorContext ctx) {
		this(type, bool, null, ctx);
	}
		
	public BoolResult(Type type, IBool bool, Iterator<Result<IValue>> iter, IEvaluatorContext ctx) {
		super(type, bool, iter, ctx);
	}
	
	public BoolResult(boolean b, IEvaluatorContext ctx) {
		this(TypeFactory.getInstance().boolType(), ValueFactoryFactory.getValueFactory().bool(b), ctx);
	}
	
	public BoolResult(boolean b, Iterator<Result<IValue>> iter, IEvaluatorContext ctx){
		this(TypeFactory.getInstance().boolType(), ValueFactoryFactory.getValueFactory().bool(b), iter, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToBool(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToBool(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result) {
		return result.lessThanBool(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualBool(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result) {
		return result.greaterThanBool(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result) {
		return result.greaterThanOrEqualBool(this);
	}
	
	@Override
	public <U extends IValue> Result<U> negate() {
		return bool(getValue().not().getValue());
	}
	
	/////
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> that) {
		return that.compareBool(this);
	}
	
	@Override
	public Result<IValue> ifThenElse(Result<IValue> then, Result<IValue> _else) {
		if (isTrue()) {
			return then;
		}
		return _else;
	}
	
	///
	
	@Override
	protected <U extends IValue> Result<U> compareBool(BoolResult that) {
		// note:  that <=> this
		BoolResult left = that;
		BoolResult right = this;
		boolean lb = left.getValue().getValue();
		boolean rb = right.getValue().getValue();
		int result = (lb == rb) ? 0 : ((!lb && rb) ? -1 : 1);
		return makeIntegerResult(result);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToBool(BoolResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToBool(BoolResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanBool(BoolResult that) {
		// note reversed args: we need that < this
		return bool(that.comparisonInts(this) < 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualBool(BoolResult that) {
		// note reversed args: we need that <= this
		return bool(that.comparisonInts(this) <= 0);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanBool(BoolResult that) {
		// note reversed args: we need that > this
		return bool(that.comparisonInts(this) > 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualBool(BoolResult that) {
		// note reversed args: we need that >= this
		return bool(that.comparisonInts(this) >= 0);
	}
	
	@Override
	public boolean isTrue() {
		return getValue().getValue();
	}
}
