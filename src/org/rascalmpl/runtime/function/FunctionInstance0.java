package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;

public class FunctionInstance0<R extends IValue> extends FunctionInstance {
	
	private final Function0<R> function;

	public FunctionInstance0(Function0<R> function){
		this.function = function;
	}

	public R call() {
		return function.call();
	}

	@SuppressWarnings("unchecked")
    @Override
	public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    return (T) call();
	}
	
}
