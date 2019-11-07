package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import io.usethesource.vallang.IValue;

public final class ValueRef<T extends IValue> {
	private T value;
	private final ValueRef<T> baseRef;

	public ValueRef(T value) {
		baseRef = null;
		this.value = value;
	}
	
	public ValueRef(ValueRef<T> value) {
	    baseRef = value;
	}
	
	public T getValue() {
		if(baseRef == null) {
			return this.value;
		}
		return baseRef.getValue();
	}
	
	public void setValue(T value) {
		if(baseRef == null) {
			this.value = value;
			return;
		}
		baseRef.setValue(value);
	}
}