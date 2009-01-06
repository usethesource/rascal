package org.meta_environment.rascal.interpreter.env;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.RascalBug;
import org.meta_environment.rascal.interpreter.RascalTypeError;

public class EvalResult {
	public Type type;
	public IValue value;

	protected EvalResult() { }
	
	public EvalResult(Type t, IValue v) {
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
	
	/*
	 * Experimental extension for backtracking over Boolean expressions
	 */
	
	public boolean hasNext(){
		return false;
	}
	
	public EvalResult next(){
		throw new RascalBug("next() cannot be called on a standard EvalResult");
	}
}
