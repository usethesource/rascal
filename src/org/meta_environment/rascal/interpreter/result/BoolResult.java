package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.ast.AbstractAST;

public class BoolResult extends ElementResult<IBool> {

	public BoolResult(Type type, IBool bool) {
		this(type, bool, null);
	}
		
	public BoolResult(Type type, IBool bool, Iterator<Result<IValue>> iter) {
		super(type, bool, iter);
	}
	
	public BoolResult(boolean b) {
		this(TypeFactory.getInstance().boolType(), ValueFactoryFactory.getValueFactory().bool(b));
	}
	
	public BoolResult(boolean b, Iterator<Result<IValue>> iter){
		this(TypeFactory.getInstance().boolType(), ValueFactoryFactory.getValueFactory().bool(b), iter);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, AbstractAST ast) {
		return that.equalToBool(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, AbstractAST ast) {
		return that.nonEqualToBool(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result, AbstractAST ast) {
		return result.lessThanBool(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result, AbstractAST ast) {
		return result.lessThanOrEqualBool(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result, AbstractAST ast) {
		return result.greaterThanBool(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result, AbstractAST ast) {
		return result.greaterThanOrEqualBool(this, ast);
	}
	
	@Override
	public <U extends IValue> Result<U> negate(AbstractAST ast) {
		return bool(getValue().not().getValue());
	}
	
	/////
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> that, AbstractAST ast) {
		return that.compareBool(this, ast);
	}
	
	@Override
	public Result<IValue> ifThenElse(Result<IValue> then, Result<IValue> _else, AbstractAST ast) {
		if (isTrue()) {
			return then;
		}
		return _else;
	}
	
	///
	
	@Override
	protected <U extends IValue> Result<U> compareBool(BoolResult that, AbstractAST ast) {
		// note:  that <=> this
		BoolResult left = that;
		BoolResult right = this;
		boolean lb = left.getValue().getValue();
		boolean rb = right.getValue().getValue();
		int result = (lb == rb) ? 0 : ((!lb && rb) ? -1 : 1);
		return makeIntegerResult(result);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToBool(BoolResult that, AbstractAST ast) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToBool(BoolResult that, AbstractAST ast) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanBool(BoolResult that, AbstractAST ast) {
		// note reversed args: we need that < this
		return bool(that.comparisonInts(this, ast) < 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualBool(BoolResult that, AbstractAST ast) {
		// note reversed args: we need that <= this
		return bool(that.comparisonInts(this, ast) <= 0);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanBool(BoolResult that, AbstractAST ast) {
		// note reversed args: we need that > this
		return bool(that.comparisonInts(this, ast) > 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualBool(BoolResult that, AbstractAST ast) {
		// note reversed args: we need that >= this
		return bool(that.comparisonInts(this, ast) >= 0);
	}
	
	public boolean isTrue() {
		return getValue().getValue();
	}



}
