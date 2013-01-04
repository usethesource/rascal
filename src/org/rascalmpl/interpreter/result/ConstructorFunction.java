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
 *   * Paul Klint - Paul.Klint@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.Pair;
import org.rascalmpl.interpreter.staticErrors.NoKeywordParametersError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredKeywordParameterError;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.values.uptr.Factory;

public class ConstructorFunction extends NamedFunction {
	private Type constructorType;

	public ConstructorFunction(AbstractAST ast, IEvaluator<Result<IValue>> eval, Environment env, Type constructorType) {
		super(ast, eval, (FunctionType) RascalTypeFactory.getInstance().functionType(constructorType.getAbstractDataType(), constructorType.getFieldTypes()), constructorType.getName(), false, env);
		this.constructorType = constructorType;
	}

	@Override
	public boolean isDefault() {
		return true;
	}
	
	@Override
	public boolean isStatic() {
		return true;
	}
	
	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals, Map<String, Result<IValue>> keyArgValues) {
		if (constructorType == Factory.Tree_Appl) {
			return new ConcreteConstructorFunction(ast, eval, declarationEnvironment).call(actualTypes, actuals, null);
		}
		Type[] allArgumentTypes = addKeywordTypes(actualTypes, keyArgValues);
		
		Map<Type,Type> bindings = new HashMap<Type,Type>();
		if (!constructorType.getFieldTypes().match(TF.tupleType(allArgumentTypes), bindings)) {
			throw new MatchFailed();
		}
		Type formalTypeParameters = constructorType.getAbstractDataType().getTypeParameters();
		Type instantiated = constructorType;

		if (!formalTypeParameters.isVoidType()) {
			for (Type field : formalTypeParameters) {
				if (!bindings.containsKey(field)) {
					bindings.put(field, TF.voidType());
				}
			}
			instantiated = constructorType.instantiate(bindings);
		}

		return makeResult(instantiated, instantiated.make(getValueFactory(), ctx.getCurrentEnvt().getStore(), addKeywordArgs(actuals, keyArgValues)), ctx);
	}
	
	protected Type[] addKeywordTypes(Type[] actualTypes, Map<String, Result<IValue>> keyArgValues){
		int missing = constructorType.getArity() - actualTypes.length;
		if(missing == 0 || keywordParameterDefaults == null)
			return actualTypes;
		Type[] extendedActualTypes = new Type[constructorType.getArity()];
	
		
		for(int i = 0; i < actualTypes.length; i++){
			extendedActualTypes[i] = actualTypes[i];
		}
		
		int k = actualTypes.length;
		
		if(keyArgValues == null){
			for(Pair<String, Result<IValue>> pair : keywordParameterDefaults){
					extendedActualTypes[k++] = pair.getSecond().getType();
			}
			return extendedActualTypes;
		}
		
		if(keywordParameterDefaults == null)
			throw new NoKeywordParametersError(getName(), ctx.getCurrentAST());
		
		int nBoundKeywordArgs = 0;
		for(Pair<String, Result<IValue>> pair : keywordParameterDefaults){
			String kwparam = pair.getFirst();
			if(keyArgValues.containsKey(kwparam)){
				nBoundKeywordArgs++;
				Result<IValue> r = keyArgValues.get(kwparam);
				extendedActualTypes[k++] = r.getType();
			} else {
				extendedActualTypes[k++] = pair.getSecond().getType();
			}
		}
		if(nBoundKeywordArgs != keyArgValues.size()){
			main:
			for(String kwparam : keyArgValues.keySet())
				for(Pair<String, Result<IValue>> pair : keywordParameterDefaults){
					if(kwparam.equals(pair.getFirst()))
							continue main;
					throw new UndeclaredKeywordParameterError(getName(), kwparam, ctx.getCurrentAST());
				}
		}
		return extendedActualTypes;
	}
	
	protected IValue[] addKeywordArgs(IValue[] actuals, Map<String, Result<IValue>> keyArgValues){
		if(constructorType.getArity() == actuals.length)
			return actuals;
		IValue[] extendedActuals = new IValue[actuals.length + keywordParameterDefaults.size()];
		
		for(int i = 0; i < actuals.length; i++){
			extendedActuals[i] = actuals[i];
		}
		int k = actuals.length;
		
		if(keyArgValues == null){
			if(keywordParameterDefaults != null){
				for(Pair<String, Result<IValue>> pair : keywordParameterDefaults){
					extendedActuals[k++] = pair.getSecond().getValue();
				}
			}
			return extendedActuals;
		}
		
//		if(keywordParameterDefaults == null)
//			throw new NoKeywordParametersError(getName(), ctx.getCurrentAST());
		
		int nBoundKeywordArgs = 0;
		for(Pair<String, Result<IValue>> pair : keywordParameterDefaults){
			String kwparam = pair.getFirst();
			if(keyArgValues.containsKey(kwparam)){
				nBoundKeywordArgs++;
				Result<IValue> r = keyArgValues.get(kwparam);
				extendedActuals[k++] = r.getValue();
			} else {
				extendedActuals[k++] = pair.getSecond().getValue();
			}
		}
//		if(nBoundKeywordArgs != keyArgValues.size()){
//			main:
//			for(String kwparam : keyArgValues.keySet())
//				for(Pair<String, Result<IValue>> pair : keywordParameterDefaults){
//					if(kwparam.equals(pair.getFirst()))
//							continue main;
//					throw new UndeclaredKeywordParameterError(getName(), kwparam, ctx.getCurrentAST());
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
