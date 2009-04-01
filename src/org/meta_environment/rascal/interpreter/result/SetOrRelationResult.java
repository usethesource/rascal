package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;

public class SetOrRelationResult<T extends ISet> extends CollectionResult<T> {

	SetOrRelationResult(Type type, T value) {
		super(type, value);
	}

	protected <U extends IValue, V extends IValue> Result<U> elementOf(
			ElementResult<V> elementResult, AbstractAST ast) {
				return bool(getValue().contains(elementResult.getValue()));
			}

	protected <U extends IValue, V extends IValue> Result<U> notElementOf(
			ElementResult<V> elementResult, AbstractAST ast) {
				return bool(!getValue().contains(elementResult.getValue()));
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
	protected <U extends IValue> Result<U> subtractRelation(RelationResult s,
			AbstractAST ast) {
				// note the reverse subtract
				return makeResult(getType().lub(s.getType()), s.getValue().subtract(getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> multiplyRelation(RelationResult that,
			AbstractAST ast) {
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
	protected <U extends IValue> Result<U> intersectRelation(RelationResult s,
			AbstractAST ast) {
				return makeResult(type.lub(s.type), getValue().intersect(s.getValue()));
			}

	@Override
	protected <U extends IValue, V extends IValue> Result<U> insertElement(
			ElementResult<V> valueResult, AbstractAST ast) {
				return addElement(valueResult, ast);
			}

	protected <U extends IValue, V extends IValue> Result<U> addElement(
			ElementResult<V> that, AbstractAST ast) {
				Type newType = getTypeFactory().setType(that.getType().lub(getType().getElementType()));
				return makeResult(newType, getValue().insert(that.getValue()));
			}

	protected <U extends IValue, V extends IValue> Result<U> removeElement(
			ElementResult<V> valueResult, AbstractAST ast) {
				return makeResult(type, getValue().delete(valueResult.getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> equalToRelation(RelationResult that,
			AbstractAST ast) {
				return that.equalityBoolean(this, ast);
			}

	@Override
	protected <U extends IValue> Result<U> nonEqualToRelation(RelationResult that,
			AbstractAST ast) {
				return that.nonEqualityBoolean(this);
			}

	@Override
	protected <U extends IValue> Result<U> equalToSet(SetResult that, AbstractAST ast) {
		return that.equalityBoolean(this, ast);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToSet(SetResult that, AbstractAST ast) {
		return that.nonEqualityBoolean(this);
	}

	@Override
	protected <U extends IValue> Result<U> lessThanSet(SetResult that, AbstractAST ast) {
		// note reversed args: we need that < this
		return bool(that.getValue().isSubsetOf(getValue()) && !that.getValue().isEqual(getValue()));
	}

	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualSet(SetResult that,
			AbstractAST ast) {
				// note reversed args: we need that <= this
				return bool(that.getValue().isSubsetOf(getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> greaterThanSet(SetResult that, AbstractAST ast) {
		// note reversed args: we need that > this
		return bool(getValue().isSubsetOf(that.getValue()) && !getValue().isEqual(that.getValue()));
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualSet(SetResult that,
			AbstractAST ast) {
				// note reversed args: we need that >= this
				return bool(getValue().isSubsetOf(that.getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> lessThanRelation(RelationResult that,
			AbstractAST ast) {
				// note reversed args: we need that < this
				return bool(that.getValue().isSubsetOf(getValue()) && !that.getValue().isEqual(getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualRelation(RelationResult that,
			AbstractAST ast) {
				// note reversed args: we need that <= this
				return bool(that.getValue().isSubsetOf(getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> greaterThanRelation(RelationResult that,
			AbstractAST ast) {
				// note reversed args: we need that > this
				return bool(getValue().isSubsetOf(that.getValue()) && !getValue().isEqual(that.getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualRelation(
			RelationResult that, AbstractAST ast) {
				// note reversed args: we need that >= this
				return bool(getValue().isSubsetOf(that.getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> compareSet(SetResult that, AbstractAST ast) {
		// Note reversed args.
		return makeIntegerResult(compareISets(that.getValue(), this.getValue(), ast));
	}

	@Override
	protected <U extends IValue> Result<U> compareRelation(RelationResult that,
			AbstractAST ast) {
				// Note reversed args.
				return makeIntegerResult(compareISets(that.getValue(), this.getValue(), ast));
			}



}
