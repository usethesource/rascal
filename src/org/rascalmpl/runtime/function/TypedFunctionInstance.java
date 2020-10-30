package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.visitors.IValueVisitor;

public abstract class TypedFunctionInstance implements IFunction {
	protected final TypeFactory $TF = TypeFactory.getInstance();
    private final Type type;
	
	public TypedFunctionInstance(Type type) {
	    this.type = type;
    }
	
	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return v.visitExternal((IExternalValue) this);
	}

	@Override
	public boolean match(IValue other) {
		 return this == other;
	}
	
	@Override
	public boolean equals(Object obj) {
	    return this == obj;
	}


	@Override
	public Type getType() {
	    return type;
	}

    @Override
    abstract public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters);
}
