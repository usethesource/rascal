package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

@FunctionalInterface
public interface TypedFunction3<R, A, B, C> {
	public R typedCall(final A a, final B b, final C c);
}