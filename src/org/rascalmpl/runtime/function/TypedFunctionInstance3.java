package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class TypedFunctionInstance3<R,A,B,C> extends TypedFunctionInstance {
	
	private final TypedFunction3<R,A,B,C> function;

	public TypedFunctionInstance3(TypedFunction3<R,A,B,C> function, Type ftype){
		super(ftype);
		this.function = function;
		assert ftype.isFunction() && ftype.getArity() == 3;
	}
	
	public R typedCall(A a, B b, C c) {
		return function.typedCall(a, b, c);
	}
	
	@SuppressWarnings("unchecked")
	public R call(IValue a, IValue b, IValue c) {
		return function.typedCall((A)a, (B)b, (C)c);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    return (T)function.typedCall((A)parameters[0], (B)parameters[1], (C)parameters[2]);
	}
}
