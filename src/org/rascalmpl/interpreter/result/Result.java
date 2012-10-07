/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.Field;
import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperationError;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.utils.LimitedResultWriter;
import org.rascalmpl.interpreter.utils.LimitedResultWriter.IOLimitReachedException;
import org.rascalmpl.values.uptr.Factory;

// TODO: perhaps move certain stuff down to ValueResult (or merge that class with this one).

public abstract class Result<T extends IValue> implements Iterator<Result<IValue>> {
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
	private static final String FIELD_ACCESS_STRING = "field access";
	private static final String EQUALS_STRING = "equality";
	private static final String COMPARE_STRING = "comparison";
	private static final String FIELD_UPDATE_STRING = "field update";
	private static final String RANGE_STRING = "range construction";
	private static final String SUBSCRIPT_STRING = "subscript";
	private static final String GET_ANNO_STRING = "get-annotation";
	private static final String SET_ANNO_STRING = "set-annotation";
	private static final String NON_EQUALS_STRING = "inequality";
	private static final String LESS_THAN_OR_EQUAL_STRING = "<=";
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
	private boolean isPublic;
	private boolean inferredType = false;
	protected IEvaluatorContext ctx;

	protected Result(Type type, T value, Iterator<Result<IValue>> iter, IEvaluatorContext ctx) {
		// Check for null in case of void result or uninit.
		if (value != null && !value.getType().isSubtypeOf(type) && !(type instanceof NonTerminalType && value.getType() == Factory.Tree)) {
			//System.err.println(value.getType());
			//System.err.println(type); 
			//System.err.println(value.getType().isSubtypeOf(type));
			throw new UnexpectedTypeError(type, value.getType(), ctx.getCurrentAST());
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
	
	public T getValue() {
		return value;
	}
	
	@Override
	public String toString(){
		return getType().toString() + ": " + value.toString();
	}
	
	public String toString(int length){
		StandardTextWriter stw = new StandardTextWriter(true);
		LimitedResultWriter lros = new LimitedResultWriter(length);
		
		try {
			stw.write(getValue(), lros);
		}
		catch (IOLimitReachedException iolrex){
			// This is fine, ignore.
		}
		catch (IOException ioex) {
			// This can never happen.
		}
		
		return getType().toString() + ": " + lros.toString();
	}
	
	public Type getType() { 
		return type;
	}
	
	protected <U extends IValue> Result<U> makeIntegerResult(int i) {
		return makeResult(getTypeFactory().integerType(), getValueFactory().integer(i), ctx);
	}
	
	/// TODO: Factory access:
	//this should probably access fields initialized by constructor invocations
	//passed into them by ResultFactory.
	
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
		throw new UnsupportedOperationError(operator, getType(), ctx.getCurrentAST());
	}
	
	protected <U extends IValue> Result<U> undefinedError(String operator, Result<?> arg) {
		throw new UnsupportedOperationError(operator, getType(), arg.getType(), ctx.getCurrentAST());
	}
	
	///////
	
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) throws MatchFailed {
		throw new UnsupportedOperationError("A value of type " + getType() + " is not something you can call like a function, a constructor or a closure.", ctx.getCurrentAST());
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

	public <U extends IValue, V extends IValue> Result<U> in(Result<V> that) {
		return undefinedError(IN_STRING, that);
	}

	public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> that) {
		return undefinedError(NOTIN_STRING, that);
	}
	
