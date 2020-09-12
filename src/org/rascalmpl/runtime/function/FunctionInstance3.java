package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;

public class FunctionInstance3<R,A,B,C> extends FunctionInstance {
	
	private final Function3<R,A,B,C> function;

	public FunctionInstance3(Function3<R,A,B,C> function){
		this.function = function;
	}
	
//	public R call(A a, B b, C c) {
//		return function.call(a, b, c);
//	}
	
	@SuppressWarnings("unchecked")
	public R call(IValue a, IValue b, IValue c) {
		return function.call((A)a, (B)b, (C)c);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    return (T)function.call((A)parameters[0], (B)parameters[1], (C)parameters[2]);
	}
}
