package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

@FunctionalInterface
public interface TypedFunction8<R, A, B, C, D, E, F, G, H> {
	public R typedCall(final A a, final B b, final C c, final D d, final E e, final F f, final G g, final H h);
}