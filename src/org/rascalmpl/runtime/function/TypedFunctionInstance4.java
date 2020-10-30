package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class TypedFunctionInstance4<R,A,B,C,D> extends TypedFunctionInstance {
	
	private final TypedFunction4<R,A,B,C,D> function;

	public TypedFunctionInstance4(TypedFunction4<R,A,B,C,D> function, Type ftype){
		super(ftype);
		this.function = function;
	}
	
	public R typedCall(A a, B b, C c, D d) {
		return function.typedCall(a, b, c, d);
	}
	
	@SuppressWarnings("unchecked")
	public R call(IValue a, IValue b, IValue c, IValue d) {
		return function.typedCall((A)a, (B)b, (C)c, (D)d);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    return (T)function.typedCall((A)parameters[0], (B)parameters[1], (C)parameters[2], (D)parameters[3]);
	}
}
