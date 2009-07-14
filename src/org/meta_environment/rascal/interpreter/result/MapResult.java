package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.RuntimeExceptionFactory;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptArityError;

public class MapResult extends ElementResult<IMap> {
	
	public MapResult(Type type, IMap map, EvaluatorContext ctx) {
		super(type, map, ctx);
	}
	
	@Override 
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result, EvaluatorContext ctx) {
		return result.addMap(this, ctx);
		
	}

	@Override 
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result, EvaluatorContext ctx) {
		return result.subtractMap(this, ctx);
		
	}
	

	@Override
	public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> result, EvaluatorContext ctx) {
		return result.intersectMap(this, ctx);
	}
	

	@Override
	@SuppressWarnings("unchecked")
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts, EvaluatorContext ctx) {
		if (subscripts.length != 1) {
			throw new UnsupportedSubscriptArityError(getType(), subscripts.length, ctx.getCurrentAST());
		}
		Result<IValue> key = (Result<IValue>) subscripts[0];
		if (!getType().getKeyType().isSubtypeOf(key.getType())) {
			throw new UnexpectedTypeError(getType().getKeyType(), key.getType(), ctx.getCurrentAST());
		}
		IValue v = getValue().get(key.getValue());
		if (v == null){
			throw RuntimeExceptionFactory.noSuchKey(key.getValue(), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		return makeResult(getType().getValueType(), v, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, EvaluatorContext ctx) {
		return that.equalToMap(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, EvaluatorContext ctx) {
		return that.nonEqualToMap(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that, EvaluatorContext ctx) {
		return that.lessThanMap(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that, EvaluatorContext ctx) {
		return that.lessThanOrEqualMap(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that, EvaluatorContext ctx) {
		return that.greaterThanMap(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that, EvaluatorContext ctx) {
		return that.greaterThanOrEqualMap(this, ctx);
	}

	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, EvaluatorContext ctx) {
		return result.compareMap(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> in(Result<V> result, EvaluatorContext ctx) {
		return result.inMap(this, ctx);
	}	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> result, EvaluatorContext ctx) {
		return result.notInMap(this, ctx);
	}	
	
	////
	
	protected <U extends IValue, V extends IValue> Result<U> elementOf(ElementResult<V> elementResult, EvaluatorContext ctx) {
		return bool(getValue().containsValue(elementResult.getValue()));
	}

	protected <U extends IValue, V extends IValue> Result<U> notElementOf(ElementResult<V> elementResult, EvaluatorContext ctx) {
		return bool(!getValue().containsValue(elementResult.getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> addMap(MapResult m, EvaluatorContext ctx) {
		// Note the reverse
		return makeResult(getType().lub(m.getType()), m.value.join(value), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractMap(MapResult m, EvaluatorContext ctx) {
		// Note the reverse
		return makeResult(m.getType(), m.getValue().remove(getValue()), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> intersectMap(MapResult m, EvaluatorContext ctx) {
		// Note the reverse
		return makeResult(m.getType(), m.getValue().common(getValue()), ctx);
	}

	
	@Override
	protected <U extends IValue> Result<U> equalToMap(MapResult that, EvaluatorContext ctx) {
		return that.equalityBoolean(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToMap(MapResult that, EvaluatorContext ctx) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanMap(MapResult that, EvaluatorContext ctx) {
		// note reversed args: we need that < this
		return bool(that.getValue().isSubMap(getValue()) && !that.getValue().isEqual(getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualMap(MapResult that, EvaluatorContext ctx) {
		// note reversed args: we need that <= this
		return bool(that.getValue().isSubMap(getValue()));
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanMap(MapResult that, EvaluatorContext ctx) {
		// note reversed args: we need that > this
		return bool(getValue().isSubMap(that.getValue()) && !getValue().isEqual(that.getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualMap(MapResult that, EvaluatorContext ctx) {
		// note reversed args: we need that >= this
		return bool(getValue().isSubMap(that.getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> compareMap(MapResult that, EvaluatorContext ctx) {
		// Note reversed args
		IMap left = that.getValue();
		IMap right = this.getValue();
		// TODO: this is not right; they can be disjoint
		if (left.isEqual(right)) {
			return makeIntegerResult(0, ctx);
		}
		if (left.isSubMap(left)) {
			return makeIntegerResult(-1, ctx);
		}
		return makeIntegerResult(1, ctx);
	}
	
}
