package org.meta_environment.rascal.interpreter.env;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.exceptions.RascalBug;
import org.meta_environment.rascal.interpreter.exceptions.RascalTypeError;

public class Result {
	public Type type;
	public IValue value;
	private boolean isPublic = false;


	protected Result() { }
	
	public Result(Type t, IValue v) {
		type = t;
		value = v;
		if (value != null && !value.getType().isSubtypeOf(t)) {
			throw new RascalTypeError("Type " + v.getType() + " is not a subtype of expected type "
					+ t);
		}
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
		if(type.isBoolType()){
			return ((IBool)value).getValue();
		} else {
			return false;
		}	
	}
	
	/*
	 * Experimental extension for backtracking over Boolean expressions
	 */
	
	public boolean hasNext(){
		return false;
	}
	
	public Result next(){
		throw new RascalBug("next() cannot be called on a standard Result");
	}
}


