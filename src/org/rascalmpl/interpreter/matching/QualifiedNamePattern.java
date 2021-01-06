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
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.HashMap;
import java.util.List;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariable;
import org.rascalmpl.interpreter.utils.Names;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class QualifiedNamePattern extends AbstractMatchingResult implements IVarPattern {
	protected org.rascalmpl.ast.QualifiedName name;
	protected Type declaredType;
	protected boolean anonymous = false;
	protected boolean debug = false;
	protected boolean iWroteItMySelf;
	
	public QualifiedNamePattern(IEvaluatorContext ctx, Expression x, org.rascalmpl.ast.QualifiedName name){
		
		super(ctx, x);
		
		this.name = name;
		if(debug)System.err.println("QualifiedNamePattern: " + name);
		this.anonymous = getName().equals("_");
		Environment env = ctx.getCurrentEnvt();
		
		// Look for this variable while we are constructing this pattern
		if(anonymous) {
			declaredType = TypeFactory.getInstance().valueType();
		} else {
			Result<IValue> varRes = env.getSimpleVariable(name);
			if (varRes == null || varRes.getStaticType() == null) {
				declaredType = TypeFactory.getInstance().valueType();
			} else {
				declaredType = varRes.getStaticType();
			}
		}
		iWroteItMySelf = false;
	}
	
	public QualifiedNamePattern(IEvaluatorContext ctx){
		super(ctx, null);
		this.anonymous = true;
		declaredType = TypeFactory.getInstance().valueType();
		iWroteItMySelf = false;
	}

	
	@Override
	public void initMatch(Result<IValue> subject) {
		super.initMatch(subject);
		// do not reinit iWroteItMyself!
	}
	
	@Override
	public Type getType(Environment env, HashMap<String,IVarPattern> patternVars) {
		if (anonymous) {
			return declaredType;
		}
		
		if(patternVars != null && patternVars.containsKey(getName())){
			Type ot = patternVars.get(getName()).getType();
			if(ot.compareTo(declaredType) < 0)
					declaredType = ot;
		}
		return declaredType;
	}
	
	@Override
	public List<IVarPattern> getVariables(){
		java.util.LinkedList<IVarPattern> res = new java.util.LinkedList<IVarPattern>();
		res.addFirst(this);
		return res;
	}
	
	public String getName(){
		return ((org.rascalmpl.semantics.dynamic.QualifiedName.Default) name).lastName();
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
		
		Result<IValue> varRes = ctx.getCurrentEnvt().getSimpleVariable(name);
		if (varRes == null) {
			// inferred declaration
			declaredType = subject.getStaticType();
			if (!ctx.getCurrentEnvt().declareVariable(declaredType, getName())) {
				throw new RedeclaredVariable(getName(), ctx.getCurrentAST());
			}
			ctx.getCurrentEnvt().storeVariable(name, subject);
			iWroteItMySelf = true;
			return true;
		}
		else if (varRes.getValue() == null) {
			declaredType = varRes.getStaticType();
			if (!ctx.getCurrentEnvt().declareVariable(declaredType, getName())) {
				throw new RedeclaredVariable(getName(), ctx.getCurrentAST());
			}
			ctx.getCurrentEnvt().storeVariable(name, subject);
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
	}
	
	@Override
	public String toString(){
		return Names.fullName(name);
	}
	
	public boolean bindingInstance() {
		return iWroteItMySelf;
	}

	@Override
	public boolean isVarIntroducing() {
		return bindingInstance();
	}

	@Override
	public String name() {
		return getName();
	}

	@Override
	public Type getType() {
		return declaredType;
	}

	@Override
	public void updateType(Type type) {
	  declaredType = type;
	}
}
