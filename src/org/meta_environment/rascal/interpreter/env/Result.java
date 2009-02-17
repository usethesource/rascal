package org.meta_environment.rascal.interpreter.env;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
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
		this(TypeFactory.getInstance().boolType(), ValueFactory.getInstance().bool(b));
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
}


