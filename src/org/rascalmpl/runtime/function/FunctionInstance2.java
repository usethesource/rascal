package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

public class FunctionInstance2<R,A,B> extends FunctionInstance {
	
	private final Function2<R,A,B> function;

	public FunctionInstance2(Function2<R,A,B> function){
		this.function = function;
	}
	
	public R call(A a, B b) {
		return function.call(a, b);
	}
}
