package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class TypedFunctionInstance5<R,A,B,C,D,E> extends TypedFunctionInstance {
	
	private final TypedFunction5<R,A,B,C,D,E> function;

	public TypedFunctionInstance5(TypedFunction5<R,A,B,C,D,E> function, Type ftype){
		super(ftype);
		this.function = function;
	}
	
	public R typedCall(A a, B b, C c, D d, E e) {
		return function.typedCall(a, b, c, d, e);
	}
	
	@SuppressWarnings("unchecked")
	public R call(IValue a, IValue b, IValue c, IValue d, IValue e) {
		return function.typedCall((A)a, (B)b, (C)c, (D)d, (E)e);
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
        return (T)function.typedCall((A)parameters[0], (B)parameters[1], (C)parameters[2], (D)parameters[3], (E)parameters[4]);
    }
}
