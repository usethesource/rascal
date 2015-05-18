package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IRascalMonitor;

public interface ICallableCompiledValue {

	public IValue call(IRascalMonitor monitor, Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues);
	
	public IValue call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues);
}
