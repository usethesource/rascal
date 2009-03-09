package org.meta_environment.rascal.interpreter.result;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.exceptions.ImplementationException;
import org.meta_environment.rascal.interpreter.exceptions.TypeErrorException;

public abstract class AbstractResult<T extends IValue> implements Iterator<AbstractResult<IValue>> {
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
	private Iterator<AbstractResult<IValue>> iterator = null;
	protected Type type;
	protected T value;

	protected AbstractResult(Type type, T value,  Iterator<AbstractResult<IValue>> iter) {
		if (!value.getType().isSubtypeOf(type)) {
			throw new TypeErrorException("expected value of type " + type + "; got a " + value.getType());
		}
		this.type = type;
		this.iterator = iter;
		this.value = value;
	}
	
	protected AbstractResult(Type type, T value) {
		this(type, value, null);
	}

	/// The "result" interface
	
	public T getValue() {
		return value;
	}
	
	public Type getType() { 
		return type;
	}
	
	@Deprecated
	public Type getValueType() {
		return getValue().getType();
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
	
	public AbstractResult<IValue> next(){
		if(iterator == null){
			new ImplementationException("next called on Result with null iterator");
		}
		return iterator.next(); //??? last = iterator.next();
	}

	public void remove() {
		throw new ImplementationException("remove() not implemented for (iterable) result");		
	}

	
	// Error aux methods
	
	private String toTypeString() {
		return type.toString();
	}
	
	protected <U extends IValue> AbstractResult<U> undefinedError(String operator) {
		return undefinedError(operator, toTypeString());
	}
	
	protected <U extends IValue, V extends IValue> AbstractResult<U> undefinedError(String operator, AbstractResult<V> arg) {
		return undefinedError(operator, toTypeString() + " and " + arg.toTypeString());
	}
	
	private <U extends IValue> AbstractResult<U> undefinedError(String operator, String args) {
		String msg = operator + "is not defined on " + args;
		throw new TypeErrorException(msg);
	}

	///////
	
	
	
	public <U extends IValue, V extends IValue> AbstractResult<U> add(AbstractResult<V> that) {
		return undefinedError(ADDITION_STRING, that);
	}

	public <U extends IValue, V extends IValue> AbstractResult<U> subtract(AbstractResult<V> that) {
		return undefinedError(SUBTRACTION_STRING, that);
	}

	public <U extends IValue, V extends IValue> AbstractResult<U> multiply(AbstractResult<V> that) {
		return undefinedError(MULTIPLICATION_STRING, that);
	}
	
	public <U extends IValue, V extends IValue> AbstractResult<U> divide(AbstractResult<V> that) {
		return undefinedError(DIVISION_STRING, that);
	}

	public <U extends IValue, V extends IValue> AbstractResult<U> modulo(AbstractResult<V> that) {
		return undefinedError(MODULO_STRING, that);
	}

	public <U extends IValue, V extends IValue> AbstractResult<U> in(AbstractResult<V> that) {
		return undefinedError(IN_STRING, that);
	}

	public <U extends IValue, V extends IValue> AbstractResult<U> notIn(AbstractResult<V> that) {
		return undefinedError(NOTIN_STRING, that);
	}

	public <U extends IValue> AbstractResult<U> negative() {
		return undefinedError(NEGATIVE_STRING);
	}

	public <U extends IValue, V extends IValue> AbstractResult<U> intersect(AbstractResult<V> that) {
		return undefinedError(INTERSECTION_STRING, this);
	}

	public <U extends IValue> AbstractResult<U> transitiveClosure() {
		return undefinedError(TRANSITIVE_CLOSURE_STRING);
	}

	public <U extends IValue> AbstractResult<U> transitiveReflexiveClosure() {
		return undefinedError(TRANSITIVE_REFLEXIVE_CLOSURE_STRING);
	}
	
	///////

	protected <U extends IValue> AbstractResult<U> addInteger(IntegerResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> subtractInteger(IntegerResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> multiplyInteger(IntegerResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> addReal(RealResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> subtractReal(RealResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> multiplyReal(RealResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}


	protected <U extends IValue> AbstractResult<U> divideReal(RealResult that) {
		return that.undefinedError(DIVISION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> divideInteger(IntegerResult that) {
		return that.undefinedError(DIVISION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> addString(StringResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> addList(ListResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> subtractList(ListResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> multiplyList(ListResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, that);
	}

	protected <U extends IValue> AbstractResult<U> addSet(SetResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> addRelation(RelationResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> addBool(BoolResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> subtractSet(SetResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> multiplySet(SetResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> addMap(MapResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> subtractRelation(RelationResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> moduloReal(RealResult that) {
		return that.undefinedError(MODULO_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> addTuple(TupleResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> moduloInteger(IntegerResult that) {
		return that.undefinedError(MODULO_STRING, this);
	}

	
	protected <U extends IValue> AbstractResult<U> intersectSet(SetResult that) {
		return that.undefinedError(INTERSECTION_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> inSet(SetResult that) {
		return that.undefinedError(IN_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> inList(ListResult that) {
		return that.undefinedError(IN_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> notInSet(SetResult that) {
		return that.undefinedError(NOTIN_STRING, this);
	}

	protected <U extends IValue> AbstractResult<U> notInList(ListResult that) {
		return that.undefinedError(NOTIN_STRING, this);
	}
	
	
	
}
