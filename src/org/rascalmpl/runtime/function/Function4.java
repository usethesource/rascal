package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

@FunctionalInterface
public interface Function4<R, A, B, C, D> {
	public R call(A a, B b, C c, D d);
}