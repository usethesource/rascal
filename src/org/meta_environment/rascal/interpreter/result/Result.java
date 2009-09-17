package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedOperationError;
import org.meta_environment.rascal.interpreter.types.NonTerminalType;
import org.meta_environment.uptr.Factory;


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
	
	private Iterator<Result<IValue>> iterator = null;
	protected Type type;
	protected T value;
	private boolean isPublic;
	private boolean inferredType = false;

	protected Result(Type type, T value, Iterator<Result<IValue>> iter, IEvaluatorContext ctx) {
		// Check for null in case of void result or uninit.
		if (value != null && !value.getType().isSubtypeOf(type) && !(type instanceof NonTerminalType && value.getType() == Factory.Tree)) {
			throw new UnexpectedTypeError(type, value.getType(), ctx.getCurrentAST());
		}
	
	    this.type = type;
		this.iterator = iter;
		this.value = value;
	}
	
	protected Result(Type type, T value, IEvaluatorContext ctx) {
		this(type, value, null, ctx);
	}

	/// The "result" interface
	
	public T getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return getType().toString() + ": " + getValue().toString();
	}
	
	public Type getType() { 
		return type;
	}
	
	protected <U extends IValue> Result<U> makeIntegerResult(int i, IEvaluatorContext ctx) {
		return makeResult(getTypeFactory().integerType(), getValueFactory().integer(i), null);
	}
	
	
	/// TODO: Factory access:
	//this should probably access fields initialized by constructor invocations
	//passed into them by ResultFactory.
	
	protected TypeFactory getTypeFactory() {
		return TypeFactory.getInstance();
	}
	
	protected IValueFactory getValueFactory() {
		return ValueFactoryFactory.getValueFactory();
	}
	
	//////// The iterator interface
	
	
	public boolean hasNext(){
		return iterator != null && iterator.hasNext();
	}
	
	public Result<IValue> next(){
		if(iterator == null){
			new ImplementationError("next called on Result with null iterator");
		}
		return iterator.next(); //??? last = iterator.next();
	}

	public void remove() {
		throw new ImplementationError("remove() not implemented for (iterable) result");		
	}

	
	// Error aux methods
	
	protected <U extends IValue> Result<U> undefinedError(String operator, IEvaluatorContext ctx) {
		throw new UnsupportedOperationError(operator, getType(), ctx.getCurrentAST());
	}
	
	@SuppressWarnings("unchecked")
	protected <U extends IValue> Result<U> undefinedError(String operator, Result arg, IEvaluatorContext ctx) {
		throw new UnsupportedOperationError(operator, getType(), arg.getType(), ctx.getCurrentAST());
	}
	
	///////
	
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, IEvaluatorContext ctx) {
		throw new UnsupportedOperationError("A value of type " + getType() + " is not something you can call like a function, a constructor or a closure.", ctx.getCurrentAST());
	}
	
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> that, IEvaluatorContext ctx) {
		return undefinedError(ADDITION_STRING, that, ctx);
	}

	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> that, IEvaluatorContext ctx) {
		return undefinedError(SUBTRACTION_STRING, that, ctx);
	}

	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> that, IEvaluatorContext ctx) {
		return undefinedError(MULTIPLICATION_STRING, that, ctx);
	}
	
	public <U extends IValue, V extends IValue> Result<U> join(Result<V> that, IEvaluatorContext ctx) {
		return undefinedError(JOIN_STRING, that, ctx);
	}
	
	public <U extends IValue, V extends IValue> Result<U> divide(Result<V> that, IEvaluatorContext ctx) {
		return undefinedError(DIVISION_STRING, that, ctx);
	}

	public <U extends IValue, V extends IValue> Result<U> modulo(Result<V> that, IEvaluatorContext ctx) {
		return undefinedError(MODULO_STRING, that, ctx);
	}

	public <U extends IValue, V extends IValue> Result<U> in(Result<V> that, IEvaluatorContext ctx) {
		return undefinedError(IN_STRING, that, ctx);
	}

	public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> that, IEvaluatorContext ctx) {
		return undefinedError(NOTIN_STRING, that, ctx);
	}
	

	public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right, IEvaluatorContext ctx) {
		return undefinedError(COMPOSE_STRING, right, ctx);
	}


	public <U extends IValue> Result<U> negative(IEvaluatorContext ctx) {
		return undefinedError(NEGATIVE_STRING, ctx);
	}

	public <U extends IValue> Result<U> negate(IEvaluatorContext ctx) {
		return undefinedError(NEGATE_STRING, ctx);
	}

	
	public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> that, IEvaluatorContext ctx) {
		return undefinedError(INTERSECTION_STRING, this, ctx);
	}
	
	public <U extends IValue, V extends IValue> Result<U> makeRange(Result<V> to, IEvaluatorContext ctx) {
		return undefinedError(RANGE_STRING, ctx);
	}

	
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, IEvaluatorContext ctx) {
		// e.g. for closures equality is undefined
		return undefinedError(EQUALS_STRING, this, ctx);
	}
	
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> that, IEvaluatorContext ctx) {
		return undefinedError(COMPARE_STRING, that, ctx);
	}
	
	public <U extends IValue> Result<U> compareFunction(AbstractFunction that,
			IEvaluatorContext ctx) {
		return undefinedError(COMPARE_STRING, that, ctx);
	}
	
	
	
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that, IEvaluatorContext ctx) {
		return undefinedError(LESS_THAN_STRING, that, ctx);
	}
	
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that, IEvaluatorContext ctx) {
		return undefinedError(LESS_THAN_OR_EQUAL_STRING, that, ctx);
	}

	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that, IEvaluatorContext ctx) {
		return undefinedError(GREATER_THAN_STRING, that, ctx);
	}
	
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that, IEvaluatorContext ctx) {
		return undefinedError(GREATER_THAN_OR_EQUAL_STRING, that, ctx);
	}


	public Result<IValue> ifThenElse(Result<IValue> then, Result<IValue> _else, IEvaluatorContext ctx) {
		return undefinedError(IF_THEN_ELSE_STRING, ctx);
	}
	
	public <U extends IValue> Result<U> transitiveClosure(IEvaluatorContext ctx) {
		return undefinedError(TRANSITIVE_CLOSURE_STRING, ctx);
	}

	public <U extends IValue> Result<U> transitiveReflexiveClosure(IEvaluatorContext ctx) {
		return undefinedError(TRANSITIVE_REFLEXIVE_CLOSURE_STRING, ctx);
	}
	
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store, IEvaluatorContext ctx) {
		return undefinedError(FIELD_ACCESS_STRING, ctx);
	}
	
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store, IEvaluatorContext ctx) {
		return undefinedError(FIELD_UPDATE_STRING, ctx);
	}
	
	
	public <U extends IValue, V extends IValue, W extends IValue> Result<U> makeStepRange(Result<V> to, Result<W> second, IEvaluatorContext ctx) {
		return undefinedError(RANGE_STRING, ctx);
	}


	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts, IEvaluatorContext ctx) {
		return undefinedError(SUBSCRIPT_STRING, ctx);
	}

	public <U extends IValue> Result<U> getAnnotation(String annoName, Environment env, IEvaluatorContext ctx) {
		return undefinedError(GET_ANNO_STRING, ctx);
	}	

	public <U extends IValue, V extends IValue> Result<U> setAnnotation(String annoName, Result<V> anno,
			Environment peek, IEvaluatorContext ctx) {
		return undefinedError(SET_ANNO_STRING, ctx);
	}
	
	
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, IEvaluatorContext ctx) { 
		return undefinedError(NON_EQUALS_STRING, that, ctx);
	}
	
	public boolean isTrue() {
		return false;
	}


	
	///////
	
	protected <U extends IValue> Result<U> addInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> subtractInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.undefinedError(SUBTRACTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> multiplyInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.undefinedError(MULTIPLICATION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> addReal(RealResult that, IEvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> subtractReal(RealResult that, IEvaluatorContext ctx) {
		return that.undefinedError(SUBTRACTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> multiplyReal(RealResult that, IEvaluatorContext ctx) {
		return that.undefinedError(MULTIPLICATION_STRING, this, ctx);
	}


	protected <U extends IValue> Result<U> divideReal(RealResult that, IEvaluatorContext ctx) {
		return that.undefinedError(DIVISION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> divideInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.undefinedError(DIVISION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> addString(StringResult that, IEvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> addList(ListResult that, IEvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> subtractList(ListResult that, IEvaluatorContext ctx) {
		return that.undefinedError(SUBTRACTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> multiplyList(ListResult that, IEvaluatorContext ctx) {
		return that.undefinedError(MULTIPLICATION_STRING, that, ctx);
	}

	protected <U extends IValue> Result<U> addSet(SetResult that, IEvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> addRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> addBool(BoolResult that, IEvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> subtractSet(SetResult that, IEvaluatorContext ctx) {
		return that.undefinedError(SUBTRACTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> multiplySet(SetResult that, IEvaluatorContext ctx) {
		return that.undefinedError(MULTIPLICATION_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> joinSet(SetResult that, IEvaluatorContext ctx) {
		return that.undefinedError(JOIN_STRING, this, ctx);
	}


	protected <U extends IValue> Result<U> multiplyRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(MULTIPLICATION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> joinRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(JOIN_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> addMap(MapResult that, IEvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> subtractRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(SUBTRACTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> subtractMap(MapResult that, IEvaluatorContext ctx) {
		return that.undefinedError(SUBTRACTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> moduloReal(RealResult that, IEvaluatorContext ctx) {
		return that.undefinedError(MODULO_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> addTuple(TupleResult that, IEvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> moduloInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.undefinedError(MODULO_STRING, this, ctx);
	}

	
	protected <U extends IValue> Result<U> intersectSet(SetResult that, IEvaluatorContext ctx) {
		return that.undefinedError(INTERSECTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> intersectRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(INTERSECTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> intersectMap(MapResult that, IEvaluatorContext ctx) {
		return that.undefinedError(INTERSECTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> makeRangeFromInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.undefinedError(RANGE_STRING, this, ctx);
	}

	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromInteger(IntegerResult that, Result<V> step, IEvaluatorContext ctx) {
		return that.undefinedError(RANGE_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> inSet(SetResult that, IEvaluatorContext ctx) {
		return that.undefinedError(IN_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> inRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(IN_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> inList(ListResult that, IEvaluatorContext ctx) {
		return that.undefinedError(IN_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> inMap(MapResult that, IEvaluatorContext ctx) {
		return that.undefinedError(IN_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> notInSet(SetResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NOTIN_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> notInRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NOTIN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> notInList(ListResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NOTIN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> notInMap(MapResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NOTIN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> composeRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPOSE_STRING, this, ctx);
	}
	
	public <U extends IValue> Result<U> composeMap(MapResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPOSE_STRING, this, ctx);
	}
	
	public <U extends IValue> Result<U> composeFunction(AbstractFunction that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPOSE_STRING, this, ctx);
	}
	
	public <U extends IValue> Result<U> composeOverloadedFunction(
			OverloadedFunctionResult that,
			IEvaluatorContext ctx) {
		return that.undefinedError(COMPOSE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareReal(RealResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareString(StringResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareSourceLocation(SourceLocationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareTuple(TupleResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareSet(SetResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareList(ListResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareBool(BoolResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	public <U extends IValue> Result<U> compareOverloadedFunction(
			OverloadedFunctionResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareConstructor(NodeResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareMap(MapResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareNode(NodeResult that, IEvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> equalToInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToReal(RealResult that, IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToString(StringResult that, IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToList(ListResult that, IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToSet(SetResult that, IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToMap(MapResult that, IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToNode(NodeResult that, IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToConcreteSyntax(ConcreteSyntaxResult that, IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToSourceLocation(SourceLocationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToTuple(TupleResult that, IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> equalToBool(BoolResult that, IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> equalToValue(ValueResult that, IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	
	public <U extends IValue> Result<U> equalToOverloadedFunction(
			OverloadedFunctionResult that,
			IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	
	public <U extends IValue> Result<U> equalToConstructorFunction(
			ConstructorFunction that,
			IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	
	public <U extends IValue> Result<U> equalToRascalFunction(
			RascalFunction that,
			IEvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> nonEqualToInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToString(StringResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToList(ListResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToSet(SetResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToMap(MapResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToNode(NodeResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToConcreteSyntax(ConcreteSyntaxResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToSourceLocation(SourceLocationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToTuple(TupleResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> nonEqualToBool(BoolResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> nonEqualToValue(ValueResult that, IEvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualInteger(IntegerResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}

	
	protected <U extends IValue> Result<U> lessThanReal(RealResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualReal(RealResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanReal(RealResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualReal(RealResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> lessThanString(StringResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualString(StringResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanString(StringResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualString(StringResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> lessThanTuple(TupleResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualTuple(TupleResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanTuple(TupleResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualTuple(TupleResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanNode(NodeResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualNode(NodeResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanNode(NodeResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualNode(NodeResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanBool(BoolResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualBool(BoolResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanBool(BoolResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualBool(BoolResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanMap(MapResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualMap(MapResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanMap(MapResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualMap(MapResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanSet(SetResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualSet(SetResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanSet(SetResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualSet(SetResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualRelation(RelationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanList(ListResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualList(ListResult that, IEvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanList(ListResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualList(ListResult that, IEvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> addSourceLocation(
			SourceLocationResult that, IEvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
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
