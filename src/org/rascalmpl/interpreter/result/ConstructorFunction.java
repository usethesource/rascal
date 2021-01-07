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

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.TypeDeclarationEvaluator;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.ModuleEnvironment.GenericKeywordParameters;
import org.rascalmpl.interpreter.staticErrors.UndeclaredField;
import org.rascalmpl.interpreter.staticErrors.UnexpectedKeywordArgumentType;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.RascalValueFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class ConstructorFunction extends NamedFunction {
	protected final Type constructorType;
	private Type kwTypes = null; // cache
	private final List<KeywordFormal> initializers;

	public ConstructorFunction(AbstractAST ast, IEvaluator<Result<IValue>> eval, Environment env, Type constructorType, List<KeywordFormal> initializers) {
		super(ast, 
			eval, 
			TF.functionType(constructorType.getAbstractDataType(), constructorType.getFieldTypes(), TypeDeclarationEvaluator.computeKeywordParametersType(initializers, eval)), 
			TF.functionType(constructorType.getAbstractDataType(), constructorType.getFieldTypes(), TypeDeclarationEvaluator.computeKeywordParametersType(initializers, eval)), 
			initializers, 
			constructorType.getName(), 
			false, 
			true, 
			false, 
			env
		);
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
	
	// TODO: refactor and make small. For now this does the job.
	public Result<IValue> computeDefaultKeywordParameter(String label, IConstructor value, Environment callerEnvironment) {
		Set<GenericKeywordParameters> kws = callerEnvironment.lookupGenericKeywordParameters(constructorType.getAbstractDataType());
		IWithKeywordParameters<? extends IConstructor> wkw = value.asWithKeywordParameters();
		Environment old = ctx.getCurrentEnvt();
		Environment resultEnv = new Environment(declarationEnvironment, URIUtil.rootLocation("initializer"), "keyword parameter initializer");
		
		// first we compute the keyword parameters for the abstract data-type:
		for (GenericKeywordParameters gkw : kws) {
			// for hygiene's sake, each list of generic params needs to be evaluated in its declaring environment
			Environment env = new Environment(gkw.getEnv(), URIUtil.rootLocation("initializer"), "kwp initializer");
			
			try {
				ctx.setCurrentEnvt(env);
			
				for (KeywordFormal kwparam : gkw.getFormals()) {
					String name = Names.name(kwparam.getName());
					Type kwType = gkw.getTypes().get(name);
					
					if (kwType == null) {
						continue;
					}
					Result<IValue> kwResult;
					
					if (wkw.hasParameter(name)){
						IValue r = wkw.getParameter(name);
						
						if(!r.getType().isSubtypeOf(kwType)) {
							throw new UnexpectedKeywordArgumentType(name, kwType, r.getType(), ctx.getCurrentAST());
						}

						kwResult = ResultFactory.makeResult(kwType, r, ctx);
					} 
					else {
						Expression def = kwparam.getExpression();
						kwResult = def.interpret(eval);
					}
					
					if (name.equals(label)) {
						// we have the one we need, bail out quickly
						return kwResult;
					}
					else {
						env.declareVariable(kwResult.getStaticType(), name);
						env.storeVariable(name, kwResult);
						resultEnv.declareVariable(kwResult.getStaticType(), name);
						resultEnv.storeVariable(name, kwResult);
					}
				}
			}
			finally {
				ctx.setCurrentEnvt(old);
			}
		}
		
		Type formals = getFunctionType().getFieldTypes();
		
		try {
			// we set up an environment to hold the positional parameter values
			ctx.setCurrentEnvt(resultEnv);
			
			for (int i = 0; i < formals.getArity(); i++) {
				String fieldName = formals.getFieldName(i);
				Type fieldType = formals.getFieldType(i);
				resultEnv.declareVariable(fieldType, fieldName);
				resultEnv.storeLocalVariable(fieldName, ResultFactory.makeResult(fieldType, value.get(fieldName), ctx));
			}
		
			for (String kwparam : staticFunctionType.getKeywordParameterTypes().getFieldNames()) {
	            Type kwType = staticFunctionType.getKeywordParameterType(kwparam);
	            Result<IValue> kwResult;
	            
	            if (wkw.hasParameter(kwparam)){
	                IValue r = wkw.getParameter(kwparam);
	
	                if(!r.getType().isSubtypeOf(kwType)) {
	                    throw new UnexpectedKeywordArgumentType(kwparam, kwType, r.getType(), ctx.getCurrentAST());
	                }
	
	                kwResult = ResultFactory.makeResult(kwType, r, ctx);
	            } 
	            else {
	                Expression def = getKeywordParameterDefaults().get(kwparam);
	                kwResult = def.interpret(eval);
	            }
	            
	            if (kwparam.equals(label)) {
	            	return kwResult;
	            }
	            else {
	            	resultEnv.declareVariable(kwType, kwparam);
	            	resultEnv.storeVariable(kwparam, kwResult);
	            }
	        }
			
			throw new UndeclaredField(label,constructorType, ctx.getCurrentAST());
		}
		finally {
			ctx.setCurrentEnvt(old);
		}
	}
	
	
	@Override
	public Type getKeywordArgumentTypes(Environment env) {
	    if (kwTypes != null) {
	        return kwTypes;
	    }
		
		Type kwTypes = staticFunctionType.getKeywordParameterTypes();
		ArrayList<Type> types = new ArrayList<>();
		ArrayList<String> labels = new ArrayList<>();
		
		for (String label : kwTypes.getFieldNames()) {
			types.add(kwTypes.getFieldType(label));
			labels.add(label);
		}
		
		Set<GenericKeywordParameters> kws = env.lookupGenericKeywordParameters(constructorType.getAbstractDataType());
		for (GenericKeywordParameters p : kws) {
			Map<String, Type> m = p.getTypes();
			for (String name : m.keySet()) {
				labels.add(name);
				types.add(m.get(name));
			}
		}
		
		Type[] typeArray = types.toArray(new Type[0]);
		String[] stringArray = labels.toArray(new String[0]);
		kwTypes = TypeFactory.getInstance().tupleType(typeArray, stringArray);
		return kwTypes;
	}
	
	
	
	@Override
	public boolean isStatic() {
		return true;
	}
	
	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals, Map<String, IValue> keyArgValues) {
	    Type formalTypeParameters = constructorType.getAbstractDataType().getTypeParameters();
	    Type fieldTypes = constructorType.getFieldTypes();

		// first we match to see if the actual parameters can fit the constructor's parameter types:
	    if (fieldTypes.getArity() != actuals.length) {
	        throw new MatchFailed();
	    }
	    
		for (int i = 0; i < actuals.length; i++) {
		    if (!actuals[i].getType().isSubtypeOf(fieldTypes.getFieldType(i))) {
		        throw new MatchFailed();
		    }
		}
		
		// if the match works, then we can go fill the information for the static type system:
		Map<Type,Type> bindings = new HashMap<Type,Type>();
		
        if (!fieldTypes.match(TF.tupleType(actualTypes), bindings)) {
		    // so now the static types don't match, but the dynamic types did match... 
		    // then what should the bindings be for the type parameters?
		    // they should bubble up to their upper bounds because we don't know 
            // anything about them statically.
		    bindings = new HashMap<Type,Type>();
		    
		    if (!formalTypeParameters.isBottom()) {
	            for (Type field : formalTypeParameters) {
	                Type bound = field.getBound();
                    if (bound != null) {
	                    bindings.put(field, bound);
                    }
                    else {
                        bindings.put(field, TF.valueType());
                    }
	            }
		    }
		}
		
		// TODO: when characters get proper types we need to add them here.
		if (constructorType == RascalValueFactory.Tree_Appl || constructorType == RascalValueFactory.Tree_Amb || constructorType == RascalValueFactory.Tree_Cycle || constructorType == RascalValueFactory.Tree_Char) {
			return new ConcreteConstructorFunction(ast, constructorType, eval, declarationEnvironment).call(actualTypes, actuals, keyArgValues);
		}
		
		Type instantiated = constructorType;

		if (!formalTypeParameters.isBottom()) {
			for (Type field : formalTypeParameters) {
				if (!bindings.containsKey(field)) {
					bindings.put(field, TF.voidType());
				}
			}
			instantiated = constructorType.instantiate(bindings);
		}

		return makeResult(instantiated.getAbstractDataType(), ctx.getValueFactory().constructor(constructorType, actuals, keyArgValues), ctx);
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
