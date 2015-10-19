package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.value.IAnnotatable;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;

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
	public boolean declaresAnnotation(TypeStore store, String label) {
		return getConstructor().declaresAnnotation(store, label);
	}

	@Override
	public IAnnotatable<? extends IConstructor> asAnnotatable() {
		return getConstructor().asAnnotatable();
	}
	
	@Override
	public IWithKeywordParameters<? extends IConstructor> asWithKeywordParameters() {
		return new WithKeywordParametersCursor(getConstructor().asWithKeywordParameters(), getCtx());
	}
}
