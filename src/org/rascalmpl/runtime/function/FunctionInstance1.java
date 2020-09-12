package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;

public class FunctionInstance1<R extends IValue,A> extends FunctionInstance {
	
	private final Function1<R,A> function;

	public FunctionInstance1(Function1<R,A> function){
		this.function = function;
	}
	
//	public R call(A a) {
//		return function.call(a);
//	}
	
	@SuppressWarnings("unchecked")
	public R call(IValue a) {
		return function.call((A)a);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    return (T)function.call((A)parameters[0]);
	}
}
