package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Arrays;
import java.util.Map;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.RascalValueFactory;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.impl.persistent.ValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class TypedFunctionInstance1<R extends IValue,A> extends TypedFunctionInstance {
	
	private final TypedFunction1<R,A> function;
	private final Type type_arg_0;

	public TypedFunctionInstance1(TypedFunction1<R,A> function, Type ftype){
		super(ftype);
		this.function = function;
		type_arg_0 = ftype.isFunction() ? type.getFieldType(0) : TypeFactory.getInstance().valueType();
			
	}
	
	public R typedCall(A a) {
		if(((IValue) a).getType().comparable(type_arg_0)){
			return function.typedCall(a);
		}
		throw RuntimeExceptionFactory.callFailed(ValueFactory.getInstance().list((IValue)a));
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
