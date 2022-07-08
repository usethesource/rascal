package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.impl.persistent.ValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class TypedFunctionInstance3<R,A,B,C> extends TypedFunctionInstance {
	
	private final TypedFunction3<R,A,B,C> function;
	private final Type type_arg_0;
	private final Type type_arg_1;
	private final Type type_arg_2;

	public TypedFunctionInstance3(TypedFunction3<R,A,B,C> function, Type ftype){
		super(ftype);
		this.function = function;
		if(ftype.isFunction()) {
			assert ftype.getArity() == 3;
			type_arg_0 = type.getFieldType(0);
			type_arg_1 = type.getFieldType(1);
			type_arg_2 = type.getFieldType(2);
		} else {
			type_arg_0 = type_arg_1 = type_arg_2 = TypeFactory.getInstance().valueType();
		}
	}
	
	public R typedCall(A a, B b, C c) {
		if(((IValue) a).getType().comparable(type_arg_0) &&
		   ((IValue) b).getType().comparable(type_arg_1) &&
		   ((IValue) c).getType().comparable(type_arg_2)){
				return function.typedCall(a, b, c);
		}
		throw RuntimeExceptionFactory.callFailed(ValueFactory.getInstance().list((IValue)a, (IValue)b, (IValue)c));
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
