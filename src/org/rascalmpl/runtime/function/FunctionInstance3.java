package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;

public class FunctionInstance3<R,A,B,C> extends FunctionInstance {
	
	private final Function3<R,A,B,C> function;

	public FunctionInstance3(Function3<R,A,B,C> function){
		this.function = function;
	}
	
	public R call(A a, B b, C c) {
		return function.call(a, b, c);
	}
	
	@Override
	public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    return call(parameters[0], parameters[1], parameters[2]);
	}
}
