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
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, IEvaluatorContext ctx) {
		return that.equalToBool(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, IEvaluatorContext ctx) {
		return that.nonEqualToBool(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result, IEvaluatorContext ctx) {
		return result.lessThanBool(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result, IEvaluatorContext ctx) {
		return result.lessThanOrEqualBool(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result, IEvaluatorContext ctx) {
		return result.greaterThanBool(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result, IEvaluatorContext ctx) {
		return result.greaterThanOrEqualBool(this, ctx);
	}
	
	@Override
	public <U extends IValue> Result<U> negate(IEvaluatorContext ctx) {
		return bool(getValue().not().getValue());
	}
	
	/////
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> that, IEvaluatorContext ctx) {
		return that.compareBool(this, ctx);
	}
	
	@Override
	public Result<IValue> ifThenElse(Result<IValue> then, Result<IValue> _else, IEvaluatorContext ctx) {
		if (isTrue()) {
			return then;
		}
		return _else;
	}
	
	///
	
	@Override
	protected <U extends IValue> Result<U> compareBool(BoolResult that, IEvaluatorContext ctx) {
		// note:  that <=> this
		BoolResult left = that;
		BoolResult right = this;
		boolean lb = left.getValue().getValue();
		boolean rb = right.getValue().getValue();
		int result = (lb == rb) ? 0 : ((!lb && rb) ? -1 : 1);
		return makeIntegerResult(result, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToBool(BoolResult that, IEvaluatorContext ctx) {
		return that.equalityBoolean(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToBool(BoolResult that, IEvaluatorContext ctx) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanBool(BoolResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that < this
		return bool(that.comparisonInts(this, ctx) < 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualBool(BoolResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that <= this
		return bool(that.comparisonInts(this, ctx) <= 0);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanBool(BoolResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that > this
		return bool(that.comparisonInts(this, ctx) > 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualBool(BoolResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that >= this
		return bool(that.comparisonInts(this, ctx) >= 0);
	}
	
	@Override
	public boolean isTrue() {
		return getValue().getValue();
	}



}
