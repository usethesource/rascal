package org.meta_environment.rascal.interpreter.result;


import org.eclipse.imp.pdb.facts.ISet;
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
		
}
