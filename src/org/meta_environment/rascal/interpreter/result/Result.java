package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedOperationError;


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

	protected Result(Type type, T value, Iterator<Result<IValue>> iter, EvaluatorContext ctx) {
		// Check for null in case of void result or uninit.
		if (value != null && !value.getType().isSubtypeOf(type)) {
			throw new UnexpectedTypeError(type, value.getType(), ctx.getCurrentAST());
		}
	
	    this.type = type;
		this.iterator = iter;
		this.value = value;
	}
	
	protected Result(Type type, T value, EvaluatorContext ctx) {
		this(type, value, null, ctx);
	}

	/// The "result" interface
	
	public T getValue() {
		return value;
	}
	
	public Type getType() { 
		return type;
	}
	
	protected <U extends IValue> Result<U> makeIntegerResult(int i, EvaluatorContext ctx) {
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
	
	protected <U extends IValue> Result<U> undefinedError(String operator, EvaluatorContext ctx) {
		throw new UnsupportedOperationError(operator, getType(), ctx.getCurrentAST());
	}
	
	@SuppressWarnings("unchecked")
	protected <U extends IValue> Result<U> undefinedError(String operator, Result arg, EvaluatorContext ctx) {
		// TODO find source location
		throw new UnsupportedOperationError(operator, getType(), arg.getType(), ctx.getCurrentAST());
	}
	
	///////
	
	
	
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> that, EvaluatorContext ctx) {
		return undefinedError(ADDITION_STRING, that, ctx);
	}

	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> that, EvaluatorContext ctx) {
		return undefinedError(SUBTRACTION_STRING, that, ctx);
	}

	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> that, EvaluatorContext ctx) {
		return undefinedError(MULTIPLICATION_STRING, that, ctx);
	}
	
	public <U extends IValue, V extends IValue> Result<U> join(Result<V> that, EvaluatorContext ctx) {
		return undefinedError(JOIN_STRING, that, ctx);
	}
	
	public <U extends IValue, V extends IValue> Result<U> divide(Result<V> that, EvaluatorContext ctx) {
		return undefinedError(DIVISION_STRING, that, ctx);
	}

	public <U extends IValue, V extends IValue> Result<U> modulo(Result<V> that, EvaluatorContext ctx) {
		return undefinedError(MODULO_STRING, that, ctx);
	}

	public <U extends IValue, V extends IValue> Result<U> in(Result<V> that, EvaluatorContext ctx) {
		return undefinedError(IN_STRING, that, ctx);
	}

	public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> that, EvaluatorContext ctx) {
		return undefinedError(NOTIN_STRING, that, ctx);
	}
	

	public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right, EvaluatorContext ctx) {
		return undefinedError(COMPOSE_STRING, right, ctx);
	}


	public <U extends IValue> Result<U> negative(EvaluatorContext ctx) {
		return undefinedError(NEGATIVE_STRING, ctx);
	}

	public <U extends IValue> Result<U> negate(EvaluatorContext ctx) {
		return undefinedError(NEGATE_STRING, ctx);
	}

	
	public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> that, EvaluatorContext ctx) {
		return undefinedError(INTERSECTION_STRING, this, ctx);
	}
	
	public <U extends IValue, V extends IValue> Result<U> makeRange(Result<V> to, EvaluatorContext ctx) {
		return undefinedError(RANGE_STRING, ctx);
	}

	
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, EvaluatorContext ctx) {
		// e.g. for closures equality is undefined
		return undefinedError(EQUALS_STRING, this, ctx);
	}
	
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> that, EvaluatorContext ctx) {
		return undefinedError(COMPARE_STRING, that, ctx);
	}
	
	
	
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that, EvaluatorContext ctx) {
		return undefinedError(LESS_THAN_STRING, that, ctx);
	}
	
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that, EvaluatorContext ctx) {
		return undefinedError(LESS_THAN_OR_EQUAL_STRING, that, ctx);
	}

	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that, EvaluatorContext ctx) {
		return undefinedError(GREATER_THAN_STRING, that, ctx);
	}
	
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that, EvaluatorContext ctx) {
		return undefinedError(GREATER_THAN_OR_EQUAL_STRING, that, ctx);
	}


	public Result<IValue> ifThenElse(Result<IValue> then, Result<IValue> _else, EvaluatorContext ctx) {
		return undefinedError(IF_THEN_ELSE_STRING, ctx);
	}
	
	public <U extends IValue> Result<U> transitiveClosure(EvaluatorContext ctx) {
		return undefinedError(TRANSITIVE_CLOSURE_STRING, ctx);
	}

	public <U extends IValue> Result<U> transitiveReflexiveClosure(EvaluatorContext ctx) {
		return undefinedError(TRANSITIVE_REFLEXIVE_CLOSURE_STRING, ctx);
	}
	
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store, EvaluatorContext ctx) {
		return undefinedError(FIELD_ACCESS_STRING, ctx);
	}
	
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store, EvaluatorContext ctx) {
		return undefinedError(FIELD_UPDATE_STRING, ctx);
	}
	
	
	public <U extends IValue, V extends IValue, W extends IValue> Result<U> makeStepRange(Result<V> to, Result<W> second, EvaluatorContext ctx) {
		return undefinedError(RANGE_STRING, ctx);
	}


	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts, EvaluatorContext ctx) {
		return undefinedError(SUBSCRIPT_STRING, ctx);
	}

	public <U extends IValue> Result<U> getAnnotation(String annoName, Environment env, EvaluatorContext ctx) {
		return undefinedError(GET_ANNO_STRING, ctx);
	}	

	public <U extends IValue, V extends IValue> Result<U> setAnnotation(String annoName, Result<V> anno,
			Environment peek, EvaluatorContext ctx) {
		return undefinedError(SET_ANNO_STRING, ctx);
	}
	
	
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, EvaluatorContext ctx) { 
		return undefinedError(NON_EQUALS_STRING, that, ctx);
	}
	
	public boolean isTrue() {
		return false;
	}


	
	///////
	
	protected <U extends IValue> Result<U> addInteger(IntegerResult that, EvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> subtractInteger(IntegerResult that, EvaluatorContext ctx) {
		return that.undefinedError(SUBTRACTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> multiplyInteger(IntegerResult that, EvaluatorContext ctx) {
		return that.undefinedError(MULTIPLICATION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> addReal(RealResult that, EvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> subtractReal(RealResult that, EvaluatorContext ctx) {
		return that.undefinedError(SUBTRACTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> multiplyReal(RealResult that, EvaluatorContext ctx) {
		return that.undefinedError(MULTIPLICATION_STRING, this, ctx);
	}


	protected <U extends IValue> Result<U> divideReal(RealResult that, EvaluatorContext ctx) {
		return that.undefinedError(DIVISION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> divideInteger(IntegerResult that, EvaluatorContext ctx) {
		return that.undefinedError(DIVISION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> addString(StringResult that, EvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> addList(ListResult that, EvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> subtractList(ListResult that, EvaluatorContext ctx) {
		return that.undefinedError(SUBTRACTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> multiplyList(ListResult that, EvaluatorContext ctx) {
		return that.undefinedError(MULTIPLICATION_STRING, that, ctx);
	}

	protected <U extends IValue> Result<U> addSet(SetResult that, EvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> addRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> addBool(BoolResult that, EvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> subtractSet(SetResult that, EvaluatorContext ctx) {
		return that.undefinedError(SUBTRACTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> multiplySet(SetResult that, EvaluatorContext ctx) {
		return that.undefinedError(MULTIPLICATION_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> joinSet(SetResult that, EvaluatorContext ctx) {
		return that.undefinedError(JOIN_STRING, this, ctx);
	}


	protected <U extends IValue> Result<U> multiplyRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(MULTIPLICATION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> joinRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(JOIN_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> addMap(MapResult that, EvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> subtractRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(SUBTRACTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> subtractMap(MapResult that, EvaluatorContext ctx) {
		return that.undefinedError(SUBTRACTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> moduloReal(RealResult that, EvaluatorContext ctx) {
		return that.undefinedError(MODULO_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> addTuple(TupleResult that, EvaluatorContext ctx) {
		return that.undefinedError(ADDITION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> moduloInteger(IntegerResult that, EvaluatorContext ctx) {
		return that.undefinedError(MODULO_STRING, this, ctx);
	}

	
	protected <U extends IValue> Result<U> intersectSet(SetResult that, EvaluatorContext ctx) {
		return that.undefinedError(INTERSECTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> intersectRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(INTERSECTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> intersectMap(MapResult that, EvaluatorContext ctx) {
		return that.undefinedError(INTERSECTION_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> makeRangeFromInteger(IntegerResult that, EvaluatorContext ctx) {
		return that.undefinedError(RANGE_STRING, this, ctx);
	}

	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromInteger(IntegerResult that, Result<V> step, EvaluatorContext ctx) {
		return that.undefinedError(RANGE_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> inSet(SetResult that, EvaluatorContext ctx) {
		return that.undefinedError(IN_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> inRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(IN_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> inList(ListResult that, EvaluatorContext ctx) {
		return that.undefinedError(IN_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> inMap(MapResult that, EvaluatorContext ctx) {
		return that.undefinedError(IN_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> notInSet(SetResult that, EvaluatorContext ctx) {
		return that.undefinedError(NOTIN_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> notInRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(NOTIN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> notInList(ListResult that, EvaluatorContext ctx) {
		return that.undefinedError(NOTIN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> notInMap(MapResult that, EvaluatorContext ctx) {
		return that.undefinedError(NOTIN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> composeRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(COMPOSE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareInteger(IntegerResult that, EvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareReal(RealResult that, EvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareString(StringResult that, EvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareSourceLocation(SourceLocationResult that, EvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareTuple(TupleResult that, EvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareSet(SetResult that, EvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareList(ListResult that, EvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareBool(BoolResult that, EvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareConstructor(NodeResult that, EvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> compareMap(MapResult that, EvaluatorContext ctx) {
		return that.undefinedError(COMPARE_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> equalToInteger(IntegerResult that, EvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToReal(RealResult that, EvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToString(StringResult that, EvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToList(ListResult that, EvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToSet(SetResult that, EvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToMap(MapResult that, EvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToNode(NodeResult that, EvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToSourceLocation(SourceLocationResult that, EvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> equalToTuple(TupleResult that, EvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> equalToBool(BoolResult that, EvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> equalToValue(ValueResult that, EvaluatorContext ctx) {
		return that.undefinedError(EQUALS_STRING, this, ctx);
	}
	

	protected <U extends IValue> Result<U> nonEqualToInteger(IntegerResult that, EvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that, EvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToString(StringResult that, EvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToList(ListResult that, EvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToSet(SetResult that, EvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToMap(MapResult that, EvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToNode(NodeResult that, EvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToSourceLocation(SourceLocationResult that, EvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	protected <U extends IValue> Result<U> nonEqualToTuple(TupleResult that, EvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> nonEqualToBool(BoolResult that, EvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> nonEqualToValue(ValueResult that, EvaluatorContext ctx) {
		return that.undefinedError(NON_EQUALS_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanInteger(IntegerResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualInteger(IntegerResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanInteger(IntegerResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualInteger(IntegerResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}

	
	protected <U extends IValue> Result<U> lessThanReal(RealResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualReal(RealResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanReal(RealResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualReal(RealResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> lessThanString(StringResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualString(StringResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanString(StringResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualString(StringResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> lessThanTuple(TupleResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualTuple(TupleResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanTuple(TupleResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualTuple(TupleResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanNode(NodeResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualNode(NodeResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanNode(NodeResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualNode(NodeResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanBool(BoolResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualBool(BoolResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanBool(BoolResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualBool(BoolResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanMap(MapResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualMap(MapResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanMap(MapResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualMap(MapResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanSet(SetResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualSet(SetResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanSet(SetResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualSet(SetResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualRelation(RelationResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanList(ListResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualList(ListResult that, EvaluatorContext ctx) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ctx);
	}

	protected <U extends IValue> Result<U> greaterThanList(ListResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_STRING, this, ctx);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualList(ListResult that, EvaluatorContext ctx) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ctx);
	}
	
	
	public boolean isPublic() {
		return isPublic;
	}
	
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	
	
	
	
}
