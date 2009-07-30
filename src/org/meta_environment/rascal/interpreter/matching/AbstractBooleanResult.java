package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public abstract class AbstractBooleanResult implements IBooleanResult {
	protected boolean initialized = false;
	protected boolean hasNext = true;
	protected final TypeFactory tf = TypeFactory.getInstance();
	protected final IValueFactory vf;
	protected final IEvaluatorContext ctx;
	
	public AbstractBooleanResult(IValueFactory vf, IEvaluatorContext ctx) {
		this.vf = vf;
		this.ctx = ctx;
	}
	
	public void init() {
		this.initialized = true;
		this.hasNext = true;
	}
	
	public boolean hasNext() {
		return initialized && hasNext;
	}
	
	abstract public boolean next();
}