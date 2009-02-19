package org.meta_environment.rascal.interpreter.result;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ValueFactoryFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;
import org.meta_environment.rascal.interpreter.errors.TypeError;

public class Result  implements Iterator<Result>{
	protected Type type;          // The declared type of the Result
	
	public Type getType(){
		return type;
	}
	
	public void setType(Type t){
		type = t;
	}
	
	protected IValue value;        // The actual value of the Result
	
	public IValue getValue(){
		return value;
	}
	
	public void setValue(IValue v){
		value = v;
	}
	
	public Type getValueType(){
		//System.err.println("getValueType on Result(" + type + ", " + value +")");
		if(iterator != null)
			return TypeFactory.getInstance().boolType();
		else if(value != null)
			return value.getType();
		else
			return null;	
	}
	
	private boolean isPublic = false;
	
	protected Iterator<Result> iterator;     // An optional IValue iterator
	private Result last;          // The last IValue returned by the iterator

	protected Result() { }
	
	public Result(Type t, IValue v) {
		//System.err.println("Result(" + t + ", " + v + ")");
		type = t;
		value = v;
		if (value != null && !value.getType().isSubtypeOf(t)) {
			throw new TypeError("Type " + v.getType() + " is not a subtype of expected type "
					+ t);
		}
		iterator = null;
		last = null;
	}
	
	public Result(Iterator<Result> beval, boolean b){
		this(TypeFactory.getInstance().boolType(), ValueFactoryFactory.getValueFactory().bool(b));
		this.iterator = beval;
	}
	
	public Result(Iterator<Result> beval){
		this(beval, true);
	}
	
	
	public Type getDeclaredType(){
		return type;
	}
	
	public String toString() {
		return "EResult(" + type + ", " + value + ")";
	}
	
	public boolean isNormal() {
		return true;
	}
	
	public boolean isClosure() {
		return false;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean val) {
		isPublic = val;
	}
	
	public boolean isTrue(){
		if(iterator == null){
			if(type.isBoolType()){
				return ((IBool)value).getValue();
			} else {
				return false;
			}
		} else {
			return (last == null) || last.isTrue();
		}
	}
	
	public boolean hasNext(){
		return iterator != null && iterator.hasNext();
	}
	
	public Result next(){
		if(iterator == null){
			new ImplementationError("next called on Result with null iterator");
		}
		return last = iterator.next();
	}

	public void remove() {
		throw new ImplementationError("remove() not implemented for Result");
		
	}


	/*
	 * Double dispatch stuff.
	 */
	

	// TODO make these abstract
	protected Result addInteger(IntegerResult n) {
		throw new ImplementationError("NIY");
	}

	protected Result reverseSubtractInteger(IntegerResult integerResult) {
		return null;
	}

	public Result add(Result result) {
		// TODO throw type error
		return null;
	}

	public Result subtract(Result result) {
		// TODO throw type error
		return null;
	}

	public Result multiply(Result result) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result multiplyInteger(IntegerResult integerResult) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result addReal(RealResult n) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result reverseSubtractReal(RealResult n) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result multiplyReal(RealResult n) {
		// TODO Auto-generated method stub
		return null;
	}

	public Result divide(Result result) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result reverseDivideReal(RealResult realResult) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result reverseDivideInteger(IntegerResult integerResult) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result addString(StringResult s) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result addList(ListResult l) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result addSet(SetResult s) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result addRelation(RelationResult r) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result addBool(BoolResult n) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result subtractSet(SetResult s) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result illegalArguments(String op, AbstractAST ast, Type type1, Type type2) {
		throw new TypeError("Operands of " + op + " have illegal types: " + type1 + ", " + type2, ast);
	}

	protected void checkElementType(String op, AbstractAST ast, Type elemType) {
		if (!elemType.isSubtypeOf(getType().getElementType())) {
			illegalArguments(op, ast, getType(), elemType);
		}
	}

	protected Result multiplySet(SetResult setResult) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result addMap(MapResult m) {
		// TODO Auto-generated method stub
		return null;
	}




}


