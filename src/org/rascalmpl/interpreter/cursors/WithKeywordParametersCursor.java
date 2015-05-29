package org.rascalmpl.interpreter.cursors;

import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IWithKeywordParameters;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;

public class WithKeywordParametersCursor extends Cursor implements IWithKeywordParameters<IConstructor> {

	private IWithKeywordParameters<? extends IConstructor> kwp;

	public WithKeywordParametersCursor(IWithKeywordParameters<? extends IConstructor> kwp) {
		super(null);
		this.kwp = kwp;
	}

	public WithKeywordParametersCursor(IWithKeywordParameters<? extends IConstructor> kwp, Context ctx) {
		super(null, ctx);
		this.kwp = kwp;
	}
	

	private IWithKeywordParameters<? extends IConstructor> getKWP() {
		return kwp;
	}

	@Override
	public IValue getParameter(String label) throws FactTypeUseException {
		Context ctx = new WithKeywordParametersContext(getCtx(), label, getKWP());
		return CursorFactory.makeCursor(getKWP().getParameter(label), ctx);
	}

	@Override
	public IConstructor setParameter(String label, IValue newValue)
			throws FactTypeUseException {
		return new ConstructorCursor(getKWP().setParameter(label, newValue), getCtx());
	}

	@Override
	public boolean hasParameter(String label) throws FactTypeUseException {
		return getKWP().hasParameter(label);
	}

	@Override
	public boolean hasParameters() {
		return getKWP().hasParameters();
	}

	@Override
	public Set<String> getParameterNames() {
		return getKWP().getParameterNames();
	}

	@Override
	public Map<String, IValue> getParameters() {
		return getKWP().getParameters();
	}

	@Override
	public IConstructor unsetParameter(String label) {
		// TODO: @tijs check this?
		return new ConstructorCursor(getKWP().unsetParameter(label));
	}
	
	@Override
	public IConstructor unsetAll() {
		// TODO: @tijs check this?
		return new ConstructorCursor(getKWP().unsetAll());
	}
	
	@Override
	public IConstructor setParameters(Map<String, IValue> params) {
		return new ConstructorCursor(getKWP().setParameters(params), getCtx());
	}

	@Override
	public <U extends IWithKeywordParameters<? extends IValue>> boolean equalParameters( U other) {
		return getKWP().equalParameters(other);
	}

}
