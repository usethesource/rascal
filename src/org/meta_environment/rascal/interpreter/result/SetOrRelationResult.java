package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class SetOrRelationResult<T extends ISet> extends CollectionResult<T> {

	SetOrRelationResult(Type type, T value, IEvaluatorContext ctx) {
		super(type, value, ctx);
	}

	protected <U extends IValue, V extends IValue> Result<U> elementOf(
			ElementResult<V> elementResult, IEvaluatorContext ctx) {
				return bool(getValue().contains(elementResult.getValue()));
			}

	protected <U extends IValue, V extends IValue> Result<U> notElementOf(
			ElementResult<V> elementResult, IEvaluatorContext ctx) {
				return bool(!getValue().contains(elementResult.getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> addSet(SetResult s, IEvaluatorContext ctx) {
		return makeResult(type.lub(s.type), getValue().union(s.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> addRelation(RelationResult s, IEvaluatorContext ctx) {
		return makeResult(type.lub(s.type), getValue().union(s.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> subtractSet(SetResult s, IEvaluatorContext ctx) {
		// note the reverse subtract
		return makeResult(getType().lub(s.getType()), s.getValue().subtract(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> subtractRelation(RelationResult s,
			IEvaluatorContext ctx) {
				// note the reverse subtract
				return makeResult(getType().lub(s.getType()), s.getValue().subtract(getValue()), ctx);
			}

	@Override
	protected <U extends IValue> Result<U> multiplyRelation(RelationResult that,
			IEvaluatorContext ctx) {
				Type tupleType = getTypeFactory().tupleType(that.type.getElementType(), type.getElementType());
				// Note the reverse in .product
				return makeResult(getTypeFactory().relTypeFromTuple(tupleType), that.getValue().product(getValue()), ctx);
			}

	@Override
	protected <U extends IValue> Result<U> multiplySet(SetResult s, IEvaluatorContext ctx) {
		Type tupleType = getTypeFactory().tupleType(s.type.getElementType(), type.getElementType());
		// Note the reverse in .product
		return makeResult(getTypeFactory().relTypeFromTuple(tupleType), s.getValue().product(getValue()), ctx);
	}


	
	@Override
	protected <U extends IValue> Result<U> intersectSet(SetResult s, IEvaluatorContext ctx) {
		return makeResult(type.lub(s.type), getValue().intersect(s.getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> intersectRelation(RelationResult s,
			IEvaluatorContext ctx) {
				return makeResult(type.lub(s.type), getValue().intersect(s.getValue()), ctx);
			}

	@Override
	protected <U extends IValue, V extends IValue> Result<U> insertElement(
			ElementResult<V> valueResult, IEvaluatorContext ctx) {
				return addElement(valueResult, ctx);
			}

	protected <U extends IValue, V extends IValue> Result<U> addElement(
			ElementResult<V> that, IEvaluatorContext ctx) {
				Type newType = getTypeFactory().setType(that.getType().lub(getType().getElementType()));
				return makeResult(newType, getValue().insert(that.getValue()), ctx);
			}

	protected <U extends IValue, V extends IValue> Result<U> removeElement(
			ElementResult<V> valueResult, IEvaluatorContext ctx) {
				return makeResult(type, getValue().delete(valueResult.getValue()), ctx);
			}

	@Override
	protected <U extends IValue> Result<U> equalToRelation(RelationResult that,
			IEvaluatorContext ctx) {
				return that.equalityBoolean(this, ctx);
			}

	@Override
	protected <U extends IValue> Result<U> nonEqualToRelation(RelationResult that,
			IEvaluatorContext ctx) {
				return that.nonEqualityBoolean(this);
			}

	@Override
	protected <U extends IValue> Result<U> equalToSet(SetResult that, IEvaluatorContext ctx) {
		return that.equalityBoolean(this, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToSet(SetResult that, IEvaluatorContext ctx) {
		return that.nonEqualityBoolean(this);
	}

	@Override
	protected <U extends IValue> Result<U> lessThanSet(SetResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that < this
		return bool(that.getValue().isSubsetOf(getValue()) && !that.getValue().isEqual(getValue()));
	}

	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualSet(SetResult that,
			IEvaluatorContext ctx) {
				// note reversed args: we need that <= this
				return bool(that.getValue().isSubsetOf(getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> greaterThanSet(SetResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that > this
		return bool(getValue().isSubsetOf(that.getValue()) && !getValue().isEqual(that.getValue()));
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualSet(SetResult that,
			IEvaluatorContext ctx) {
				// note reversed args: we need that >= this
				return bool(getValue().isSubsetOf(that.getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> lessThanRelation(RelationResult that,
			IEvaluatorContext ctx) {
				// note reversed args: we need that < this
				return bool(that.getValue().isSubsetOf(getValue()) && !that.getValue().isEqual(getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualRelation(RelationResult that,
			IEvaluatorContext ctx) {
				// note reversed args: we need that <= this
				return bool(that.getValue().isSubsetOf(getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> greaterThanRelation(RelationResult that,
			IEvaluatorContext ctx) {
				// note reversed args: we need that > this
				return bool(getValue().isSubsetOf(that.getValue()) && !getValue().isEqual(that.getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualRelation(
			RelationResult that, IEvaluatorContext ctx) {
				// note reversed args: we need that >= this
				return bool(getValue().isSubsetOf(that.getValue()));
			}

	@Override
	protected <U extends IValue> Result<U> compareSet(SetResult that, IEvaluatorContext ctx) {
		// Note reversed args.
		return makeIntegerResult(compareISets(that.getValue(), this.getValue(), ctx), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> compareRelation(RelationResult that,
			IEvaluatorContext ctx) {
				// Note reversed args.
				return makeIntegerResult(compareISets(that.getValue(), this.getValue(), ctx), ctx);
			}



}
