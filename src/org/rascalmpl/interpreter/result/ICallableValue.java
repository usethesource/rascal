package org.rascalmpl.interpreter.result;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IRascalMonitor;

public interface ICallableValue extends IValue {
	public Result<IValue> call(IRascalMonitor monitor, Type[] argTypes, IValue[] argValues);
	public Result<IValue> call(Type[] argTypes, IValue[] argValues);
}
