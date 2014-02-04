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
 *   * Paul Klint - Paul.Klint@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.KeywordParameter;
import org.rascalmpl.interpreter.staticErrors.ArgumentsMismatch;
import org.rascalmpl.interpreter.staticErrors.NoKeywordParameters;
import org.rascalmpl.interpreter.staticErrors.UndeclaredKeywordParameter;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.values.uptr.Factory;

public class ConstructorFunction extends NamedFunction {
	private Type constructorType;
	private final List<KeywordParameter> keyArgs;

	public ConstructorFunction(AbstractAST ast, IEvaluator<Result<IValue>> eval, Environment env, Type constructorType, List<KeywordParameter> keyargs) {
		super(ast, eval, (FunctionType) RascalTypeFactory.getInstance().functionType(constructorType.getAbstractDataType(), constructorType.getFieldTypes()), constructorType.getName(), false, true, false, keyargs, env);
		this.keyArgs = keyargs;
		this.constructorType = constructorType;
	}

	@Override
	public ConstructorFunction cloneInto(Environment env) {
		ConstructorFunction c = new ConstructorFunction(getAst(), getEval(), env, constructorType, keyArgs);
		c.setPublic(isPublic());
		return c;
	}
	
	@Override
	public boolean isStatic() {
		return true;
	}
	
	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals, Map<String, IValue> keyArgValues) {
		if (constructorType == Factory.Tree_Appl) {
			return new ConcreteConstructorFunction(ast, eval, declarationEnvironment).call(actualTypes, actuals, keyArgValues);
		}
		Type[] allArgumentTypes = addKeywordTypes(actualTypes, keyArgValues);
		
		Map<Type,Type> bindings = new HashMap<Type,Type>();
		if (!constructorType.getFieldTypes().match(TF.tupleType(allArgumentTypes), bindings)) {
			throw new MatchFailed();
		}
		Type formalTypeParameters = constructorType.getAbstractDataType().getTypeParameters();
		Type instantiated = constructorType;

		if (!formalTypeParameters.isBottom()) {
			for (Type field : formalTypeParameters) {
				if (!bindings.containsKey(field)) {
					bindings.put(field, TF.voidType());
				}
			}
			instantiated = constructorType.instantiate(bindings);
		}

		return makeResult(instantiated, ctx.getValueFactory().constructor(constructorType, addKeywordArgs(actuals, keyArgValues)), ctx);
	}
	
	protected Type[] addKeywordTypes(Type[] actualTypes, Map<String, IValue> keyArgValues){
		if(constructorType.getArity() == actualTypes.length && keywordParameterDefaults == null)
			return actualTypes;
		
		if(constructorType.getPositionalArity() < actualTypes.length){
			throw new ArgumentsMismatch("Too many arguments for constructor " + getName(), ctx.getCurrentAST());
		}
		if(constructorType.getPositionalArity() > actualTypes.length){
			throw new ArgumentsMismatch("Too few arguments for constructor " + getName(), ctx.getCurrentAST());
		}
		
		if(!constructorType.hasKeywordArguments() && keyArgValues != null)
			throw new NoKeywordParameters(getName(), ctx.getCurrentAST());

		Type[] extendedActualTypes = new Type[constructorType.getArity()];
	
		for(int i = 0; i < actualTypes.length; i++){
			extendedActualTypes[i] = actualTypes[i];
		}
		
		int k = actualTypes.length;
		
		if(keyArgValues == null){
			
			for(KeywordParameter kw : keywordParameterDefaults){
					extendedActualTypes[k++] = kw.getType();
			}
			return extendedActualTypes;
		}
	
		int nBoundKeywordArgs = 0;
		for(KeywordParameter kw : keywordParameterDefaults){
			String kwparam = kw.getName();
			if(keyArgValues.containsKey(kwparam)){
				nBoundKeywordArgs++;
				IValue r = keyArgValues.get(kwparam);
				extendedActualTypes[k++] = r.getType();
			} else {
				extendedActualTypes[k++] = kw.getType();
			}
		}
		if(nBoundKeywordArgs != keyArgValues.size()){
			main:
			for(String kwparam : keyArgValues.keySet())
				for(KeywordParameter kw : keywordParameterDefaults){
					if(kwparam.equals(kw.getName()))
							continue main;
					throw new UndeclaredKeywordParameter(getName(), kwparam, ctx.getCurrentAST());
				}
		}
		return extendedActualTypes;
	}
	
	protected IValue[] addKeywordArgs(IValue[] actuals, Map<String, IValue> keyArgValues){
		if(constructorType.getArity() == actuals.length)
			return actuals;
		IValue[] extendedActuals = new IValue[actuals.length + keywordParameterDefaults.size()];
		
		for(int i = 0; i < actuals.length; i++){
			extendedActuals[i] = actuals[i];
		}
		int k = actuals.length;
		
		if(keyArgValues == null){
			if(keywordParameterDefaults != null){
				for(KeywordParameter kw : keywordParameterDefaults){
					extendedActuals[k++] = kw.getValue();
				}
			}
			return extendedActuals;
		}
		
//		if(keywordParameterDefaults == null)
//			throw new NoKeywordParameters(getName(), ctx.getCurrentAST());
		
//		int nBoundKeywordArgs = 0;
		for(KeywordParameter kw : keywordParameterDefaults){
			String kwparam = kw.getName();
			if(keyArgValues.containsKey(kwparam)){
//				nBoundKeywordArgs++;
				IValue r = keyArgValues.get(kwparam);
				extendedActuals[k++] = r;
			} else {
				extendedActuals[k++] = kw.getValue();
			}
		}
//		if(nBoundKeywordArgs != keyArgValues.size()){
//			main:
//			for(String kwparam : keyArgValues.keySet())
//				for(Pair<String, Result<IValue>> pair : keywordParameterDefaults){
//					if(kwparam.equals(pair.getFirst()))
//							continue main;
//					throw new UndeclaredKeywordParameter(getName(), kwparam, ctx.getCurrentAST());
//				}
//		}
		return extendedActuals;
	}
	
	@Override
	public int hashCode() {
		return constructorType.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConstructorFunction) {
			return constructorType == ((ConstructorFunction) obj).constructorType;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return constructorType.toString();
	}
	
	@Override
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return that.equalToConstructorFunction(this);
	}
	
	@Override
	public Result<IBool> equalToConstructorFunction(ConstructorFunction that) {
		return ResultFactory.bool((constructorType == that.constructorType), ctx);
	}
}
