package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import io.usethesource.vallang.IAnnotatable;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.exceptions.IllegalOperationException;
import io.usethesource.vallang.impl.AbstractExternalValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.visitors.IValueVisitor;

public class FunctionInstance implements IExternalValue {
	
	final TypeFactory $TF = TypeFactory.getInstance();
	
	// IExternalValue methods
	
	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return v.visitExternal((IExternalValue) this);
	}

	@Override
	public boolean isEqual(IValue other) {
		return this == other;
	}

	@Override
	public boolean match(IValue other) {
		 return this == other;
	}

	@Override
	public boolean isAnnotatable() {
		return false;
	}

	@Override
	public IAnnotatable<? extends IValue> asAnnotatable() {
		throw new IllegalOperationException("Cannot be viewed as annotatable", getType());

	}

	@Override
	public boolean mayHaveKeywordParameters() {
		return false; // TODO
	}

	@Override
	public IWithKeywordParameters<? extends IValue> asWithKeywordParameters() {
		throw new IllegalOperationException("Cannot be viewed as with keyword parameters", getType());
	}

	@Override
	public Type getType() {
		return $TF.valueType(); //TODO
	}

	@Override
	public IConstructor encodeAsConstructor() {
		return AbstractExternalValue.encodeAsConstructor(this);
	}

}
