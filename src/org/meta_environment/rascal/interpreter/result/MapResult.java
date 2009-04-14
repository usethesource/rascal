package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.RuntimeExceptionFactory;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptArityError;

public class MapResult extends ElementResult<IMap> {
	
	public MapResult(Type type, IMap map, AbstractAST ast) {
		super(type, map, ast);
	}
	
	@Override 
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result, AbstractAST ast) {
		return result.addMap(this, ast);
		
	}

	@Override 
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result, AbstractAST ast) {
		return result.subtractMap(this, ast);
		
	}
	

	@Override
	public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> result, AbstractAST ast) {
		return result.intersectMap(this, ast);
	}
	

	@Override
	@SuppressWarnings("unchecked")
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts, AbstractAST ast) {
		if (subscripts.length != 1) {
			throw new UnsupportedSubscriptArityError(getType(), subscripts.length, ast);
		}
		Result<IValue> key = (Result<IValue>) subscripts[0];
		if (!key.getType().isSubtypeOf(getType().getKeyType())) {
			throw new UnexpectedTypeError(getType().getKeyType(), key.getType(), ast);
		}
		IValue v = getValue().get(key.getValue());
		if (v == null){
			throw RuntimeExceptionFactory.noSuchKey(key.getValue(), ast);
		}
		return makeResult(getType().getValueType(), v, ast);
	};
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, AbstractAST ast) {
		return that.equalToMap(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, AbstractAST ast) {
		return that.nonEqualToMap(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that, AbstractAST ast) {
		return that.lessThanMap(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that, AbstractAST ast) {
		return that.lessThanOrEqualMap(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that, AbstractAST ast) {
		return that.greaterThanMap(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that, AbstractAST ast) {
		return that.greaterThanOrEqualMap(this, ast);
	}

	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, AbstractAST ast) {
		return result.compareMap(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> in(Result<V> result, AbstractAST ast) {
		return result.inMap(this, ast);
	}	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> result, AbstractAST ast) {
		return result.notInMap(this, ast);
	}	
	
	////
	
	protected <U extends IValue, V extends IValue> Result<U> elementOf(ElementResult<V> elementResult, AbstractAST ast) {
		return bool(getValue().containsValue(elementResult.getValue()));
	}

	protected <U extends IValue, V extends IValue> Result<U> notElementOf(ElementResult<V> elementResult, AbstractAST ast) {
		return bool(!getValue().containsValue(elementResult.getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> addMap(MapResult m, AbstractAST ast) {
		// Note the reverse
		return makeResult(getType().lub(m.getType()), m.value.join(value), ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractMap(MapResult m, AbstractAST ast) {
		// Note the reverse
		return makeResult(m.getType(), m.getValue().remove(getValue()), ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> intersectMap(MapResult m, AbstractAST ast) {
		// Note the reverse
		return makeResult(m.getType(), m.getValue().common(getValue()), ast);
	}

	
	@Override
	protected <U extends IValue> Result<U> equalToMap(MapResult that, AbstractAST ast) {
		return that.equalityBoolean(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToMap(MapResult that, AbstractAST ast) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanMap(MapResult that, AbstractAST ast) {
		// note reversed args: we need that < this
		return bool(that.getValue().isSubMap(getValue()) && !that.getValue().isEqual(getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualMap(MapResult that, AbstractAST ast) {
		// note reversed args: we need that <= this
		return bool(that.getValue().isSubMap(getValue()));
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanMap(MapResult that, AbstractAST ast) {
		// note reversed args: we need that > this
		return bool(getValue().isSubMap(that.getValue()) && !getValue().isEqual(that.getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualMap(MapResult that, AbstractAST ast) {
		// note reversed args: we need that >= this
		return bool(getValue().isSubMap(that.getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> compareMap(MapResult that, AbstractAST ast) {
		// Note reversed args
		IMap left = that.getValue();
		IMap right = this.getValue();
		// TODO: this is not right; they can be disjoint
		if (left.isEqual(right)) {
			return makeIntegerResult(0, ast);
		}
		if (left.isSubMap(left)) {
			return makeIntegerResult(-1, ast);
		}
		return makeIntegerResult(1, ast);
	}
	
}
