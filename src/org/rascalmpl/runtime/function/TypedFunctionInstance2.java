package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.impl.persistent.ValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class TypedFunctionInstance2<R extends IValue,A,B> extends TypedFunctionInstance {
	
	private final TypedFunction2<R,A,B> function;
	private final Type type_arg_0;
	private final Type type_arg_1;

	public TypedFunctionInstance2(TypedFunction2<R,A,B> function, Type ftype){
		super(ftype);
		this.function = function;
		if(ftype.isFunction()) {
			assert type.getArity() == 2;
			type_arg_0 = type.getFieldType(0);
			type_arg_1 = type.getFieldType(1);
		} else {
			type_arg_0 = type_arg_1 = TypeFactory.getInstance().valueType();
		}
	}
	
	public R typedCall(A a, B b) {
		if(validating && 
		    !(((IValue) a).getType().comparable(type_arg_0) &&
		      ((IValue) b).getType().comparable(type_arg_1))){
			throw RuntimeExceptionFactory.callFailed(ValueFactory.getInstance().list((IValue)a, (IValue)b));
		}
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
