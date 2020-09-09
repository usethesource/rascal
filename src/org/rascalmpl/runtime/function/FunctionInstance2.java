package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;

public class FunctionInstance2<R extends IValue,A,B> extends FunctionInstance {
	
	private final Function2<R,A,B> function;

	public FunctionInstance2(Function2<R,A,B> function){
		this.function = function;
	}
	
	public R call(A a, B b) {
		return function.call(a, b);
	}
	
	@Override
	public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    return call(parameters[0], parameters[1]);
	}
}
