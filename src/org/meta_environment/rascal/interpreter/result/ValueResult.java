package org.meta_environment.rascal.interpreter.result;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedOperationError;

public class ValueResult extends ElementResult<IValue> {

	public ValueResult(Type type, IValue value, IEvaluatorContext ctx) {
		super(type, value, ctx);
	}
	
	public ValueResult(Type type, IValue value, Iterator<Result<IValue>> iter, IEvaluatorContext ctx) {
		super(type, value, iter, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, IEvaluatorContext ctx) {
		return that.equalToValue(this, ctx);
	}
	

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, IEvaluatorContext ctx) {
		return equals(that, ctx).negate(ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> that, IEvaluatorContext ctx) {
		// the default fall back implementation for IValue-based results
		// Note the use of runtime types here. 
		return dynamicCompare(getValue(), that.getValue(), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToInteger(IntegerResult that, IEvaluatorContext ctx) {
		return equalityBoolean(that, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> equalToReal(RealResult that, IEvaluatorContext ctx) {
		return equalityBoolean(that, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> equalToString(StringResult that, IEvaluatorContext ctx) {
		return equalityBoolean(that, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToList(ListResult that, IEvaluatorContext ctx) {
		return equalityBoolean(that, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToSet(SetResult that, IEvaluatorContext ctx) {
		return equalityBoolean(that, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToMap(MapResult that, IEvaluatorContext ctx) {
		return equalityBoolean(that, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToNode(NodeResult that, IEvaluatorContext ctx) {
		return equalityBoolean(that, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToSourceLocation(SourceLocationResult that, IEvaluatorContext ctx) {
		return equalityBoolean(that, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToRelation(RelationResult that, IEvaluatorContext ctx) {
		return equalityBoolean(that, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToTuple(TupleResult that, IEvaluatorContext ctx) {
		return equalityBoolean(that, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToBool(BoolResult that, IEvaluatorContext ctx) {
		return equalityBoolean(that, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToValue(ValueResult that, IEvaluatorContext ctx) {
		return that.equalityBoolean(this, ctx);
	}

	
	@Override
	protected <U extends IValue> Result<U> nonEqualToInteger(IntegerResult that, IEvaluatorContext ctx) {
		return nonEqualityBoolean(that);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that, IEvaluatorContext ctx) {
		return nonEqualityBoolean(that);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToString(StringResult that, IEvaluatorContext ctx) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToList(ListResult that, IEvaluatorContext ctx) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToSet(SetResult that, IEvaluatorContext ctx) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToMap(MapResult that, IEvaluatorContext ctx) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToNode(NodeResult that, IEvaluatorContext ctx) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToSourceLocation(SourceLocationResult that, IEvaluatorContext ctx) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToRelation(RelationResult that, IEvaluatorContext ctx) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToTuple(TupleResult that, IEvaluatorContext ctx) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToBool(BoolResult that, IEvaluatorContext ctx) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToValue(ValueResult that, IEvaluatorContext ctx) {
		return nonEqualityBoolean(this);
	}

	
	
	@Override
	protected <U extends IValue> Result<U> compareInteger(IntegerResult that, IEvaluatorContext ctx) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareReal(RealResult that, IEvaluatorContext ctx) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareString(StringResult that, IEvaluatorContext ctx) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareBool(BoolResult that, IEvaluatorContext ctx) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareTuple(TupleResult that, IEvaluatorContext ctx) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareList(ListResult that, IEvaluatorContext ctx) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareSet(SetResult that, IEvaluatorContext ctx) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareMap(MapResult that, IEvaluatorContext ctx) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareRelation(RelationResult that, IEvaluatorContext ctx) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareSourceLocation(SourceLocationResult that, IEvaluatorContext ctx) {
		return typeCompare(that);
	}
	
	
	
	/* Utilities  */
	
	private <U extends IValue, V extends IValue> Result<U> typeCompare(Result<V> that) {
		int result = getType().toString().compareTo(that.getType().toString());
		return makeIntegerResult(result, null);
	}
	
	private <U extends IValue> Result<U> dynamicCompare(IValue a, IValue b, IEvaluatorContext ctx) {
		// Since equals and compare must be total on all values, we are allowed
		// to lift dynamic types here to static types. This makes the dynamic compare the 
		// same as the static compare (to prevent surprises). However, if the static types
		// do not implement a comparison (like [] == 1 compares a list to an int), 
		// then we fall back to the comparison of type names.
		try {
			Result<?> aResult = ResultFactory.makeResult(a.getType(), a, ctx);
			Result<?> bResult = ResultFactory.makeResult(b.getType(), b, ctx);
			return aResult.compare(bResult, ctx);
		}
		catch (UnsupportedOperationError e) {
			return makeIntegerResult(a.getType().toString().compareTo(b.getType().toString()), ctx);
		}
	}
}
