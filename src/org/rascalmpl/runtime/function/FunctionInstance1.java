package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

public class FunctionInstance1<R,A> extends FunctionInstance {
	
	private final Function1<R,A> function;

	public FunctionInstance1(Function1<R,A> function){
		this.function = function;
	}
	
	public R call(A a) {
		return function.call(a);
	}
}
