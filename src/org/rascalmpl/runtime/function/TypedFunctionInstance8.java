package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class TypedFunctionInstance8<R,A,B,C,D,E,F,G,H> extends TypedFunctionInstance {
	
	private final TypedFunction8<R,A,B,C,D,E,F,G,H> function;

	public TypedFunctionInstance8(TypedFunction8<R,A,B,C,D,E,F,G,H> function, Type ftype){
		super(ftype);
		this.function = function;
		assert ftype.isFunction() && ftype.getArity() == 8;
	}
	
	public R typedCall(A a, B b, C c, D d, E e, F f, G g, H h) {
		return function.typedCall(a, b, c, d, e, f, g, h);
	}
	
	@SuppressWarnings("unchecked")
	public R call(IValue a, IValue b, IValue c, IValue d, IValue e, IValue f, IValue g, IValue h) {
		return function.typedCall((A)a, (B)b, (C)c, (D)d, (E)e, (F) f, (G) g, (H) h);
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
        return (T)function.typedCall((A)parameters[0], (B)parameters[1], (C)parameters[2], (D)parameters[3], (E)parameters[4], (F)parameters[5], (G) parameters[6], (H) parameters[7]);
    }
}
