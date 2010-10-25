package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;

public abstract class AbstractBooleanResult implements IBooleanResult {
	protected boolean initialized = false;
	protected boolean hasNext = true;
	protected final TypeFactory tf = TypeFactory.getInstance();
	protected final IEvaluatorContext ctx;
	
	public AbstractBooleanResult(IEvaluatorContext ctx) {
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