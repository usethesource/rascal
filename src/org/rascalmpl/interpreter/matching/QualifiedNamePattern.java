/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - emilie.balland@inria.fr (INRIA)
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariableError;
import org.rascalmpl.interpreter.utils.Names;

public class QualifiedNamePattern extends AbstractMatchingResult {
	protected org.rascalmpl.ast.QualifiedName name;
	private Type declaredType;
	protected boolean anonymous = false;
	private boolean debug = false;
	private boolean iWroteItMySelf;
	
	public QualifiedNamePattern(Expression x, org.rascalmpl.ast.QualifiedName name){
		super(x);
		this.name = name;
		this.anonymous = getName().equals("_");
		Environment env = ctx.getCurrentEnvt();
		
		// Look for this variable while we are constructing this pattern
		if(anonymous){
			declaredType = TypeFactory.getInstance().valueType();
		} else {
			Result<IValue> varRes = env.getVariable(name);
			if (varRes == null || varRes.getType() == null) {
				declaredType = TypeFactory.getInstance().valueType();
			} else {
				declaredType = varRes.getType();
			}
		}
		
		iWroteItMySelf = false;
	}
	
	@Override
	public void initMatch(IEvaluatorContext ctx, Result<IValue> subject) {
		super.initMatch(ctx, subject);
		// do not reinit iWroteItMyself!
	}
	
	@Override
	public Type getType(Environment env) {
		return declaredType;
	}
	
	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String>();
		res.addFirst(getName());
		return res;
	}
	
	public String getName(){
		return Names.name(Names.lastName(name));
	}
	
	public boolean isAnonymous(){
		return anonymous;
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		if(!hasNext) {
			return false;
		}
		hasNext = false;
		
		if (debug) System.err.println("AbstractPatternQualifiedName.match: " + name);
		
		// Anonymous variables matches always
		if (anonymous) {
			return true;
		}
	
		if (iWroteItMySelf) {
			// overwrite a previous binding
			ctx.getCurrentEnvt().storeVariable(name, subject);
			return true;
		}
		// either bind the variable or check for equality
		
		Result<IValue> varRes = ctx.getCurrentEnvt().getVariable(name);
		if (varRes == null) {
			// inferred declaration
			declaredType = subject.getType();
			if (!ctx.getCurrentEnvt().declareVariable(declaredType, getName())) {
				throw new RedeclaredVariableError(getName(), ctx.getCurrentAST());
			}
			ctx.getCurrentEnvt().storeVariable(name, subject);
			iWroteItMySelf = true;
			return true;
		}
		else if (varRes.getValue() == null) {
			declaredType = varRes.getType();
			if (!ctx.getCurrentEnvt().declareVariable(declaredType, getName())) {
				throw new RedeclaredVariableError(getName(), ctx.getCurrentAST());
			}
			ctx.getCurrentEnvt().storeVariable(name, subject);
			iWroteItMySelf = true;
			return true;
		}
		else {
			// equality check
			if (subject.getValue().getType().isSubtypeOf(varRes.getType())) {
				if(debug) {
					System.err.println("returns " + subject.equals(varRes));
				}
//					iWroteItMySelf = false;
				return subject.equals(varRes).isTrue();
			}
			return false;
		}
	}
	
	@Override
	public String toString(){
		return name + "==" + subject;
	}
	
	public boolean bindingInstance() {
		return iWroteItMySelf;
	}
}
