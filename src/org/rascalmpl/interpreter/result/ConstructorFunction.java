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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.util.ImmutableMap;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.TypeDeclarationEvaluator;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.ModuleEnvironment.GenericKeywordParameters;
import org.rascalmpl.interpreter.staticErrors.UnexpectedKeywordArgumentType;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.uptr.Factory;

public class ConstructorFunction extends NamedFunction {
	private final Type constructorType;
	private final List<KeywordFormal> initializers;

	public ConstructorFunction(AbstractAST ast, IEvaluator<Result<IValue>> eval, Environment env, Type constructorType, List<KeywordFormal> initializers) {
		super(ast, eval, (FunctionType) RascalTypeFactory.getInstance().functionType(constructorType.getAbstractDataType(), constructorType.getFieldTypes(), TypeDeclarationEvaluator.computeKeywordParametersType(initializers, eval)), initializers, constructorType.getName(), false, true, false, env);
		this.constructorType = constructorType;
		this.initializers = initializers;
	}
	
	public Type getConstructorType() {
		return constructorType;
	}

	@Override
	public ConstructorFunction cloneInto(Environment env) {
		ConstructorFunction c = new ConstructorFunction(getAst(), getEval(), env, constructorType, initializers);
		c.setPublic(isPublic());
		return c;
	}
	
	@Override
	public Type getKeywordArgumentTypes() {
		Type kwTypes = functionType.getKeywordParameterTypes();
		ArrayList<Type> types = new ArrayList<>();
		ArrayList<String> labels = new ArrayList<>();
		
		for (String label : kwTypes.getFieldNames()) {
			types.add(kwTypes.getFieldType(label));
			labels.add(label);
		}
		
		Set<GenericKeywordParameters> kws = getEvaluatorContext().getCurrentEnvt().lookupGenericKeywordParameters(constructorType.getAbstractDataType());
		for (GenericKeywordParameters p : kws) {
			Map<String, Type> m = p.getTypes();
			for (String name : m.keySet()) {
				labels.add(name);
				types.add(m.get(name));
			}
		}
		
		Type[] typeArray = types.toArray(new Type[0]);
		String[] stringArray = labels.toArray(new String[0]);
		return TypeFactory.getInstance().tupleType(typeArray, stringArray);
	}
	
	@Override
	protected void bindKeywordArgs(Map<String, IValue> keyArgValues, Environment callerEnvironment) {
		Set<GenericKeywordParameters> kws = callerEnvironment.lookupGenericKeywordParameters(constructorType.getAbstractDataType());
		Environment resultScope = ctx.getCurrentEnvt();
		
		for (GenericKeywordParameters gkw : kws) {
			// for hygiene's sake, each list of generic params needs to be evaluated in its declaring environment
			Environment env = new Environment(gkw.getEnv(), vf.sourceLocation(URIUtil.rootScheme("initializer")), "keyword parameter initializer");
			ctx.setCurrentEnvt(env);
			
			try {
				for (KeywordFormal kwparam : gkw.getFormals()) {
					String name = Names.name(kwparam.getName());
					Type kwType = gkw.getTypes().get(name);
					
					if (kwType == null) {
						continue;
					}
					Result<IValue> kwResult;
					
					if (keyArgValues.containsKey(name)){
						IValue r = keyArgValues.get(name);
						
						if(!r.getType().isSubtypeOf(kwType)) {
							throw new UnexpectedKeywordArgumentType(name, kwType, r.getType(), ctx.getCurrentAST());
						}

						kwResult = ResultFactory.makeResult(kwType, r, ctx);
					} 
					else {
						Expression def = kwparam.getExpression();
						kwResult = def.interpret(eval);
					}
					
					// we store it in the local scope so the next initializer may use it
					env.declareVariable(kwType, name);
					env.storeVariable(name, kwResult);
					
					// and we clone it in the outer scope so the constructor specific initializers may use them
					resultScope.declareVariable(kwType, name);
					resultScope.storeVariable(name, kwResult);
				}
			}
			finally {
				ctx.setCurrentEnvt(resultScope);
			}
		}
			
		// and here go the constructor-specific initializers
		super.bindKeywordArgs(keyArgValues, callerEnvironment);
	}
	
	@Override
	protected ImmutableMap<String, IValue> transferKeywordArgs(Environment env, Environment callerScope, 
			ImmutableMap<String, IValue> result) {
		Set<GenericKeywordParameters> kws = callerScope.lookupGenericKeywordParameters(constructorType.getAbstractDataType());
		
		for (GenericKeywordParameters gkw : kws) {
			Map<String, Type> types = gkw.getTypes();
			
			for (String name : types.keySet()) {
				result = result.__put(name, env.getVariable(name).getValue());
			}
		}
		
		result = super.transferKeywordArgs(env, callerScope, result);
		return result;
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
		
		Map<Type,Type> bindings = new HashMap<Type,Type>();
		if (!constructorType.getFieldTypes().match(TF.tupleType(actualTypes), bindings)) {
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

		return makeResult(instantiated, ctx.getValueFactory().constructor(constructorType, actuals, computeKeywordArgs(actuals, keyArgValues)), ctx);
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
