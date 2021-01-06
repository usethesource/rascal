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
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariable;
import org.rascalmpl.interpreter.utils.Names;
import io.usethesource.vallang.IValue;

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

		if (debug) System.err.println("AbstractPatternMultiVariablePattern.match: " + name);
		
		// Anonymous variables matches always
		if (anonymous) {
			return true;
		}
	
		if (iWroteItMySelf) {
			// overwrite a previous binding
			ctx.getCurrentEnvt().storeVariable(((org.rascalmpl.semantics.dynamic.QualifiedName.Default) name).lastName(), subject);
			return true;
		}
		// either bind the variable or check for equality
		
		Result<IValue> varRes = ctx.getCurrentEnvt().getSimpleVariable(name);
		if (varRes == null) {
			// inferred declaration
			declaredType = subject.getStaticType();
			if (!ctx.getCurrentEnvt().declareVariable(declaredType, getName())) {
				throw new RedeclaredVariable(getName(), ctx.getCurrentAST());
			}
			ctx.getCurrentEnvt().storeVariable(((org.rascalmpl.semantics.dynamic.QualifiedName.Default) name).lastName(), subject);
			iWroteItMySelf = true;
			return true;
		}
		else if (varRes.getValue() == null) {
			declaredType = varRes.getStaticType();
			if (!ctx.getCurrentEnvt().declareVariable(declaredType, getName())) {
				throw new RedeclaredVariable(getName(), ctx.getCurrentAST());
			}
			ctx.getCurrentEnvt().storeVariable(((org.rascalmpl.semantics.dynamic.QualifiedName.Default) name).lastName(), subject);
			iWroteItMySelf = true;
			return true;
		}
		else {
			// equality check
			if(debug)System.err.printf("subject.getTYpe() = %s, varRes.getType() = %s\n", subject.getValue().getType(), varRes.getStaticType());
			if (subject.getValue().getType().isSubtypeOf(varRes.getStaticType())) {
				if(debug) {
					System.err.println("returns " + subject.equals(varRes));
				}
//					iWroteItMySelf = false;
				return subject.getValue().match(varRes.getValue());
			}
			return false;
		}
		
		
		
		// If not anonymous, store the value.
		// TODO: This is problematic, we probably need to move some of the logic
		// in QualifiedNamePattern here to check on whether this is "bindable".
//		if(!anonymous) {
//			ctx.getCurrentEnvt().storeVariable(((org.rascalmpl.semantics.dynamic.QualifiedName.Default) name).lastName(), subject);
//		}
//		return true;
	}
	
	@Override
	public String toString(){
		return Names.fullName(name);
	}
	
}
