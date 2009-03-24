package org.meta_environment.rascal.interpreter.result;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;

import org.meta_environment.rascal.ast.AbstractAST;

public class ValueResult extends ElementResult<IValue> {

	public ValueResult(Type type, IValue value) {
		super(type, value);
	}
	
	public ValueResult(Type type, IValue value, Iterator<Result<IValue>> iter) {
		super(type, value, iter);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, AbstractAST ast) {
		return that.equalToValue(this, ast);
	}
	

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, AbstractAST ast) {
		return equals(that, ast).negate(ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> that, AbstractAST ast) {
		// the default fall back implementation for IValue-based results
		// Note the use of runtime types here. 
		int result = getValue().getType().toString().compareTo(that.getValue().getType().toString());
		return makeIntegerResult(result);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToInteger(IntegerResult that, AbstractAST ast) {
		return equalityBoolean(that);
	}

	@Override
	protected <U extends IValue> Result<U> equalToReal(RealResult that, AbstractAST ast) {
		return equalityBoolean(that);
	}

	@Override
	protected <U extends IValue> Result<U> equalToString(StringResult that, AbstractAST ast) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToList(ListResult that, AbstractAST ast) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToSet(SetResult that, AbstractAST ast) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToMap(MapResult that, AbstractAST ast) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToNode(NodeResult that, AbstractAST ast) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToSourceLocation(SourceLocationResult that, AbstractAST ast) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToRelation(RelationResult that, AbstractAST ast) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToTuple(TupleResult that, AbstractAST ast) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToBool(BoolResult that, AbstractAST ast) {
		return equalityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToValue(ValueResult that, AbstractAST ast) {
		return that.equalityBoolean(this);
	}

	
	@Override
	protected <U extends IValue> Result<U> nonEqualToInteger(IntegerResult that, AbstractAST ast) {
		return nonEqualityBoolean(that);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that, AbstractAST ast) {
		return nonEqualityBoolean(that);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToString(StringResult that, AbstractAST ast) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToList(ListResult that, AbstractAST ast) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToSet(SetResult that, AbstractAST ast) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToMap(MapResult that, AbstractAST ast) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToNode(NodeResult that, AbstractAST ast) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToSourceLocation(SourceLocationResult that, AbstractAST ast) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToRelation(RelationResult that, AbstractAST ast) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToTuple(TupleResult that, AbstractAST ast) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToBool(BoolResult that, AbstractAST ast) {
		return nonEqualityBoolean(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToValue(ValueResult that, AbstractAST ast) {
		return nonEqualityBoolean(this);
	}

	
	
	@Override
	protected <U extends IValue> Result<U> compareInteger(IntegerResult that, AbstractAST ast) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareReal(RealResult that, AbstractAST ast) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareString(StringResult that, AbstractAST ast) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareBool(BoolResult that, AbstractAST ast) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareTuple(TupleResult that, AbstractAST ast) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareList(ListResult that, AbstractAST ast) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareSet(SetResult that, AbstractAST ast) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareMap(MapResult that, AbstractAST ast) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareRelation(RelationResult that, AbstractAST ast) {
		return typeCompare(that);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareSourceLocation(SourceLocationResult that, AbstractAST ast) {
		return typeCompare(that);
	}
	
	
	
	/* Utilities  */
	
	private <U extends IValue, V extends IValue> Result<U> typeCompare(Result<V> that) {
		int result = getType().toString().compareTo(that.getType().toString());
		return makeIntegerResult(result);
	}


}
