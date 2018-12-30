package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

@FunctionalInterface
public interface Function5<R, A, B, C, D, E> {
	public R call(A a, B b, C c, D d, E e);
}