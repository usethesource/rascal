package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

@FunctionalInterface
public interface Function3<R, A, B, C> {
	public R call(A a, B b, C c);
}