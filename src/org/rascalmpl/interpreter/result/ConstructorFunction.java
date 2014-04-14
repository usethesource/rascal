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
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.values.uptr.Factory;

public class ConstructorFunction extends NamedFunction {
	private Type constructorType;

	public ConstructorFunction(AbstractAST ast, IEvaluator<Result<IValue>> eval, Environment env, Type constructorType) {
		super(ast, eval, (FunctionType) RascalTypeFactory.getInstance().functionType(constructorType.getAbstractDataType(), constructorType.getFieldTypes(), constructorType.getKeywordParameterTypes(), constructorType.getKeywordParameterDefaults()), constructorType.getName(), false, true, false, env);
		this.constructorType = constructorType;
	}

	@Override
	public ConstructorFunction cloneInto(Environment env) {
		ConstructorFunction c = new ConstructorFunction(getAst(), getEval(), env, constructorType);
		c.setPublic(isPublic());
		return c;
	}
	
	@Override
	public Map<String, Type> getKeywordArgumentTypes() {
	  return constructorType.getKeywordParameterTypes();
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

		Environment old = ctx.getCurrentEnvt();
		Environment init = new Environment(old.getLocation(), "initializer");
		
		try {
			ctx.setCurrentEnvt(init);
			
			// make sure the children and the given kw params are set in the scope, such that
			// the other kwParameters can be initialized accordingly.
			
			for (String key : keyArgValues.keySet()) {
				init.storeVariable(key, ResultFactory.makeResult(constructorType.getKeywordParameterType(key), keyArgValues.get(key), ctx));
			}
			
			for (int i = 0; i < constructorType.getArity(); i++) {
				init.storeVariable(constructorType.getFieldName(i), ResultFactory.makeResult(constructorType.getFieldType(i), actuals[i], ctx));
			}
			
			return makeResult(instantiated, ctx.getValueFactory().constructor(constructorType, actuals), ctx);
		}
		finally {
			ctx.unwind(old);
		}
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
