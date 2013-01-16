/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.strategy.topological;

import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.strategy.IContextualVisitable;
import org.rascalmpl.interpreter.strategy.IStrategyContext;

public class TopologicalVisitable implements IContextualVisitable {
	private final AbstractFunction function;

	public TopologicalVisitable(AbstractFunction function) {
		this.function = function;
	}

	public IValue getChildAt(IValue v, int i) throws IndexOutOfBoundsException {
		return getContext().getChildren(v).get(i);
	}

	public <T extends IValue> T setChildAt(T v, int i, IValue newchild) throws IndexOutOfBoundsException {
		if (v instanceof IRelation) return (T) getContext().getValue();
		IValue oldchild = getChildAt(v,i);
		getContext().update(oldchild, newchild);
		return v;
	}


	public int getChildrenNumber(IValue v) {
		List<IValue> children = getContext().getChildren(v);
		if (children == null) {
			throw new RuntimeException("Unexpected value "+v+" in the context "+getContext().getValue());
		}
		return children.size();
	}


	public <T extends IValue> T setChildren(T v, List<IValue> children) throws IndexOutOfBoundsException {
		if (v instanceof IRelation) return (T) getContext().getValue();
		return v;
	}

	public IStrategyContext getContext() {
		return function.getEvaluatorContext().getStrategyContext();
	}

	public boolean init(IValue v) {
		if(v instanceof IRelation){
			//initialize a new context
			//TODO: manage a stack of contexts
			IRelation relation = ((IRelation) v);
			IStrategyContext context = new TopologicalContext(relation);
			function.getEvaluatorContext().pushStrategyContext(context);
			return true;
		}
		return false;
	}
	
	public void mark(IValue v){
		((TopologicalContext) getContext()).mark(v);
	}
}
