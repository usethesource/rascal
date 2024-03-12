package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

@FunctionalInterface
public interface TypedFunction7<R, A, B, C, D, E, F, G> {
	public R typedCall(final A a, final B b, final C c, final D d, final E e, final F f, final G g);
}