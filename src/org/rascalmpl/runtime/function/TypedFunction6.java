package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

@FunctionalInterface
public interface TypedFunction6<R, A, B, C, D, E, F> {
	public R typedCall(final A a, final B b, final C c, final D d, final E e, final F f);
}