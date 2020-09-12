package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;

public class FunctionInstance5<R,A,B,C,D,E> extends FunctionInstance {
	
	private final Function5<R,A,B,C,D,E> function;

	public FunctionInstance5(Function5<R,A,B,C,D,E> function){
		this.function = function;
	}
	
//	public R call(A a, B b, C c, D d, E e) {
//		return function.call(a, b, c, d, e);
//	}
	
	@SuppressWarnings("unchecked")
	public R call(IValue a, IValue b, IValue c, IValue d, IValue e) {
		return function.call((A)a, (B)b, (C)c, (D)d, (E)e);
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
        return (T)function.call((A)parameters[0], (B)parameters[1], (C)parameters[2], (D)parameters[3], (E)parameters[4]);
    }
}
