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
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UninitializedPatternMatchError;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.values.uptr.Factory;

public abstract class AbstractMatchingResult extends AbstractBooleanResult implements IMatchingResult {
	protected Result<IValue> subject = null;
	private final AbstractAST ast;
	
	public AbstractMatchingResult(IEvaluatorContext ctx, AbstractAST ast) {
		super(ctx);
		this.ast = ast;
	}
	
	public AbstractAST getAST(){
		return ast;
	}
	
	public void initMatch(Result<IValue> subject) {
		if(subject.isVoid()) 
			throw new UninitializedPatternMatchError("Uninitialized pattern match: trying to match a value of the type 'void'", ctx.getCurrentAST());
		init();
		this.subject = subject;
	}
	
	public boolean mayMatch(Type subjectType, Environment env){
		return mayMatch(getType(env, null), subjectType);
	}
	
	protected void checkInitialized(){
		if(!initialized){
			throw new ImplementationError("hasNext or match called before initMatch", ast.getLocation());
		}
	}
	
	public boolean hasNext()
	{
		return initialized && hasNext;
	}
	
	public List<IVarPattern> getVariables(){
		return new java.util.LinkedList<IVarPattern>();
	}
	
	boolean matchChildren(Iterator<IValue> subjChildren, Iterator<IMatchingResult> iterator){
		while (iterator.hasNext()) {
			if (!iterator.next().next()){
				return false;
			}
		}
		return true;
	}

	public IValue toIValue() {
		// if a pattern does not contain variables, simply evaluating it as an expression should
		// yield a proper value
		return getAST().interpret(ctx.getEvaluator()).getValue();
//		return ctx.getEvaluator().eval((Expression) getAST()).getValue();
	}
	
	abstract public Type getType(Environment env, HashMap<String,IVarPattern> patternVars);
	
	public HashMap<String,IVarPattern> merge(HashMap<String,IVarPattern> left, List<IVarPattern> right){
		if(left == null){
			HashMap<String,IVarPattern> res = new  HashMap<String,IVarPattern>();
			for(IVarPattern vpr: right){
				res.put(vpr.name(), vpr);
			}
			return res;
		}
		for(IVarPattern vpr: right){
			String name = vpr.name();
			if(left.containsKey(name)){
				IVarPattern vpl = left.get(name);
				if(!vpl.isVarIntroducing() && vpr.isVarIntroducing())
					left.put(name, vpr);
			} else
				left.put(name, vpr);	
		}
		return left;
	}

	abstract public boolean next();
	
	protected boolean mayMatch(Type small, Type large){
		if(small.equivalent(large))
			return true;

		if(small.isVoidType() || large.isVoidType())
			return false;

		if(small.isSubtypeOf(large) || large.isSubtypeOf(small))
			return true;

		if (small instanceof NonTerminalType && large instanceof NonTerminalType) {
			return small.equals(large);
		}
		
		if (small instanceof NonTerminalType) {
			return large.isSubtypeOf(Factory.Tree);
		}
		
		if (large instanceof NonTerminalType) {
			return small.isSubtypeOf(Factory.Tree);
		}
		
		if(small.isListType() && large.isListType() || 
				small.isSetType() && large.isSetType())
			return mayMatch(small.getElementType(),large.getElementType());
		if(small.isMapType() && large.isMapType())
			return mayMatch(small.getKeyType(), large.getKeyType()) &&
			mayMatch(small.getValueType(), large.getValueType());
		if(small.isTupleType() && large.isTupleType()){
			if(small.getArity() != large.getArity())
				return false;
			for(int i = 0; i < large.getArity(); i++){
				if(mayMatch(small.getFieldType(i), large.getFieldType(i)))
					return true;
			}
			return false;
		}
		if(small.isConstructorType() && large.isConstructorType()){
			if(small.getName().equals(large.getName()))
				return false;
			for(int i = 0; i < large.getArity(); i++){
				if(mayMatch(small.getFieldType(i), large.getFieldType(i)))
					return true;
			}
			return false;
		}
		if(small.isConstructorType() && large.isAbstractDataType())
			return small.getAbstractDataType().equivalent(large);
		
		if(small.isAbstractDataType() && large.isConstructorType())
			return small.equivalent(large.getAbstractDataType());
		
		
		return false;
	}

}
