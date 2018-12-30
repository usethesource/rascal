package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

public class FunctionInstance4<R,A,B,C,D> extends FunctionInstance {
	
	private final Function4<R,A,B,C,D> function;

	public FunctionInstance4(Function4<R,A,B,C,D> function){
		this.function = function;
	}
	
	public R call(A a, B b, C c, D d) {
		return function.call(a, b, c, d);
	}
}
