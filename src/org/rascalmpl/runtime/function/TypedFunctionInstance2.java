package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.InternalCompilerError;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class TypedFunctionInstance2<R extends IValue,A,B> extends TypedFunctionInstance {
	
	private final TypedFunction2<R,A,B> function;

	public TypedFunctionInstance2(TypedFunction2<R,A,B> function, Type ftype){
		super(ftype);
		this.function = function;
		assert ftype.isFunction() && ftype.getArity() == 2;
	}
	
	public R typedCall(A a, B b) {
		return function.typedCall(a, b);
	}
	
	@SuppressWarnings("unchecked")
	public R call(IValue a, IValue b) {
		return function.typedCall((A)a, (B)b);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    return (T)function.typedCall((A)parameters[0], (B)parameters[1]);
	}
}
