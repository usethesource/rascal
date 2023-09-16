package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.impl.persistent.ValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class TypedFunctionInstance4<R,A,B,C,D> extends TypedFunctionInstance {
	
	private final TypedFunction4<R,A,B,C,D> function;
	private final Type type_arg_0;
	private final Type type_arg_1;
	private final Type type_arg_2;
	private final Type type_arg_3;

	public TypedFunctionInstance4(TypedFunction4<R,A,B,C,D> function, Type ftype){
		super(ftype);
		this.function = function;
		if(ftype.isFunction()) {
			assert ftype.getArity() == 4;
			type_arg_0 = type.getFieldType(0);
			type_arg_1 = type.getFieldType(1);
			type_arg_2 = type.getFieldType(2);
			type_arg_3 = type.getFieldType(3);
		} else {
			type_arg_0 = type_arg_1 = type_arg_2 = type_arg_3 = TypeFactory.getInstance().valueType();
		}
	}
	
	public R typedCall(A a, B b, C c, D d) {
		if(validating &&
		   !(((IValue) a).getType().comparable(type_arg_0) &&
		     ((IValue) b).getType().comparable(type_arg_1) &&
		     ((IValue) c).getType().comparable(type_arg_2) &&
		     ((IValue) d).getType().comparable(type_arg_3))){
			throw RuntimeExceptionFactory.callFailed(ValueFactory.getInstance().list((IValue)a, (IValue)b, (IValue)c, (IValue)d));
	    }
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
