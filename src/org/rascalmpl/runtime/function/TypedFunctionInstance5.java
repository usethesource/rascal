package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.impl.persistent.ValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class TypedFunctionInstance5<R,A,B,C,D,E> extends TypedFunctionInstance {
	
	private final TypedFunction5<R,A,B,C,D,E> function;
	private final Type type_arg_0;
	private final Type type_arg_1;
	private final Type type_arg_2;
	private final Type type_arg_3;
	private final Type type_arg_4;

	public TypedFunctionInstance5(TypedFunction5<R,A,B,C,D,E> function, Type ftype){
		super(ftype);
		this.function = function;
		if(ftype.isFunction()) {
			type_arg_0 = type.getFieldType(0);
			type_arg_1 = type.getFieldType(1);
			type_arg_2 = type.getFieldType(2);
			type_arg_3 = type.getFieldType(3);
			type_arg_4 = type.getFieldType(4);
		} else {
			type_arg_0 = type_arg_1 = type_arg_2 = type_arg_3 = type_arg_4 =TypeFactory.getInstance().valueType();
		}
	}
	
	public R typedCall(A a, B b, C c, D d, E e) {
		if(((IValue) a).getType().comparable(type_arg_0) &&
		   ((IValue) b).getType().comparable(type_arg_1) &&
		   ((IValue) c).getType().comparable(type_arg_2) &&
		   ((IValue) d).getType().comparable(type_arg_3) &&
		   ((IValue) e).getType().comparable(type_arg_4)){
				return function.typedCall(a, b, c, d, e);
		}
		throw RuntimeExceptionFactory.callFailed(ValueFactory.getInstance().list((IValue)a, (IValue)b, (IValue)c, (IValue)d, (IValue)e));
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
