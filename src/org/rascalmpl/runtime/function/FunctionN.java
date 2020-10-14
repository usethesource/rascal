package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import io.usethesource.vallang.IValue;

@FunctionalInterface
public interface FunctionN<R> {
	public R call(IValue... parameters);
}