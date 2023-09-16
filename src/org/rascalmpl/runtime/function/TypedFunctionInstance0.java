package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class TypedFunctionInstance0<R extends IValue> extends TypedFunctionInstance {
	
	private final TypedFunction0<R> function;

	public TypedFunctionInstance0(TypedFunction0<R> function, Type ftype){
		super(ftype);
		if(validating) {
			assert ftype.isFunction() ? ftype.getArity() == 0 : true;
		}
		this.function = function;
	}

	public R typedCall() {
		return function.typedCall();
	}

	@SuppressWarnings("unchecked")
    @Override
	public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    return (T) function.typedCall();
	}
	
}
