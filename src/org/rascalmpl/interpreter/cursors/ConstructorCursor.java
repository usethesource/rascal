package org.rascalmpl.interpreter.cursors;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.Type;

public class ConstructorCursor extends NodeCursor implements IConstructor {

	public ConstructorCursor(IConstructor value) {
		super(value);
	}
	
	public ConstructorCursor(IConstructor value, Context ctx) {
		super(value, ctx);
	}
	
	IConstructor getConstructor() {
		return (IConstructor) getWrappedValue();
	}

	@Override
	public Type getConstructorType() {
		return getConstructor().getConstructorType();
	}

	@Override
	public Type getUninstantiatedConstructorType() {
		return getConstructor().getUninstantiatedConstructorType();
	}

	@Override
	public IValue get(String label) {
		Context ctx = new ConstructorLabelContext(getCtx(), label, getConstructor());
		return CursorFactory.makeCursor(getConstructor().get(label), ctx);
	}

	@Override
	public IConstructor set(String label, IValue newChild) throws FactTypeUseException {
		return new ConstructorCursor(getConstructor().set(label, newChild), getCtx());
	}

	@Override
	public boolean has(String label) {
		return getConstructor().has(label);
	}

	@Override
	public IConstructor set(int index, IValue newChild) throws FactTypeUseException {
		return new ConstructorCursor(getConstructor().set(index, newChild), getCtx());
	}

	@Override
	public Type getChildrenTypes() {
		return getConstructor().getChildrenTypes();
	}

	@Override
	public IWithKeywordParameters<? extends IConstructor> asWithKeywordParameters() {
		return new WithKeywordParametersCursor(getConstructor().asWithKeywordParameters(), getCtx());
	}
}
