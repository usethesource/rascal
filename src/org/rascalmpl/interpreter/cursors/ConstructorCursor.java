package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IWithKeywordParameters;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;

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
	public IWithKeywordParameters<IConstructor> asWithKeywordParameters() {
		return getConstructor().asWithKeywordParameters();
	}
}
