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
	
	
	/// Factory access: this should probably access fields initialized by constructor invocations
	
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
	
	
	
	public <U extends IValue> AbstractResult<U> add(AbstractResult<U> that) {
		return undefinedError(ADDITION_STRING, that);
	}

	public AbstractResult subtract(AbstractResult that) {
		return undefinedError(SUBTRACTION_STRING, that);
	}

	public AbstractResult multiply(AbstractResult that) {
		return undefinedError(MULTIPLICATION_STRING, that);
	}
	
	public AbstractResult divide(AbstractResult that) {
		return undefinedError(DIVISION_STRING, that);
	}

	public AbstractResult modulo(AbstractResult that) {
		return undefinedError(MODULO_STRING, that);
	}

	public AbstractResult in(AbstractResult that) {
		return undefinedError(IN_STRING, that);
	}

	public AbstractResult notIn(AbstractResult that) {
		return undefinedError(NOTIN_STRING, that);
	}

	public AbstractResult negative() {
		return undefinedError(NEGATIVE_STRING);
	}

	public AbstractResult transitiveClosure() {
		return undefinedError(TRANSITIVE_CLOSURE_STRING);
	}

	public AbstractResult transitiveReflexiveClosure() {
		return undefinedError(TRANSITIVE_REFLEXIVE_CLOSURE_STRING);
	}
	
	///////

	protected AbstractResult addInteger(IntegerResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected AbstractResult subtractInteger(IntegerResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected AbstractResult multiplyInteger(IntegerResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}

	protected AbstractResult addReal(RealResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected AbstractResult subtractReal(RealResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected AbstractResult multiplyReal(RealResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}


	protected AbstractResult divideReal(RealResult that) {
		return that.undefinedError(DIVISION_STRING, this);
	}

	protected AbstractResult divideInteger(IntegerResult that) {
		return that.undefinedError(DIVISION_STRING, this);
	}

	protected AbstractResult addString(StringResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected AbstractResult addList(ListResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected AbstractResult subtractList(ListResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected AbstractResult multiplyList(CollectionResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, that);
	}

	protected AbstractResult addSet(SetResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected AbstractResult addRelation(RelationResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected AbstractResult addBool(BoolResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected AbstractResult subtractSet(SetResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected AbstractResult multiplySet(SetResult that) {
		return that.undefinedError(MULTIPLICATION_STRING, this);
	}

	protected AbstractResult addMap(MapResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected AbstractResult subtractRelation(RelationResult that) {
		return that.undefinedError(SUBTRACTION_STRING, this);
	}

	protected AbstractResult moduloReal(RealResult that) {
		return that.undefinedError(MODULO_STRING, this);
	}

	protected AbstractResult addTuple(TupleResult that) {
		return that.undefinedError(ADDITION_STRING, this);
	}

	protected AbstractResult moduloInteger(IntegerResult that) {
		return that.undefinedError(MODULO_STRING, this);
	}

	protected AbstractResult intersect(AbstractResult that) {
		return that.undefinedError(INTERSECTION_STRING, this);
	}

	protected AbstractResult intersectSet(SetResult that) {
		return that.undefinedError(INTERSECTION_STRING, this);
	}

	protected AbstractResult inSet(SetResult that) {
		return that.undefinedError(IN_STRING, this);
	}

	protected AbstractResult inList(ListResult that) {
		return that.undefinedError(IN_STRING, this);
	}

	protected AbstractResult notInSet(SetResult that) {
		return that.undefinedError(NOTIN_STRING, this);
	}

	protected AbstractResult notInList(ListResult that) {
		return that.undefinedError(NOTIN_STRING, this);
	}
	
	
	
}
