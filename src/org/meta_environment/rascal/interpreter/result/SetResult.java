package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;

import org.meta_environment.rascal.ast.AbstractAST;

public class SetResult extends CollectionResult<ISet> {

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
	
	//////
	
	protected <U extends IValue, V extends IValue> Result<U> elementOf(ElementResult<V> elementResult, AbstractAST ast) {
		return makeResult(TypeFactory.getInstance().boolType(), iboolOf(getValue().contains(elementResult.getValue())));
	}

	protected <U extends IValue, V extends IValue> Result<U> notElementOf(ElementResult<V> elementResult, AbstractAST ast) {
		return makeResult(TypeFactory.getInstance().boolType(), iboolOf(!getValue().contains(elementResult.getValue())));
	}


	@Override
	protected <U extends IValue> Result<U> addSet(SetResult s, AbstractAST ast) {
		return makeResult(type.lub(s.type), getValue().union(s.getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> addRelation(RelationResult s, AbstractAST ast) {
		return makeResult(type.lub(s.type), getValue().union(s.getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractSet(SetResult s, AbstractAST ast) {
		// note the reverse subtract
		return makeResult(getType().lub(s.getType()), s.getValue().subtract(getValue()));
	}

	@Override
	protected <U extends IValue> Result<U> subtractRelation(RelationResult s, AbstractAST ast) {
		// note the reverse subtract
		return makeResult(getType().lub(s.getType()), s.getValue().subtract(getValue()));
	}

	@Override
	protected <U extends IValue> Result<U> multiplyRelation(RelationResult that, AbstractAST ast) {
		Type tupleType = getTypeFactory().tupleType(that.type.getElementType(), type.getElementType());
		// Note the reverse in .product
		return makeResult(getTypeFactory().relTypeFromTuple(tupleType), that.getValue().product(getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplySet(SetResult s, AbstractAST ast) {
		Type tupleType = getTypeFactory().tupleType(s.type.getElementType(), type.getElementType());
		// Note the reverse in .product
		return makeResult(getTypeFactory().relTypeFromTuple(tupleType), s.getValue().product(getValue()));
	}
	
	@Override 
	protected <U extends IValue> Result<U> intersectSet(SetResult s, AbstractAST ast) {
		return makeResult(type.lub(s.type), getValue().intersect(s.getValue()));
	}
	
	@Override 
	protected <U extends IValue> Result<U> intersectRelation(RelationResult s, AbstractAST ast) {
		return makeResult(type.lub(s.type), getValue().intersect(s.getValue()));
	}
	
	
	@Override
	<U extends IValue, V extends IValue> Result<U> insertElement(ElementResult<V> valueResult, AbstractAST ast) {
		return addElement(valueResult, ast);
	}
	
	<U extends IValue, V extends IValue> Result<U> addElement(ElementResult<V> that, AbstractAST ast) {
		Type newType = getTypeFactory().setType(that.getType().lub(getType().getElementType()));
		return makeResult(newType, getValue().insert(that.getValue()));
	}

	<U extends IValue, V extends IValue> Result<U> removeElement(ElementResult<V> valueResult, AbstractAST ast) {
		return makeResult(type, getValue().delete(valueResult.getValue()));
	}

	@Override
	protected <U extends IValue> Result<U> equalToRelation(RelationResult that, AbstractAST ast) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToRelation(RelationResult that, AbstractAST ast) {
		return that.nonEqualityBoolean(this);
	}

	@Override
	protected <U extends IValue> Result<U> equalToSet(SetResult that, AbstractAST ast) {
		return that.equalityBoolean(this);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToSet(SetResult that, AbstractAST ast) {
		return that.nonEqualityBoolean(this);
	}
	
	
	
	@Override
	protected <U extends IValue> Result<U> lessThanSet(SetResult that, AbstractAST ast) {
		// note reversed args: we need that < this
		return bool(that.comparisonInts(this, ast) < 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualSet(SetResult that, AbstractAST ast) {
		// note reversed args: we need that <= this
		return bool(that.comparisonInts(this, ast) <= 0);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanSet(SetResult that, AbstractAST ast) {
		// note reversed args: we need that > this
		return bool(that.comparisonInts(this, ast) > 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualSet(SetResult that, AbstractAST ast) {
		// note reversed args: we need that >= this
		return bool(that.comparisonInts(this, ast) >= 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanRelation(RelationResult that, AbstractAST ast) {
		// note reversed args: we need that < this
		return bool(that.comparisonInts(this, ast) < 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualRelation(RelationResult that, AbstractAST ast) {
		// note reversed args: we need that <= this
		return bool(that.comparisonInts(this, ast) <= 0);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanRelation(RelationResult that, AbstractAST ast) {
		// note reversed args: we need that > this
		return bool(that.comparisonInts(this, ast) > 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualRelation(RelationResult that, AbstractAST ast) {
		// note reversed args: we need that >= this
		return bool(that.comparisonInts(this, ast) >= 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareSet(SetResult that, AbstractAST ast) {
		// Note reversed args.
		return makeIntegerResult(compareISets(that.getValue(), this.getValue(), ast));
	}
	
	@Override
	protected <U extends IValue> Result<U> compareRelation(RelationResult that, AbstractAST ast) {
		// Note reversed args.
		return makeIntegerResult(compareISets(that.getValue(), this.getValue(), ast));
	}
		
	private IBool iboolOf(boolean b) {
		return ValueFactoryFactory.getValueFactory().bool(b);
	}
	
}
