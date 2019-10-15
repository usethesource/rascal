package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import io.usethesource.vallang.IValue;

public final class GuardedIValue {
	final boolean defined;
	final IValue value;
	
	GuardedIValue(){
		defined = false;
		value = null;
	}
	
	GuardedIValue(final IValue value){
		this.defined = true;
		this.value = value;
	}
	
	public final IValue getValue() {
		if(value == null) {
			  throw new RuntimeException("Cannot get value from uninitialized GuardedIValue");
		}
		return value;
	}
}
