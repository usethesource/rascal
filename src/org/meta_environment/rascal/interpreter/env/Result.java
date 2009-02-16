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
	public Type type;
	public IValue value;
	private boolean isPublic = false;
	Iterator<Result> iterator;
	private Result last;

	protected Result() { }
	
	public Result(Type t, IValue v) {
		type = t;
		value = v;
		if (value != null && !value.getType().isSubtypeOf(t)) {
			throw new TypeError("Type " + v.getType() + " is not a subtype of expected type "
					+ t);
		}
		iterator = null;
		last = null;
	}
	
	Result(Iterator<Result> beval){
		this(TypeFactory.getInstance().boolType(), null);
		this.iterator = beval;
	}
	
	public Result(Iterator<Result> beval, boolean b){
		this(TypeFactory.getInstance().boolType(), ValueFactory.getInstance().bool(b));
		this.iterator = beval;
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


