package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.EvaluatorContext;

public class SetOrRelationResult<T extends ISet> extends CollectionResult<T> {

	SetOrRelationResult(Type type, T value, EvaluatorContext ctx) {
		super(type, value, ctx);
	}

	protected <U extends IValue, V extends IValue> Result<U> elementOf(
			ElementResult<V> elementResult, EvaluatorContext ctx) {
				return bool(getValue().contains(elementResult.getValue()));
			}

	protected <U extends IValue, V extends IValue> Result<U> notElementOf(
			ElementResult<V> elementResult, EvaluatorContext ctx) {
				return bool(!getValue().contains(elementResult.getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> addSet(SetResult s, EvaluatorContext ctx) {
		return makeResult(type.lub(s.type), getValue().union(s.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> addRelation(RelationResult s, EvaluatorContext ctx) {
		return makeResult(type.lub(s.type), getValue().union(s.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> subtractSet(SetResult s, EvaluatorContext ctx) {
		// note the reverse subtract
		return makeResult(getType().lub(s.getType()), s.getValue().subtract(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> subtractRelation(RelationResult s,
			EvaluatorContext ctx) {
				// note the reverse subtract
				return makeResult(getType().lub(s.getType()), s.getValue().subtract(getValue()), ctx);
			}

	@Override
	protected <U extends IValue> Result<U> multiplyRelation(RelationResult that,
			EvaluatorContext ctx) {
				Type tupleType = getTypeFactory().tupleType(that.type.getElementType(), type.getElementType());
				// Note the reverse in .product
				return makeResult(getTypeFactory().relTypeFromTuple(tupleType), that.getValue().product(getValue()), ctx);
			}

	@Override
	protected <U extends IValue> Result<U> multiplySet(SetResult s, EvaluatorContext ctx) {
		Type tupleType = getTypeFactory().tupleType(s.type.getElementType(), type.getElementType());
		// Note the reverse in .product
		return makeResult(getTypeFactory().relTypeFromTuple(tupleType), s.getValue().product(getValue()), ctx);
	}


	
	@Override
	protected <U extends IValue> Result<U> intersectSet(SetResult s, EvaluatorContext ctx) {
		return makeResult(type.lub(s.type), getValue().intersect(s.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> intersectRelation(RelationResult s,
			EvaluatorContext ctx) {
				return makeResult(type.lub(s.type), getValue().intersect(s.getValue()), ctx);
			}

	@Override
	protected <U extends IValue, V extends IValue> Result<U> insertElement(
			ElementResult<V> valueResult, EvaluatorContext ctx) {
				return addElement(valueResult, ctx);
			}

	protected <U extends IValue, V extends IValue> Result<U> addElement(
			ElementResult<V> that, EvaluatorContext ctx) {
				Type newType = getTypeFactory().setType(that.getType().lub(getType().getElementType()));
				return makeResult(newType, getValue().insert(that.getValue()), ctx);
			}

	protected <U extends IValue, V extends IValue> Result<U> removeElement(
			ElementResult<V> valueResult, EvaluatorContext ctx) {
				return makeResult(type, getValue().delete(valueResult.getValue()), ctx);
			}

	@Override
	protected <U extends IValue> Result<U> equalToRelation(RelationResult that,
			EvaluatorContext ctx) {
				return that.equalityBoolean(this, ctx);
			}

	@Override
	protected <U extends IValue> Result<U> nonEqualToRelation(RelationResult that,
			EvaluatorContext ctx) {
				return that.nonEqualityBoolean(this);
			}

	@Override
	protected <U extends IValue> Result<U> equalToSet(SetResult that, EvaluatorContext ctx) {
		return that.equalityBoolean(this, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToSet(SetResult that, EvaluatorContext ctx) {
		return that.nonEqualityBoolean(this);
	}

	@Override
	protected <U extends IValue> Result<U> lessThanSet(SetResult that, EvaluatorContext ctx) {
		// note reversed args: we need that < this
		return bool(that.getValue().isSubsetOf(getValue()) && !that.getValue().isEqual(getValue()));
	}

	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualSet(SetResult that,
			EvaluatorContext ctx) {
				// note reversed args: we need that <= this
				return bool(that.getValue().isSubsetOf(getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> greaterThanSet(SetResult that, EvaluatorContext ctx) {
		// note reversed args: we need that > this
		return bool(getValue().isSubsetOf(that.getValue()) && !getValue().isEqual(that.getValue()));
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualSet(SetResult that,
			EvaluatorContext ctx) {
				// note reversed args: we need that >= this
				return bool(getValue().isSubsetOf(that.getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> lessThanRelation(RelationResult that,
			EvaluatorContext ctx) {
				// note reversed args: we need that < this
				return bool(that.getValue().isSubsetOf(getValue()) && !that.getValue().isEqual(getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualRelation(RelationResult that,
			EvaluatorContext ctx) {
				// note reversed args: we need that <= this
				return bool(that.getValue().isSubsetOf(getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> greaterThanRelation(RelationResult that,
			EvaluatorContext ctx) {
				// note reversed args: we need that > this
				return bool(getValue().isSubsetOf(that.getValue()) && !getValue().isEqual(that.getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualRelation(
			RelationResult that, EvaluatorContext ctx) {
				// note reversed args: we need that >= this
				return bool(getValue().isSubsetOf(that.getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> compareSet(SetResult that, EvaluatorContext ctx) {
		// Note reversed args.
		return makeIntegerResult(compareISets(that.getValue(), this.getValue(), ctx), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> compareRelation(RelationResult that,
			EvaluatorContext ctx) {
				// Note reversed args.
				return makeIntegerResult(compareISets(that.getValue(), this.getValue(), ctx), ctx);
			}



}
