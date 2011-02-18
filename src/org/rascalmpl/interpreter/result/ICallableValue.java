package org.rascalmpl.interpreter.result;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;

public interface ICallableValue extends IValue {
	public Result<IValue> call(Type[] argTypes, IValue[] argValues);
}
