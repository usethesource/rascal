package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.util.Map;

import org.rascalmpl.debug.IRascalMonitor;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public interface ICallableCompiledValue {

	public IValue call(IRascalMonitor monitor, Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues);
	
	public IValue call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues);
}
