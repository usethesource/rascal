package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;

public class FunctionInstance4<R,A,B,C,D> extends FunctionInstance {
	
	private final Function4<R,A,B,C,D> function;

	public FunctionInstance4(Function4<R,A,B,C,D> function){
		this.function = function;
	}
	
//	public R call(A a, B b, C c, D d) {
//		return function.call(a, b, c, d);
//	}
	
	@SuppressWarnings("unchecked")
	public R call(IValue a, IValue b, IValue c, IValue d) {
		return function.call((A)a, (B)b, (C)c, (D)d);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    return (T)function.call((A)parameters[0], (B)parameters[1], (C)parameters[2], (D)parameters[3]);
	}
}
