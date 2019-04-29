package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import io.usethesource.vallang.IValue;

public final class ValueRef<T extends IValue> {
	public T value;

	public ValueRef(T value) {
		this.value = value;
	}
	
	public ValueRef(ValueRef<T> value) {
		this.value = value.value;
	}
}