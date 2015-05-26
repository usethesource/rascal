/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Emilie Balland - (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Mark Hills - Mark.Hills@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;


public class TypedVariablePattern extends AbstractMatchingResult implements IVarPattern {
	private String name;
	protected org.eclipse.imp.pdb.facts.type.Type declaredType;
	private boolean anonymous = false;
	private boolean debug = false;
	protected boolean alreadyStored = false;

	public TypedVariablePattern(IEvaluatorContext ctx, Expression x, org.eclipse.imp.pdb.facts.type.Type type, org.rascalmpl.ast.Name name) {
		super(ctx, x);
		this.name = Names.name(name);
		this.declaredType = type;
		this.anonymous = Names.name(name).equals("_");
		if(debug) System.err.println("AbstractPatternTypedVariabe: " + name);
		
	}
	
	public TypedVariablePattern(IEvaluatorContext ctx, Expression x, org.eclipse.imp.pdb.facts.type.Type type, String name) {
		super(ctx, x);
		this.name = name;
		this.declaredType = type;
		this.anonymous = name.equals("_");
		if(debug) System.err.println("AbstractPatternTypedVariabe: " + name);
		
	}
	
	@Override
	public Type getType(Environment env, HashMap<String,IVarPattern> patternVars) {
		return declaredType;
	}
	
	@Override
	public List<IVarPattern> getVariables(){
		java.util.LinkedList<IVarPattern> res = new java.util.LinkedList<IVarPattern>();
		res.addFirst(this);
		return res;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isAnonymous(){
		return anonymous;
	}

	@Override
	public boolean next() {
		if(debug)System.err.println("AbstractTypedVariable.next");
		checkInitialized();
		if(!hasNext)
			return false;
		hasNext = false;
		if(debug) {
			System.err.println("Subject: " + subject + " name: " + name + " getType: ");
			System.err.println("AbstractTypedVariable.next: " + subject + "(type=" + subject.getType() + ") with " + declaredType + " " + name);
		}

		Type tmp;
		if (subject.getValue().getType().isSubtypeOf(declaredType)) {
			if(debug)System.err.println("matches");
			
			try {
				// type checking code for formal parameters; the static type of the actual should be a sub-type of the type of the formal
				Map<Type, Type> bindings = new HashMap<Type,Type>();
				bindings.putAll(ctx.getCurrentEnvt().getTypeBindings());
				declaredType.match(subject.getType(), bindings);

			   tmp = declaredType.instantiate(bindings);

				if (tmp != declaredType) {
					ctx.getCurrentEnvt().storeTypeBindings(bindings);
				}
			}
			catch (FactTypeUseException e) {
				// however, in normal matching (not formal parameters) we allow the static type to be a strict super-type, as long as the dynamic type is a sub-type of the pattern we succeed!
				tmp = declaredType;
			}
			
			if (anonymous) {
				return true;
			}
			
		
			ctx.getCurrentEnvt().declareAndStoreInferredInnerScopeVariable(name, ResultFactory.makeResult(tmp, subject.getValue(), ctx));
			this.alreadyStored = true;
			return true;
		}
		
		if(debug)System.err.println("no match");
		return false;
	}
	
	@Override
	public String toString(){
		return declaredType + " " + name /* + " => " + subject */;
	}

	@Override
	public boolean isVarIntroducing() {
		return true;
	}

	@Override
	public String name() {
		return getName();
	}

	@Override
	public Type getType() {
		return declaredType;
	}
	
	public boolean bindingInstance() {
		return this.alreadyStored;
	}
	 
}
