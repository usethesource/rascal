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
package org.rascalmpl.interpreter.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.Result;

public class All extends AbstractStrategy {
	private final IVisitable v;

	public All(AbstractFunction function, IVisitable v) {
		super(function);
		this.v = v;
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, Result<IValue>> keyArgValues) {
		IValue res = argValues[0];
		boolean entered = v.init(res);
		
		List<IValue> newchildren = new ArrayList<IValue>();
		for (int i = 0; i < v.getChildrenNumber(res); i++) {
			IValue child = v.getChildAt(res, i);
			v.mark(child);
			newchildren.add(function.call(new Type[]{child.getType()}, new IValue[]{child}, null).getValue());
		}
		res = v.setChildren(res, newchildren);
		
		if(entered){
			getEvaluatorContext().popStrategyContext(); // Exit the context.
		}
		
		return makeResult(res, ctx);
	}
	
}
