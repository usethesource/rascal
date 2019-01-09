package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

@FunctionalInterface
public interface Function4<R, A, B, C, D> {
	public R call(final A a, final B b, final C c, final D d);
}