	public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right) {
		return undefinedError(COMPOSE_STRING, right);
	}
	
	public <U extends IValue> Result<U> negative() {
		return undefinedError(NEGATIVE_STRING);
	}

	public <U extends IValue> Result<U> negate() {
		return undefinedError(NEGATE_STRING);
	}

	public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> that) {
		return undefinedError(INTERSECTION_STRING, this);
	}
	
	public <U extends IValue, V extends IValue> Result<U> makeRange(Result<V> to) {
		return undefinedError(RANGE_STRING);
	}

	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		// e.g. for closures equality is undefined
		return undefinedError(EQUALS_STRING, this);
	}
	
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> that) {
		return undefinedError(COMPARE_STRING, that);
	}
	
	public <U extends IValue> Result<U> compareFunction(AbstractFunction that) {
		return undefinedError(COMPARE_STRING, that);
	}
	
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that) {
		return undefinedError(LESS_THAN_STRING, that);
	}
	
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that) {
		return undefinedError(LESS_THAN_OR_EQUAL_STRING, that);
	}

	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that) {
		return undefinedError(GREATER_THAN_STRING, that);
	}
	
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that) {
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

	public <U extends IValue> Result<U> getAnnotation(String annoName, Environment env) {
		return undefinedError(GET_ANNO_STRING);
	}	

	public <U extends IValue, V extends IValue> Result<U> setAnnotation(String annoName, Result<V> anno,
			Environment peek) {
		return undefinedError(SET_ANNO_STRING);
	}
	
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) { 
		return undefinedError(NON_EQUALS_STRING, that);
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

	protected <U extends IValue> Result<U> addReal(RealResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> Result<U> subtractReal(RealResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> Result<U> multiplyReal(RealResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}
	
	protected <U extends IValue> Result<U> divideReal(RealResult that) {
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
	
	protected <U extends IValue> Result<U> multiplyRelation(RelationResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}

	protected <U extends IValue> Result<U> joinRelation(RelationResult that) {
		return that.undefinedError(JOIN_STRING, this);
	}

	protected <U extends IValue> Result<U> addMap(MapResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> Result<U> subtractRelation(RelationResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> Result<U> subtractMap(MapResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> Result<U> moduloReal(RealResult that) {
		return that.undefinedError(MODULO_STRING, this);
	}
	
	protected <U extends IValue> Result<U> remainderReal(RealResult that) {
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

	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromReal(RealResult that, Result<V> step) {
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

	protected <U extends IValue> Result<U> makeRangeFromReal(RealResult that) {
		return that.undefinedError(RANGE_STRING, this);
	}

	protected <U extends IValue> Result<U> inSet(SetResult that) {
		return that.undefinedError(IN_STRING, this);
	}

	protected <U extends IValue> Result<U> inRelation(RelationResult that) {
		return that.undefinedError(IN_STRING, this);
	}

	protected <U extends IValue> Result<U> inList(ListResult that) {
		return that.undefinedError(IN_STRING, this);
	}

	protected <U extends IValue> Result<U> inMap(MapResult that) {
		return that.undefinedError(IN_STRING, this);
	}

	protected <U extends IValue> Result<U> notInSet(SetResult that) {
		return that.undefinedError(NOTIN_STRING, this);
	}

	protected <U extends IValue> Result<U> notInRelation(RelationResult that) {
		return that.undefinedError(NOTIN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> notInList(ListResult that) {
		return that.undefinedError(NOTIN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> notInMap(MapResult that) {
		return that.undefinedError(NOTIN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> composeRelation(RelationResult that) {
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
	
	protected <U extends IValue> Result<U> compareInteger(IntegerResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> compareRational(RationalResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> compareReal(RealResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> compareString(StringResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> compareSourceLocation(SourceLocationResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> compareTuple(TupleResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> compareSet(SetResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> compareList(ListResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> compareRelation(RelationResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> compareBool(BoolResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	public <U extends IValue> Result<U> compareOverloadedFunction(
			OverloadedFunction that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> compareConstructor(NodeResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> compareMap(MapResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> compareNode(NodeResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> compareDateTime(DateTimeResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}

	protected <U extends IValue> Result<U> equalToInteger(IntegerResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> equalToRational(RationalResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> equalToReal(RealResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> equalToString(StringResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> equalToList(ListResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> equalToSet(SetResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> equalToMap(MapResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> equalToNode(NodeResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> equalToConcreteSyntax(ConcreteSyntaxResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> equalToSourceLocation(SourceLocationResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> equalToRelation(RelationResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> equalToTuple(TupleResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> equalToBool(BoolResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> equalToValue(ValueResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	public <U extends IValue> Result<U> equalToOverloadedFunction(OverloadedFunction that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	public <U extends IValue> Result<U> equalToConstructorFunction(ConstructorFunction that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	public <U extends IValue> Result<U> equalToRascalFunction(RascalFunction that) {
		return that.undefinedError(EQUALS_STRING, this);
	}

	protected <U extends IValue> Result<U> equalToDateTime(DateTimeResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToInteger(IntegerResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToRational(RationalResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToString(StringResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToList(ListResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToSet(SetResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToMap(MapResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToNode(NodeResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToConcreteSyntax(ConcreteSyntaxResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToSourceLocation(SourceLocationResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToRelation(RelationResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToTuple(TupleResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToBool(BoolResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToValue(ValueResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> nonEqualToDateTime(DateTimeResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanInteger(IntegerResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualInteger(IntegerResult that) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> greaterThanInteger(IntegerResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualInteger(IntegerResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanRational(RationalResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualRational(RationalResult that) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> greaterThanRational(RationalResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualRational(RationalResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanReal(RealResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualReal(RealResult that) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> greaterThanReal(RealResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualReal(RealResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> lessThanString(StringResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualString(StringResult that) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> greaterThanString(StringResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualString(StringResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> lessThanTuple(TupleResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualTuple(TupleResult that) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> greaterThanTuple(TupleResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualTuple(TupleResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanNode(NodeResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualNode(NodeResult that) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> greaterThanNode(NodeResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualNode(NodeResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanBool(BoolResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualBool(BoolResult that) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> greaterThanBool(BoolResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualBool(BoolResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanMap(MapResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualMap(MapResult that) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> greaterThanMap(MapResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualMap(MapResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanSet(SetResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualSet(SetResult that) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> greaterThanSet(SetResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualSet(SetResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanRelation(RelationResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualRelation(RelationResult that) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> greaterThanRelation(RelationResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualRelation(RelationResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanList(ListResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualList(ListResult that) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> greaterThanList(ListResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualList(ListResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	// Comparison on DateTime

	protected <U extends IValue> Result<U> lessThanDateTime(DateTimeResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualDateTime(DateTimeResult that) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> greaterThanDateTime(DateTimeResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualDateTime(DateTimeResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}
	
	// Comparison on SourceLocation
	
	protected <U extends IValue> Result<U> lessThanSourceLocation(SourceLocationResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualSourceLocation(SourceLocationResult that) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this);
	}
	
	protected <U extends IValue> Result<U> greaterThanSourceLocation(SourceLocationResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualSourceLocation(SourceLocationResult that) {
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

	protected <U extends IValue> Result<U> equalToNumber(NumberResult that) {
		return that.undefinedError(EQUALS_STRING, this);
	}

	protected <U extends IValue> Result<U> nonEqualToNumber(NumberResult that) {
		return that.undefinedError(NON_EQUALS_STRING, this);
	}

	protected <U extends IValue> Result<U> lessThanNumber(NumberResult that) {
		return that.undefinedError(LESS_THAN_STRING, this);
	}

	protected <U extends IValue> Result<U> lessThanOrEqualNumber(NumberResult that) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> greaterThanNumber(NumberResult that) {
		return that.undefinedError(GREATER_THAN_STRING, this);
	}

	protected <U extends IValue> Result<U> greaterThanOrEqualNumber(NumberResult that) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this);
	}

	protected <U extends IValue> Result<U> compareNumber(NumberResult that) {
		return that.undefinedError(COMPARE_STRING, this);
	}
	
	protected <U extends IValue> Result<U> addSourceLocation(
			SourceLocationResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}
	
	public <U extends IValue> Result<U> is(Name name) {
		return undefinedError(IS_STRING, this);
	}
	
	public <U extends IValue> Result<U> has(Name name) {
		return undefinedError(HAS_STRING, this);
	}
	
	public boolean isPublic() {
		return isPublic;
	}
	
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public void setInferredType(boolean inferredType) {
		this.inferredType  = inferredType;
	}
	
	public boolean hasInferredType() {
		return inferredType;
	}

	

	
}
