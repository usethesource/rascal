package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.Map;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

public interface ICallableCompiledValue {

	public IValue call(IRascalMonitor monitor, Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues);
	
	public IValue call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues);
}
