package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class TypedFunctionInstance6<R,A,B,C,D,E,F> extends TypedFunctionInstance {
	
	private final TypedFunction6<R,A,B,C,D,E,F> function;

	public TypedFunctionInstance6(TypedFunction6<R,A,B,C,D,E,F> function, Type ftype){
		super(ftype);
		this.function = function;
		assert ftype.isFunction() && ftype.getArity() == 6;
	}
	
	public R typedCall(A a, B b, C c, D d, E e, F f) {
		return function.typedCall(a, b, c, d, e, f);
	}
	
	@SuppressWarnings("unchecked")
	public R call(IValue a, IValue b, IValue c, IValue d, IValue e, IValue f) {
		return function.typedCall((A)a, (B)b, (C)c, (D)d, (E)e, (F) f);
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
        return (T)function.typedCall((A)parameters[0], (B)parameters[1], (C)parameters[2], (D)parameters[3], (E)parameters[4], (F)parameters[5]);
    }
}
