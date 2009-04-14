package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.ast.AbstractAST;
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

	protected Result(Type type, T value, Iterator<Result<IValue>> iter, AbstractAST ast) {
		// Check for null in case of void result or uninit.
		if (value != null && !value.getType().isSubtypeOf(type)) {
			throw new UnexpectedTypeError(type, value.getType(), ast);
		}
	
	    this.type = type;
		this.iterator = iter;
		this.value = value;
	}
	
	protected Result(Type type, T value, AbstractAST ast) {
		this(type, value, null, ast);
	}

	/// The "result" interface
	
	public T getValue() {
		return value;
	}
	
	public Type getType() { 
		return type;
	}
	
	protected <U extends IValue> Result<U> makeIntegerResult(int i) {
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
	
	protected <U extends IValue> Result<U> undefinedError(String operator, AbstractAST ast) {
		throw new UnsupportedOperationError(operator, getType(), ast);
	}
	
	@SuppressWarnings("unchecked")
	protected <U extends IValue> Result<U> undefinedError(String operator, Result arg, AbstractAST ast) {
		// TODO find source location
		throw new UnsupportedOperationError(operator, getType(), arg.getType(), ast);
	}
	
	///////
	
	
	
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> that, AbstractAST ast) {
		return undefinedError(ADDITION_STRING, that, ast);
	}

	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> that, AbstractAST ast) {
		return undefinedError(SUBTRACTION_STRING, that, ast);
	}

	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> that, AbstractAST ast) {
		return undefinedError(MULTIPLICATION_STRING, that, ast);
	}
	
	public <U extends IValue, V extends IValue> Result<U> join(Result<V> that, AbstractAST ast) {
		return undefinedError(JOIN_STRING, that, ast);
	}
	
	public <U extends IValue, V extends IValue> Result<U> divide(Result<V> that, AbstractAST ast) {
		return undefinedError(DIVISION_STRING, that, ast);
	}

	public <U extends IValue, V extends IValue> Result<U> modulo(Result<V> that, AbstractAST ast) {
		return undefinedError(MODULO_STRING, that, ast);
	}

	public <U extends IValue, V extends IValue> Result<U> in(Result<V> that, AbstractAST ast) {
		return undefinedError(IN_STRING, that, ast);
	}

	public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> that, AbstractAST ast) {
		return undefinedError(NOTIN_STRING, that, ast);
	}
	

	public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right, AbstractAST ast) {
		return undefinedError(COMPOSE_STRING, right, ast);
	}


	public <U extends IValue> Result<U> negative(AbstractAST ast) {
		return undefinedError(NEGATIVE_STRING, ast);
	}

	public <U extends IValue> Result<U> negate(AbstractAST ast) {
		return undefinedError(NEGATE_STRING, ast);
	}

	
	public <U extends IValue, V extends IValue> Result<U> intersect(Result<V> that, AbstractAST ast) {
		return undefinedError(INTERSECTION_STRING, this, ast);
	}
	
	public <U extends IValue, V extends IValue> Result<U> makeRange(Result<V> to, AbstractAST ast) {
		return undefinedError(RANGE_STRING, ast);
	}

	
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, AbstractAST ast) {
		// e.g. for closures equality is undefined
		return undefinedError(EQUALS_STRING, this, ast);
	}
	
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> that, AbstractAST ast) {
		return undefinedError(COMPARE_STRING, that, ast);
	}
	
	
	
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that, AbstractAST ast) {
		return undefinedError(LESS_THAN_STRING, that, ast);
	}
	
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that, AbstractAST ast) {
		return undefinedError(LESS_THAN_OR_EQUAL_STRING, that, ast);
	}

	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that, AbstractAST ast) {
		return undefinedError(GREATER_THAN_STRING, that, ast);
	}
	
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that, AbstractAST ast) {
		return undefinedError(GREATER_THAN_OR_EQUAL_STRING, that, ast);
	}


	public Result<IValue> ifThenElse(Result<IValue> then, Result<IValue> _else, AbstractAST ast) {
		return undefinedError(IF_THEN_ELSE_STRING, ast);
	}
	
	public <U extends IValue> Result<U> transitiveClosure(AbstractAST ast) {
		return undefinedError(TRANSITIVE_CLOSURE_STRING, ast);
	}

	public <U extends IValue> Result<U> transitiveReflexiveClosure(AbstractAST ast) {
		return undefinedError(TRANSITIVE_REFLEXIVE_CLOSURE_STRING, ast);
	}
	
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store, AbstractAST ast) {
		return undefinedError(FIELD_ACCESS_STRING, ast);
	}
	
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store, AbstractAST ast) {
		return undefinedError(FIELD_UPDATE_STRING, ast);
	}
	
	
	public <U extends IValue, V extends IValue, W extends IValue> Result<U> makeStepRange(Result<V> to, Result<W> second, AbstractAST ast) {
		return undefinedError(RANGE_STRING, ast);
	}


	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts, AbstractAST ast) {
		return undefinedError(SUBSCRIPT_STRING, ast);
	}

	public <U extends IValue> Result<U> getAnnotation(String annoName, Environment env, AbstractAST ast) {
		return undefinedError(GET_ANNO_STRING, ast);
	}	

	public <U extends IValue, V extends IValue> Result<U> setAnnotation(String annoName, Result<V> anno,
			Environment peek, AbstractAST ast) {
		return undefinedError(SET_ANNO_STRING, ast);
	}
	
	
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, AbstractAST ast) { 
		return undefinedError(NON_EQUALS_STRING, that, ast);
	}
	
	public boolean isTrue() {
		return false;
	}


	
	///////
	
	protected <U extends IValue> Result<U> addInteger(IntegerResult that, AbstractAST ast) {
		return that.undefinedError(ADDITION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> subtractInteger(IntegerResult that, AbstractAST ast) {
		return that.undefinedError(SUBTRACTION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> multiplyInteger(IntegerResult that, AbstractAST ast) {
		return that.undefinedError(MULTIPLICATION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> addReal(RealResult that, AbstractAST ast) {
		return that.undefinedError(ADDITION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> subtractReal(RealResult that, AbstractAST ast) {
		return that.undefinedError(SUBTRACTION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> multiplyReal(RealResult that, AbstractAST ast) {
		return that.undefinedError(MULTIPLICATION_STRING, this, ast);
	}


	protected <U extends IValue> Result<U> divideReal(RealResult that, AbstractAST ast) {
		return that.undefinedError(DIVISION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> divideInteger(IntegerResult that, AbstractAST ast) {
		return that.undefinedError(DIVISION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> addString(StringResult that, AbstractAST ast) {
		return that.undefinedError(ADDITION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> addList(ListResult that, AbstractAST ast) {
		return that.undefinedError(ADDITION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> subtractList(ListResult that, AbstractAST ast) {
		return that.undefinedError(SUBTRACTION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> multiplyList(ListResult that, AbstractAST ast) {
		return that.undefinedError(MULTIPLICATION_STRING, that, ast);
	}

	protected <U extends IValue> Result<U> addSet(SetResult that, AbstractAST ast) {
		return that.undefinedError(ADDITION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> addRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(ADDITION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> addBool(BoolResult that, AbstractAST ast) {
		return that.undefinedError(ADDITION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> subtractSet(SetResult that, AbstractAST ast) {
		return that.undefinedError(SUBTRACTION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> multiplySet(SetResult that, AbstractAST ast) {
		return that.undefinedError(MULTIPLICATION_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> joinSet(SetResult that, AbstractAST ast) {
		return that.undefinedError(JOIN_STRING, this, ast);
	}


	protected <U extends IValue> Result<U> multiplyRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(MULTIPLICATION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> joinRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(JOIN_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> addMap(MapResult that, AbstractAST ast) {
		return that.undefinedError(ADDITION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> subtractRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(SUBTRACTION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> subtractMap(MapResult that, AbstractAST ast) {
		return that.undefinedError(SUBTRACTION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> moduloReal(RealResult that, AbstractAST ast) {
		return that.undefinedError(MODULO_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> addTuple(TupleResult that, AbstractAST ast) {
		return that.undefinedError(ADDITION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> moduloInteger(IntegerResult that, AbstractAST ast) {
		return that.undefinedError(MODULO_STRING, this, ast);
	}

	
	protected <U extends IValue> Result<U> intersectSet(SetResult that, AbstractAST ast) {
		return that.undefinedError(INTERSECTION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> intersectRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(INTERSECTION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> intersectMap(MapResult that, AbstractAST ast) {
		return that.undefinedError(INTERSECTION_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> makeRangeFromInteger(IntegerResult that, AbstractAST ast) {
		return that.undefinedError(RANGE_STRING, this, ast);
	}

	protected <U extends IValue, V extends IValue> Result<U> makeStepRangeFromInteger(IntegerResult that, Result<V> step, AbstractAST ast) {
		return that.undefinedError(RANGE_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> inSet(SetResult that, AbstractAST ast) {
		return that.undefinedError(IN_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> inRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(IN_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> inList(ListResult that, AbstractAST ast) {
		return that.undefinedError(IN_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> inMap(MapResult that, AbstractAST ast) {
		return that.undefinedError(IN_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> notInSet(SetResult that, AbstractAST ast) {
		return that.undefinedError(NOTIN_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> notInRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(NOTIN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> notInList(ListResult that, AbstractAST ast) {
		return that.undefinedError(NOTIN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> notInMap(MapResult that, AbstractAST ast) {
		return that.undefinedError(NOTIN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> composeRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(COMPOSE_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> compareInteger(IntegerResult that, AbstractAST ast) {
		return that.undefinedError(COMPARE_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> compareReal(RealResult that, AbstractAST ast) {
		return that.undefinedError(COMPARE_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> compareString(StringResult that, AbstractAST ast) {
		return that.undefinedError(COMPARE_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> compareSourceLocation(SourceLocationResult that, AbstractAST ast) {
		return that.undefinedError(COMPARE_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> compareTuple(TupleResult that, AbstractAST ast) {
		return that.undefinedError(COMPARE_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> compareSet(SetResult that, AbstractAST ast) {
		return that.undefinedError(COMPARE_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> compareList(ListResult that, AbstractAST ast) {
		return that.undefinedError(COMPARE_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> compareRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(COMPARE_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> compareBool(BoolResult that, AbstractAST ast) {
		return that.undefinedError(COMPARE_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> compareConstructor(NodeResult that, AbstractAST ast) {
		return that.undefinedError(COMPARE_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> compareMap(MapResult that, AbstractAST ast) {
		return that.undefinedError(COMPARE_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> equalToInteger(IntegerResult that, AbstractAST ast) {
		return that.undefinedError(EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> equalToReal(RealResult that, AbstractAST ast) {
		return that.undefinedError(EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> equalToString(StringResult that, AbstractAST ast) {
		return that.undefinedError(EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> equalToList(ListResult that, AbstractAST ast) {
		return that.undefinedError(EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> equalToSet(SetResult that, AbstractAST ast) {
		return that.undefinedError(EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> equalToMap(MapResult that, AbstractAST ast) {
		return that.undefinedError(EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> equalToNode(NodeResult that, AbstractAST ast) {
		return that.undefinedError(EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> equalToSourceLocation(SourceLocationResult that, AbstractAST ast) {
		return that.undefinedError(EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> equalToRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> equalToTuple(TupleResult that, AbstractAST ast) {
		return that.undefinedError(EQUALS_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> equalToBool(BoolResult that, AbstractAST ast) {
		return that.undefinedError(EQUALS_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> equalToValue(ValueResult that, AbstractAST ast) {
		return that.undefinedError(EQUALS_STRING, this, ast);
	}
	

	protected <U extends IValue> Result<U> nonEqualToInteger(IntegerResult that, AbstractAST ast) {
		return that.undefinedError(NON_EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that, AbstractAST ast) {
		return that.undefinedError(NON_EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> nonEqualToString(StringResult that, AbstractAST ast) {
		return that.undefinedError(NON_EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> nonEqualToList(ListResult that, AbstractAST ast) {
		return that.undefinedError(NON_EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> nonEqualToSet(SetResult that, AbstractAST ast) {
		return that.undefinedError(NON_EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> nonEqualToMap(MapResult that, AbstractAST ast) {
		return that.undefinedError(NON_EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> nonEqualToNode(NodeResult that, AbstractAST ast) {
		return that.undefinedError(NON_EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> nonEqualToSourceLocation(SourceLocationResult that, AbstractAST ast) {
		return that.undefinedError(NON_EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> nonEqualToRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(NON_EQUALS_STRING, this, ast);
	}
	protected <U extends IValue> Result<U> nonEqualToTuple(TupleResult that, AbstractAST ast) {
		return that.undefinedError(NON_EQUALS_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> nonEqualToBool(BoolResult that, AbstractAST ast) {
		return that.undefinedError(NON_EQUALS_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> nonEqualToValue(ValueResult that, AbstractAST ast) {
		return that.undefinedError(NON_EQUALS_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanInteger(IntegerResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualInteger(IntegerResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> greaterThanInteger(IntegerResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualInteger(IntegerResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ast);
	}

	
	protected <U extends IValue> Result<U> lessThanReal(RealResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualReal(RealResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> greaterThanReal(RealResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualReal(RealResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> lessThanString(StringResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualString(StringResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> greaterThanString(StringResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualString(StringResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> lessThanTuple(TupleResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualTuple(TupleResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> greaterThanTuple(TupleResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualTuple(TupleResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanNode(NodeResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualNode(NodeResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> greaterThanNode(NodeResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualNode(NodeResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanBool(BoolResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualBool(BoolResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> greaterThanBool(BoolResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualBool(BoolResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanMap(MapResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualMap(MapResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> greaterThanMap(MapResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualMap(MapResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanSet(SetResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualSet(SetResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> greaterThanSet(SetResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualSet(SetResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> greaterThanRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualRelation(RelationResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanList(ListResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> lessThanOrEqualList(ListResult that, AbstractAST ast) {
		return that.undefinedError(LESS_THAN_OR_EQUAL_STRING, this, ast);
	}

	protected <U extends IValue> Result<U> greaterThanList(ListResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_STRING, this, ast);
	}
	
	protected <U extends IValue> Result<U> greaterThanOrEqualList(ListResult that, AbstractAST ast) {
		return that.undefinedError(GREATER_THAN_OR_EQUAL_STRING, this, ast);
	}
	
	
	public boolean isPublic() {
		return isPublic;
	}
	
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	
	
	
	
}
