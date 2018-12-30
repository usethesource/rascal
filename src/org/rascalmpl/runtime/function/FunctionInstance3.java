package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

public class FunctionInstance3<R,A,B,C> extends FunctionInstance {
	
	private final Function3<R,A,B,C> function;

	public FunctionInstance3(Function3<R,A,B,C> function){
		this.function = function;
	}
	
	public R call(A a, B b, C c) {
		return function.call(a, b, c);
	}
}
