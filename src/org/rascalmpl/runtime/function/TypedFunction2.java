package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

@FunctionalInterface
public interface TypedFunction2<R, A, B> {
	public R typedCall(final A a, final B b);
}