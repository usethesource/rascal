package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptArityError;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class MapResult extends ElementResult<IMap> {
	
	public MapResult(Type type, IMap map, IEvaluatorContext ctx) {
		super(type, map, ctx);
	}
	
	@Override 
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result) {
		return result.addMap(this);
		
	}

	@Override 
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result) {
		return result.subtractMap(this);
		
	}
	

	@Override
	public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> result) {
		return result.intersectMap(this);
	}
	

	@Override
	@SuppressWarnings("unchecked")
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts) {
		if (subscripts.length != 1) {
			throw new UnsupportedSubscriptArityError(getType(), subscripts.length, ctx.getCurrentAST());
		}
		Result<IValue> key = (Result<IValue>) subscripts[0];
		if (!getType().getKeyType().comparable(key.getType())) {
			throw new UnexpectedTypeError(getType().getKeyType(), key.getType(), ctx.getCurrentAST());
		}
		IValue v = getValue().get(key.getValue());
		if (v == null){
			throw RuntimeExceptionFactory.noSuchKey(key.getValue(), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		return makeResult(getType().getValueType(), v, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToMap(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToMap(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that) {
		return that.lessThanMap(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that) {
		return that.lessThanOrEqualMap(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that) {
		return that.greaterThanMap(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that) {
		return that.greaterThanOrEqualMap(this);
	}

	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result) {
		return result.compareMap(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> in(Result<V> result) {
		return result.inMap(this);
	}	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> result) {
		return result.notInMap(this);
	}	
	
	////
	
	protected <U extends IValue, V extends IValue> Result<U> elementOf(ElementResult<V> elementResult) {
		return bool(getValue().containsValue(elementResult.getValue()));
	}

	protected <U extends IValue, V extends IValue> Result<U> notElementOf(ElementResult<V> elementResult) {
		return bool(!getValue().containsValue(elementResult.getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> addMap(MapResult m) {
		// Note the reverse
		return makeResult(getType().lub(m.getType()), m.value.join(value), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractMap(MapResult m) {
		// Note the reverse
		return makeResult(m.getType(), m.getValue().remove(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> intersectMap(MapResult m) {
		// Note the reverse
		return makeResult(m.getType(), m.getValue().common(getValue()), ctx);
	}

	
	@Override
	protected <U extends IValue> Result<U> equalToMap(MapResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToMap(MapResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanMap(MapResult that) {
		// note reversed args: we need that < this
		return bool(that.getValue().isSubMap(getValue()) && !that.getValue().isEqual(getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualMap(MapResult that) {
		// note reversed args: we need that <= this
		return bool(that.getValue().isSubMap(getValue()));
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanMap(MapResult that) {
		// note reversed args: we need that > this
		return bool(getValue().isSubMap(that.getValue()) && !getValue().isEqual(that.getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualMap(MapResult that) {
		// note reversed args: we need that >= this
		return bool(getValue().isSubMap(that.getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> compareMap(MapResult that) {
		// Note reversed args
		IMap left = that.getValue();
		IMap right = this.getValue();
		// TODO: this is not right; they can be disjoint
		if (left.isEqual(right)) {
			return makeIntegerResult(0);
		}
		if (left.isSubMap(left)) {
			return makeIntegerResult(-1);
		}
		return makeIntegerResult(1);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compose(
			Result<V> right) {
		return right.composeMap(this);
	}
	
	@Override
	public <U extends IValue> Result<U> composeMap(MapResult left) {
		if (left.getType().getValueType().isSubtypeOf(getType().getKeyType())) {
			Type mapType = getTypeFactory().mapType(left.getType().getKeyType(), getType().getValueType());
			return ResultFactory.makeResult(mapType, left.getValue().compose(getValue()), ctx);
		}
		
		return undefinedError("composition", left);
	}
	
}
