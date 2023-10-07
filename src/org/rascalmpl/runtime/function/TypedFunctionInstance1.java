package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class TypedFunctionInstance1<R extends IValue,A> extends TypedFunctionInstance {
	
	private final TypedFunction1<R,A> function;

	public TypedFunctionInstance1(TypedFunction1<R,A> function, Type ftype){
		super(ftype);
		assert ftype.isFunction() ? ftype.getArity() == 1 : true;
		this.function = function;
	}
	
	public R typedCall(A a) {
		return function.typedCall(a);
		
	}
	
	@SuppressWarnings("unchecked")
	public R call(IValue a) {
		return function.typedCall((A)a);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    return (T)function.typedCall((A)parameters[0]);
	}
}