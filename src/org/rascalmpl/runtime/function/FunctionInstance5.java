package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

public class FunctionInstance5<R,A,B,C,D,E> extends FunctionInstance {
	
	private final Function5<R,A,B,C,D,E> function;

	public FunctionInstance5(Function5<R,A,B,C,D,E> function){
		this.function = function;
	}
	
	public R call(A a, B b, C c, D d, E e) {
		return function.call(a, b, c, d, e);
	}
}
