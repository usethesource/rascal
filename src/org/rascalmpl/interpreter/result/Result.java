/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Emilie Balland - (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.rascalmpl.ast.Field;
import org.rascalmpl.ast.Name;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperation;
import org.rascalmpl.interpreter.utils.LimitedResultWriter;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

// TODO: perhaps move certain stuff down to ValueResult (or merge that class with this one).

public abstract class Result<T extends IValue> implements Iterator<Result<IValue>>, IRascalResult {
	private static final String INTERSECTION_STRING = "intersection";
	private static final String NOTIN_STRING = "notin";
	private static final String IN_STRING = "in";
	private static final String TRANSITIVE_REFLEXIVE_CLOSURE_STRING = "transitive-reflexive closure";
	private static final String TRANSITIVE_CLOSURE_STRING = "transitive closure";
	private static final String NEGATIVE_STRING = "negative";
	private static final String MODULO_STRING = "modulo";
	private static final String DIVISION_STRING = "division";
	private static final String MULTIPLICATION_STRING = "multiplication";
	private static final String SUBTRACTION_STRING = "subtraction";
	private static final String ADDITION_STRING = "addition";
	private static final String IS_STRING = "is";
	private static final String HAS_STRING = "has";
	private static final String IS_DEFINED_STRING = "?";
	private static final String FIELD_ACCESS_STRING = "field access";
	private static final String FIELD_UPDATE_STRING = "field update";
	private static final String RANGE_STRING = "range construction";
	private static final String SUBSCRIPT_STRING = "subscript";
	private static final String SLICE_STRING = "slice";
	private static final String MAKE_SLICE_STRING = "make slice";
	private static final String GET_ANNO_STRING = "get-annotation";
	private static final String SET_ANNO_STRING = "set-annotation";
	private static final String NON_EQUALS_STRING = "inequality";
	private static final String LESS_THAN_STRING = "<";
	private static final String GREATER_THAN_OR_EQUAL_STRING = ">=";
	private static final String GREATER_THAN_STRING = ">";
	private static final String IF_THEN_ELSE_STRING = "if-then-else";
	private static final String COMPOSE_STRING = "composition";
	private static final String NEGATE_STRING = "negation";
	private static final String JOIN_STRING = "join";
	private static final String FIELD_SELECT_STRING = "field selection";
	private static final String REMAINDER_STRING = "remainder";
	
	private Iterator<Result<IValue>> iterator = null;
	protected final Type type;
	protected T value;
	private boolean inferredType = false;
	protected IEvaluatorContext ctx;

	protected Result(Type type, T value, Iterator<Result<IValue>> iter, IEvaluatorContext ctx) {
		// Check for null in case of void result or uninit.
		if (value != null && !value.getType().isSubtypeOf(type)) {
			throw new UnexpectedType(type, value.getType(), ctx.getCurrentAST());
		}
	
	    this.type = type;
		this.iterator = iter;
		this.value = value;
		this.ctx = ctx;
	}
	
	protected Result(Type type, T value, IEvaluatorContext ctx) {
		this(type, value, null, ctx);
	}
	
	public IEvaluatorContext getEvaluatorContext() {
		return ctx;
	}

	/// The "result" interface
	
	@Override
	public T getValue() {
		return value;
	}

	@Override
	public Type getStaticType() { 
		return type;
	}
	
	@Override
	public String toString(){
		return getStaticType().toString() + ": " + value.toString();
	}
	
	public String toString(int length){
		StandardTextWriter stw = new StandardTextWriter(true);
		LimitedResultWriter lros = new LimitedResultWriter(length);
		
		try {
			stw.write(getValue(), lros);
		}
		catch (/*IOLimitReachedException*/ RuntimeException iolrex){
			// This is fine, ignore.
		}
		catch (IOException ioex) {
			// This can never happen.
		}
		
		return getStaticType().toString() + ": " + lros.toString();
	}
	
	protected <U extends IValue> Result<U> makeIntegerResult(int i) {
		return makeResult(getTypeFactory().integerType(), getValueFactory().integer(i), ctx);
	}
	
	protected TypeFactory getTypeFactory() {
		return TypeFactory.getInstance();
	}
	
	protected IValueFactory getValueFactory() {
		return ctx.getValueFactory();
	}
	
	//////// The iterator interface
	
	@Override
	public boolean hasNext(){
		return iterator != null && iterator.hasNext();
	}
	
	@Override
	public Result<IValue> next(){
		if(iterator == null){
			throw new ImplementationError("next called on Result with null iterator");
		}
		return iterator.next(); //??? last = iterator.next();
	}

	@Override
	public void remove() {
		throw new ImplementationError("remove() not implemented for (iterable) result");		
	}
	
	// Error aux methods
	
	protected <U extends IValue> Result<U> undefinedError(String operator) {
		throw new UnsupportedOperation(operator, getStaticType(), ctx.getCurrentAST());
	}
	
	protected <U extends IValue> Result<U> undefinedError(String operator, Result<?> arg) {
		throw new UnsupportedOperation(operator, getStaticType(), arg.getStaticType(), ctx.getCurrentAST());
	}
	
	///////
	
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) throws MatchFailed {
    throw new UnsupportedOperation("A value of type " + getStaticType() + " is not something you can call like a function, a constructor or a closure.", ctx.getCurrentAST());
  }
	
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> that) {
		return undefinedError(ADDITION_STRING, that);
	}

	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> that) {
		return undefinedError(SUBTRACTION_STRING, that);
	}

	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> that) {
		return undefinedError(MULTIPLICATION_STRING, that);
	}
	
	public <U extends IValue, V extends IValue> Result<U> join(Result<V> that) {
		return undefinedError(JOIN_STRING, that);
	}
	
	public <U extends IValue, V extends IValue> Result<U> divide(Result<V> that) {
		return undefinedError(DIVISION_STRING, that);
	}

	public <U extends IValue, V extends IValue> Result<U> modulo(Result<V> that) {
		return undefinedError(MODULO_STRING, that);
	}
	
	public <U extends IValue, V extends IValue> Result<U> remainder(Result<V> that) {
		return undefinedError(REMAINDER_STRING, that);
	}

	public <V extends IValue> Result<IBool> in(Result<V> that) {
		return undefinedError(IN_STRING, that);
	}

	public <V extends IValue> Result<IBool> notIn(Result<V> that) {
		return undefinedError(NOTIN_STRING, that);
	}
	
	public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right) {
		return undefinedError(COMPOSE_STRING, right);
	}
	
	public <U extends IValue> Result<U> negative() {
		return undefinedError(NEGATIVE_STRING);
	}

	public Result<IBool> negate() {
		return undefinedError(NEGATE_STRING);
	}

	public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> that) {
		return undefinedError(INTERSECTION_STRING, this);
	}
	
	public <U extends IValue, V extends IValue> Result<U> makeRange(Result<V> to) {
		return undefinedError(RANGE_STRING);
	}

	public <V extends IValue> Result<IBool> equals(Result<V> that) {
	  // equals is defined everywhere, but default is false
		return ResultFactory.bool(false, ctx);
	}
	
	public <V extends IValue> Result<IBool> lessThan(Result<V> that) {
		return lessThanOrEqual(that).isLess();
	}
	
	public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> that) {
		return new LessThanOrEqualResult(false, false, ctx);
	}

	public <V extends IValue> Result<IBool> greaterThan(Result<V> that) {
		return undefinedError(GREATER_THAN_STRING, that);
	}
	
	public <V extends IValue> Result<IBool> greaterThanOrEqual(Result<V> that) {
		return undefinedError(GREATER_THAN_OR_EQUAL_STRING, that);
	}
	
	public Result<IValue> ifThenElse(Result<IValue> then, Result<IValue> _else) {
		return undefinedError(IF_THEN_ELSE_STRING);
	}
	
	public <U extends IValue> Result<U> transitiveClosure() {
		return undefinedError(TRANSITIVE_CLOSURE_STRING);
	}

	public <U extends IValue> Result<U> transitiveReflexiveClosure() {
		return undefinedError(TRANSITIVE_REFLEXIVE_CLOSURE_STRING);
	}
	
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store) {
		return undefinedError(FIELD_ACCESS_STRING);
	}
	
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store) {
		return undefinedError(FIELD_UPDATE_STRING);
	}
	
	public <U extends IValue, V extends IValue, W extends IValue> Result<U> makeStepRange(Result<V> to, Result<W> second) {
		return undefinedError(RANGE_STRING);
	}
	
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts) {
		return undefinedError(SUBSCRIPT_STRING);
	}
	
	public <U extends IValue, V extends IValue> Result<U> slice(Result<?> first, Result<?> second, Result<?> last) {
		return undefinedError(SLICE_STRING);
	}
	
	public  <U extends IValue, V extends IValue> Result<U> makeSlice(int firstIndex, int secondIndex, int endIndex) {
		return undefinedError(MAKE_SLICE_STRING);
	}

	public <U extends IValue> Result<U> getAnnotation(String annoName, Environment env) {
		return undefinedError(GET_ANNO_STRING);
	}	

	public <U extends IValue, V extends IValue> Result<U> setAnnotation(String annoName, Result<V> anno,
			Environment peek) {
		return undefinedError(SET_ANNO_STRING);
	}
	
	public <V extends IValue> Result<IBool> nonEquals(Result<V> that) { 
		return bool(true, ctx);
	}
	
	public boolean isTrue() {
		return false;
	}
	
	public boolean isVoid() {
		return false;
	}
	
	///////
	
	protected <U extends IValue, V extends IValue> Result<U> insertElement(Result<V> that) {
		return that.undefinedError("insert into collection", this);
	}

	
	protected <U extends IValue> Result<U> addInteger(IntegerResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> Result<U> subtractInteger(IntegerResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> Result<U> multiplyInteger(IntegerResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}

	protected <U extends IValue> Result<U> addReal(ElementResult<IReal> that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> Result<U> subtractReal(ElementResult<IReal> that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> Result<U> multiplyReal(ElementResult<IReal> that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}
	
	protected <U extends IValue> Result<U> divideReal(ElementResult<IReal> that) {
		return that.undefinedError(DIVISION_STRING, this);
	}

	protected <U extends IValue> Result<U> divideInteger(IntegerResult that) {
		return that.undefinedError(DIVISION_STRING, this);
	}

	protected <U extends IValue> Result<U> addRational(RationalResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> Result<U> subtractRational(RationalResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> Result<U> multiplyRational(RationalResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}
	
	protected <U extends IValue> Result<U> divideRational(RationalResult that) {
		return that.undefinedError(DIVISION_STRING, this);
	}

	protected <U extends IValue> Result<U> addString(StringResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> Result<U> addList(ListResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> Result<U> subtractList(ListResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> Result<U> multiplyList(ListResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, that);
	}
	
	protected <U extends IValue> Result<U> intersectList(ListResult that) {
		return that.undefinedError(INTERSECTION_STRING, that);
	}

	protected <U extends IValue> Result<U> addSet(SetResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> Result<U> addRelation(RelationResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}
	
	protected <U extends IValue> Result<U> addListRelation(ListRelationResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> Result<U> addBool(BoolResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}
	
	public Result<IValue> fieldSelect(int[] selectedFields) {
		return undefinedError(FIELD_SELECT_STRING, this);
	}
	
	public Result<IValue> fieldSelect(Field[] selectedFields) {
		return undefinedError(FIELD_SELECT_STRING, this);
	}
	
	protected <U extends IValue> Result<U> subtractSet(SetResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> Result<U> multiplySet(SetResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}
	
	protected <U extends IValue> Result<U> joinSet(SetResult that) {
		return that.undefinedError(JOIN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> joinList(ListResult that) {
		return that.undefinedError(JOIN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> multiplyRelation(RelationResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}
	
	protected <U extends IValue> Result<U> multiplyListRelation(ListRelationResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}

	protected <U extends IValue> Result<U> joinRelation(RelationResult that) {
		return that.undefinedError(JOIN_STRING, this);
	}
	protected <U extends IValue> Result<U> joinListRelation(ListRelationResult that) {
		return that.undefinedError(JOIN_STRING, this);
	}

	protected <U extends IValue> Result<U> addMap(MapResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> Result<U> subtractRelation(RelationResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}
	
	protected <U extends IValue> Result<U> subtractListRelation(ListRelationResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> Result<U> subtractMap(MapResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> Result<U> moduloReal(ElementResult<IReal> that) {
		return that.undefinedError(MODULO_STRING, this);
	}
	
	protected <U extends IValue> Result<U> remainderReal(ElementResult<IReal> that) {
		return that.undefinedError(REMAINDER_STRING, this);
	}

	protected <U extends IValue> Result<U> addTuple(TupleResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> Result<U> moduloInteger(IntegerResult that) {
		return that.undefinedError(MODULO_STRING, this);
	}
	
	protected <U extends IValue> Result<U> remainderInteger(IntegerResult that) {
		return that.undefinedError(REMAINDER_STRING, this);
	}

	
	protected <U extends IValue> Result<U> intersectSet(SetResult that) {
		return that.undefinedError(INTERSECTION_STRING, this);
	}

	protected <U extends IValue> Result<U> intersectRelation(RelationResult that) {
		return that.undefinedError(INTERSECTION_STRING, this);
	}
	
	protected <U extends IValue> Result<U> intersectListRelation(ListRelationResult that) {
		return that.undefinedError(INTERSECTION_STRING, this);
	}

	protected <U extends IValue> Result<U> intersectMap(MapResult that) {
		return that.undefinedError(INTERSECTION_STRING, this);
	}

	protected <U extends IValue> Result<U> makeRangeFromInteger(IntegerResult that) {
		return that.undefinedError(RANGE_STRING, this);
	}

	protected <U extends IValue> Result<U> makeRangeFromRational(RationalResult that) {
		return that.undefinedError(RANGE_STRING, this);
	}

	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromInteger(IntegerResult that, Result<V> step) {
		return that.undefinedError(RANGE_STRING, this);
	}

	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromReal(ElementResult<IReal> that, Result<V> step) {
		return that.undefinedError(RANGE_STRING, this);
	}

	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromRational(RationalResult that, Result<V> step) {
		return that.undefinedError(RANGE_STRING, this);
	}

	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromNumber(NumberResult that, Result<V> step) {
		return that.undefinedError(RANGE_STRING, this);
	}

	
	protected <U extends IValue> Result<U> makeRangeFromNumber(NumberResult that) {
		return that.undefinedError(RANGE_STRING, this);
	}

	protected <U extends IValue> Result<U> makeRangeFromReal(ElementResult<IReal> that) {
		return that.undefinedError(RANGE_STRING, this);
	}

	protected Result<IBool> inSet(SetResult that) {
		return that.undefinedError(IN_STRING, this);
	}

	protected Result<IBool> inRelation(RelationResult that) {
		return that.undefinedError(IN_STRING, this);
	}

	protected Result<IBool> inListRelation(ListRelationResult that) {
		return that.undefinedError(IN_STRING, this);
	}

	protected Result<IBool> inList(ListResult that) {
		return that.undefinedError(IN_STRING, this);
	} 

	protected Result<IBool> inMap(MapResult that) {
		return that.undefinedError(IN_STRING, this);
	}

	protected Result<IBool> notInSet(SetResult that) {
		return that.undefinedError(NOTIN_STRING, this);
	}

	protected Result<IBool> notInRelation(RelationResult that) {
		return that.undefinedError(NOTIN_STRING, this);
	}
	
	protected Result<IBool> notInListRelation(ListRelationResult that) {
		return that.undefinedError(NOTIN_STRING, this);
	}
	
	protected Result<IBool> notInList(ListResult that) {
		return that.undefinedError(NOTIN_STRING, this);
	}
	
	protected Result<IBool> notInMap(MapResult that) {
		return that.undefinedError(NOTIN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> composeRelation(RelationResult that) {
		return that.undefinedError(COMPOSE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> composeListRelation(ListRelationResult that) {
		return that.undefinedError(COMPOSE_STRING, this);
	}
	
	public <U extends IValue> Result<U> composeMap(MapResult that) {
		return that.undefinedError(COMPOSE_STRING, this);
	}
	
	public <U extends IValue> Result<U> addFunctionNonDeterministic(AbstractFunction that) {
		return that.undefinedError(ADDITION_STRING, this);
	}
	
	public <U extends IValue> Result<U> addFunctionNonDeterministic(OverloadedFunction that) {
		return that.undefinedError(ADDITION_STRING, this);
	}
	
	public <U extends IValue> Result<U> addFunctionNonDeterministic(ComposedFunctionResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}
	
	public <U extends IValue> Result<U> composeFunction(AbstractFunction that) {
		return that.undefinedError(COMPOSE_STRING, this);
	}
	
	public <U extends IValue> Result<U> composeFunction(OverloadedFunction that) {
		return that.undefinedError(COMPOSE_STRING, this);
	}
	
	public <U extends IValue> Result<U> composeFunction(ComposedFunctionResult that) {
		return that.undefinedError(COMPOSE_STRING, this);
	}
	
	protected Result<IBool> equalToInteger(IntegerResult that) {
		return bool(false, ctx);
	}
	
	protected Result<IBool> equalToRational(RationalResult that) {
		return bool(false, ctx);
	}
	
	protected Result<IBool> equalToReal(RealResult that) {
		return bool(false, ctx);
	}
	
	protected Result<IBool> equalToString(StringResult that) {
		return bool(false, ctx);
	}
	
	protected Result<IBool> equalToList(ListResult that) {
		return bool(false, ctx);
	}
	
	protected Result<IBool> equalToListRelation(ListRelationResult that) {
        return bool(false, ctx);
    }
	
	protected Result<IBool> equalToSet(SetResult that) {
		return bool(false, ctx);
	}
	
	protected Result<IBool> equalToMap(MapResult that) {
		return bool(false, ctx);
	}
	
	protected Result<IBool> equalToNode(NodeResult that) {
		return bool(false, ctx);
	}
	
	protected Result<IBool> equalToConcreteSyntax(ConcreteSyntaxResult that) {
		return bool(false, ctx);
	}
	
	protected Result<IBool> equalToSourceLocation(SourceLocationResult that) {
		return bool(false, ctx);
	}
	
	protected Result<IBool> equalToRelation(RelationResult that) {
		return bool(false, ctx);
	}
	
	protected Result<IBool> equalToTuple(TupleResult that) {
		return bool(false, ctx);
	}
	
	protected Result<IBool> equalToBool(BoolResult that) {
		return bool(false, ctx);
	}
	
	protected Result<IBool> equalToValue(ValueResult that) {
		return ResultFactory.bool(false, ctx);
	}
	
	public Result<IBool> equalToOverloadedFunction(OverloadedFunction that) {
		return bool(false, ctx);
	}
	
	public Result<IBool> equalToConstructorFunction(ConstructorFunction that) {
		return bool(false, ctx);
	}
	
	public Result<IBool> equalToRascalFunction(RascalFunction that) {
		return bool(false, ctx);
	}

	protected Result<IBool> equalToDateTime(DateTimeResult that) {
		return bool(false, ctx);
	}
	
	protected Result<IBool> nonEqualToInteger(IntegerResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToRational(RationalResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToReal(RealResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToString(StringResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToList(ListResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToListRelation(ListRelationResult that) {
	  return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToSet(SetResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToMap(MapResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToNode(NodeResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToConcreteSyntax(ConcreteSyntaxResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToSourceLocation(SourceLocationResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToRelation(RelationResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToTuple(TupleResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToBool(BoolResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToValue(ValueResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> nonEqualToDateTime(DateTimeResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected Result<IBool> lessThanInteger(IntegerResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> LessThanOrEqualResult lessThanOrEqualInteger(IntegerResult that) {
    return new LessThanOrEqualResult(false, false, ctx);
	}

	protected Result<IBool> greaterThanInteger(IntegerResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected Result<IBool> greaterThanOrEqualInteger(IntegerResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected Result<IBool> lessThanRational(RationalResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> LessThanOrEqualResult lessThanOrEqualRational(RationalResult that) {
    return new LessThanOrEqualResult(false, false, ctx);
	}

	protected Result<IBool> greaterThanRational(RationalResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected Result<IBool> greaterThanOrEqualRational(RationalResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected Result<IBool> lessThanReal(RealResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected LessThanOrEqualResult lessThanOrEqualReal(ElementResult<IReal> that) {
		return new LessThanOrEqualResult(false, false, ctx);
	}

	protected Result<IBool> greaterThanReal(ElementResult<IReal> that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected Result<IBool> greaterThanOrEqualReal(ElementResult<IReal> that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}

	protected Result<IBool> lessThanString(StringResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected LessThanOrEqualResult lessThanOrEqualString(StringResult that) {
		return new LessThanOrEqualResult(false, false, ctx);
	}

	protected Result<IBool> greaterThanString(StringResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected Result<IBool> greaterThanOrEqualString(StringResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}

	protected Result<IBool> lessThanTuple(TupleResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected LessThanOrEqualResult lessThanOrEqualTuple(TupleResult that) {
		return new LessThanOrEqualResult(false, false, ctx);
	}

	protected Result<IBool> greaterThanTuple(TupleResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected Result<IBool> greaterThanOrEqualTuple(TupleResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected Result<IBool> lessThanNode(NodeResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected LessThanOrEqualResult lessThanOrEqualNode(NodeResult that) {
		return new LessThanOrEqualResult(false, false, ctx);
	}

	protected Result<IBool> greaterThanNode(NodeResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected Result<IBool> greaterThanOrEqualNode(NodeResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected Result<IBool> lessThanBool(BoolResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected LessThanOrEqualResult lessThanOrEqualBool(BoolResult that) {
		return new LessThanOrEqualResult(false, false, ctx);
	}

	protected Result<IBool> greaterThanBool(BoolResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected Result<IBool> greaterThanOrEqualBool(BoolResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected Result<IBool> lessThanMap(MapResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> LessThanOrEqualResult lessThanOrEqualMap(MapResult that) {
		return new LessThanOrEqualResult(false, false, ctx);
	}

	protected Result<IBool> greaterThanMap(MapResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected Result<IBool> greaterThanOrEqualMap(MapResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected Result<IBool> lessThanSet(SetResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> LessThanOrEqualResult lessThanOrEqualSet(SetResult that) {
    return new LessThanOrEqualResult(false, false, ctx);
	}

	protected Result<IBool> greaterThanSet(SetResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected Result<IBool> greaterThanOrEqualSet(SetResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected Result<IBool> lessThanRelation(RelationResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	

	protected <U extends IValue> LessThanOrEqualResult lessThanOrEqualRelation(RelationResult that) {
    return new LessThanOrEqualResult(false, false, ctx);
  }
  
	protected Result<IBool> lessThanListRelation(ListRelationResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected LessThanOrEqualResult lessThanOrEqualListRelation(ListRelationResult that) {
		return new LessThanOrEqualResult(false, false, ctx);
	}

	protected Result<IBool> greaterThanRelation(RelationResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected Result<IBool> greaterThanListRelation(ListRelationResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected Result<IBool> greaterThanOrEqualRelation(RelationResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected Result<IBool> greaterThanOrEqualListRelation(ListRelationResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected Result<IBool> lessThanList(ListResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> LessThanOrEqualResult lessThanOrEqualList(ListResult that) {
    return new LessThanOrEqualResult(false, false, ctx);
	}

	protected Result<IBool> greaterThanList(ListResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected Result<IBool> greaterThanOrEqualList(ListResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	// Comparison on DateTime

	protected Result<IBool> lessThanDateTime(DateTimeResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> LessThanOrEqualResult lessThanOrEqualDateTime(DateTimeResult that) {
    return new LessThanOrEqualResult(false, false, ctx);
	}

	protected <U extends IValue>Result<IBool> greaterThanDateTime(DateTimeResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected Result<IBool> greaterThanOrEqualDateTime(DateTimeResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	// Comparison on SourceLocation
	
	protected Result<IBool> lessThanSourceLocation(SourceLocationResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> LessThanOrEqualResult lessThanOrEqualSourceLocation(SourceLocationResult that) {
    return new LessThanOrEqualResult(false, false, ctx);
	}
	
	protected Result<IBool> greaterThanSourceLocation(SourceLocationResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected Result<IBool> greaterThanOrEqualSourceLocation(SourceLocationResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	
	protected <U extends IValue> Result<U> addNumber(NumberResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> Result<U> multiplyNumber(NumberResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}

	protected <U extends IValue> Result<U> divideNumber(NumberResult that) {
		return that.undefinedError(DIVISION_STRING, this);
	}

	protected <U extends IValue> Result<U> subtractNumber(NumberResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected Result<IBool> equalToNumber(NumberResult that) {
		return bool(false, ctx);
	}

	protected Result<IBool> nonEqualToNumber(NumberResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}

	protected Result<IBool> lessThanNumber(NumberResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}

	protected <U extends IValue> LessThanOrEqualResult lessThanOrEqualNumber(NumberResult that) {
    return new LessThanOrEqualResult(false, false, ctx);
	}

	protected Result<IBool> greaterThanNumber(NumberResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}

	protected Result<IBool> greaterThanOrEqualNumber(NumberResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> addSourceLocation(
			SourceLocationResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}
	
	public Result<IBool> is(Name name) {
		return undefinedError(IS_STRING, this);
	}
	
	public Result<IBool> has(Name name) {
		return undefinedError(HAS_STRING, this);
	}
	
	public Result<IBool> isDefined(Name name) {
		return undefinedError(IS_DEFINED_STRING, this);
	}
	
	public void setInferredType(boolean inferredType) {
		this.inferredType  = inferredType;
	}
	
	public boolean hasInferredType() {
		return inferredType;
	}
	
	protected <U extends IValue> Result<U> addDateTime(
			DateTimeResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}
	
	protected <U extends IValue> Result<U> subtractDateTime(
			DateTimeResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	public Type getKeywordArgumentTypes(Environment env) {
		return TypeFactory.getInstance().voidType();
	}

	public Result<IBool> isKeyDefined(Result<?>[] subscripts) {
		return bool(false, ctx);
	}
}
