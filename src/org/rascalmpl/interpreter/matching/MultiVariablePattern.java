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
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class MultiVariablePattern extends QualifiedNamePattern {

	public MultiVariablePattern(IEvaluatorContext ctx, Expression.MultiVariable x, org.rascalmpl.ast.QualifiedName name) {
		super(ctx, x, name);
	}
	
	public MultiVariablePattern(IEvaluatorContext ctx, Expression.Splice x, org.rascalmpl.ast.QualifiedName name) {
		super(ctx, x, name);
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		if(!hasNext)
			return false;
		
		// If not anonymous, store the value.
		if(!anonymous) {
			ctx.getCurrentEnvt().storeVariable(name.toString(), subject);
		}
		return true;
	}
	
	@Override
	public String toString(){
		return name.toString() /*+ " => " + ctx.getCurrentEnvt().getVariable(name.toString()) */;
	}
	
}
