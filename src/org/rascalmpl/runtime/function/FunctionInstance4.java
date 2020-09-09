package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;

public class FunctionInstance4<R,A,B,C,D> extends FunctionInstance {
	
	private final Function4<R,A,B,C,D> function;

	public FunctionInstance4(Function4<R,A,B,C,D> function){
		this.function = function;
	}
	
	public R call(A a, B b, C c, D d) {
		return function.call(a, b, c, d);
	}
	
	@Override
	public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    return call(parameters[0], parameters[1], parameters[2], parameters[3]);
	}
}
