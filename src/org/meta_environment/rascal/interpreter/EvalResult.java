package org.meta_environment.rascal.interpreter;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;

public class EvalResult {
	protected Type type;
	protected IValue value;

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
}
