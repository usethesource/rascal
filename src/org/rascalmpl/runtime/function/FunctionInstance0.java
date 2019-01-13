package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

public class FunctionInstance0<R> extends FunctionInstance {
	
	private final Function0<R> function;

	public FunctionInstance0(Function0<R> function){
		this.function = function;
	}

	public R call() {
		return function.call();
	}
}
