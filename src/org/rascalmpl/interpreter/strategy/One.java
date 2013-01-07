/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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
package org.rascalmpl.interpreter.strategy;

import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.Result;

public class One extends AbstractStrategy {
	private final IVisitable v;

	public One(AbstractFunction function, IVisitable v) {
		super(function);
		this.v = v;
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, Result<IValue>> keyArgValues) {
		IValue res = argValues[0];
		boolean entered = v.init(res);
		
		for (int i = 0; i < v.getChildrenNumber(res); i++) {
			IValue child = v.getChildAt(res, i);
			v.mark(child);
			if(v instanceof IContextualVisitable) {
				IContextualVisitable cv = (IContextualVisitable) v;
				IValue oldctx = cv.getContext().getValue();
				IValue newchild = function.call(new Type[]{child.getType()}, new IValue[]{child}, null).getValue();
				IValue newctx = cv.getContext().getValue();
				if (! oldctx.isEqual(newctx)) {
					res = v.setChildAt(res, i, newchild);
					break;
				}
			} else {
				IValue newchild = function.call(new Type[]{child.getType()}, new IValue[]{child}, null).getValue();
				if (! newchild.isEqual(child)) {
					res = v.setChildAt(res, i, newchild);
					break;
				}
			}
		}
		
		if(entered){
			getEvaluatorContext().popStrategyContext(); // Exit the context.
		}
		
		return makeResult(res, ctx);
	}
}
