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

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.utils.Names;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;


public class TypedVariablePattern extends AbstractMatchingResult implements IVarPattern {
	private String name;
	protected final io.usethesource.vallang.type.Type declaredType;
	protected final io.usethesource.vallang.type.Type instantiatedDeclaredType;
	private boolean anonymous = false;
	private boolean debug = false;
	protected boolean alreadyStored = false;
    private final boolean bindTypeParameters;

	public TypedVariablePattern(IEvaluatorContext ctx, Expression x, io.usethesource.vallang.type.Type type, org.rascalmpl.ast.Name name, boolean bindTypeParameters) {
		super(ctx, x);
		assert type != TypeFactory.getInstance().voidType();
		this.name = Names.name(name);
		this.bindTypeParameters = bindTypeParameters;
		this.declaredType = type;
		this.instantiatedDeclaredType = type.instantiate(ctx.getCurrentEnvt().getStaticTypeBindings());
		this.anonymous = Names.name(name).equals("_");
		if(debug) System.err.println("AbstractPatternTypedVariabe: " + name);
	}
	
	public TypedVariablePattern(IEvaluatorContext ctx, Expression x, io.usethesource.vallang.type.Type type, String name, boolean bindTypeParameters) {
		super(ctx, x);
		this.name = name;
		this.declaredType = type;
		this.instantiatedDeclaredType = type.instantiate(ctx.getCurrentEnvt().getStaticTypeBindings());
		this.anonymous = name.equals("_");
		this.bindTypeParameters = bindTypeParameters;
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
			System.err.println("AbstractTypedVariable.next: " + subject + "(type=" + subject.getStaticType() + ") with " + declaredType + " " + name);
		}

		// first test the static type (should match at the very least)
		if (subject.getValue().getType().isSubtypeOf(declaredType)) {
			if (debug) { System.err.println("matches"); }
			
			if (bindTypeParameters) {
			    try {
			        Map<Type, Type> staticBindings = new HashMap<>(ctx.getCurrentEnvt().getStaticTypeBindings());

			        // collect the type bindings for later usage
			        declaredType.match(subject.getStaticType(), staticBindings);
			        ctx.getCurrentEnvt().storeStaticTypeBindings(staticBindings);
			        
			        // also check the dynamic type:
	                Map<Type, Type> dynBindings = new HashMap<>(ctx.getCurrentEnvt().getDynamicTypeBindings());
					// collect the type bindings for later usage
					Type dynMatchType = subject.getValue().getType();
					
					if (dynMatchType.isOpen()) {
						// type parameter hygiene required (consider self-application of a function like `&T id(&T v) = v`)
						dynMatchType = AbstractFunction.renameType(dynMatchType, new HashMap<>());
					}

					if (!declaredType.match(dynMatchType, dynBindings)) {
						return false;
					}

					ctx.getCurrentEnvt().storeDynamicTypeBindings(dynBindings);
					
					ctx.getCurrentEnvt().declareAndStoreInferredInnerScopeVariable(name, ResultFactory.makeResult(declaredType, subject.getValue(), ctx));
					this.alreadyStored = true;
					return true;
			    }
			    catch (FactTypeUseException e) {
			        // however, in normal matching (not formal parameters) we allow the static type to be a strict super-type, as long as the dynamic type is a sub-type of the pattern we succeed!
			    }
			}
			  
			Type dynType = subject.getValue().getType();
			if (dynType.isOpen()) {
				// type parameter hygiene required (consider self-application of a function like `&T id(&T v) = v`)
				dynType = AbstractFunction.renameType(dynType, new HashMap<>());
			}

			if (!dynType.isSubtypeOf(declaredType.instantiate(ctx.getCurrentEnvt().getDynamicTypeBindings()))) {
				return false;
			}
			
			if (anonymous) {
				return true;
			}
		
			ctx.getCurrentEnvt().declareAndStoreInferredInnerScopeVariable(name, ResultFactory.makeResult(declaredType, subject.getValue(), ctx));
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
