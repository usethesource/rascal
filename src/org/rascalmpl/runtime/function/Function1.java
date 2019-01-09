package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

@FunctionalInterface
public interface Function1<R,A> {
	public R call(final A a);
}