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
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.Map;
import java.util.Set;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.Name;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.ModuleEnvironment.GenericKeywordParameters;
import org.rascalmpl.interpreter.staticErrors.UndeclaredField;
import org.rascalmpl.interpreter.staticErrors.UndeclaredKeywordParameter;
import org.rascalmpl.interpreter.staticErrors.UndeclaredType;
import org.rascalmpl.interpreter.staticErrors.UnexpectedKeywordArgumentType;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperation;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.RascalValueFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.exceptions.UndeclaredAbstractDataTypeException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeStore;

public class ConstructorResult extends NodeResult {

	public ConstructorResult(Type type, IConstructor cons, IEvaluatorContext ctx) {
		super(type, cons, ctx);
	}

	@Override
	public IConstructor getValue() {
		return (IConstructor) super.getValue();
	}
	
	@Override
	public Result<IBool> is(Name name) {
		return ResultFactory.bool(
				getValue().getName().equals(Names.name(name)), ctx);
	}

	@Override
	public Result<IBool> has(Name name) {
		String sname = Names.name(name);
		return ResultFactory
				.bool(getValue().has(sname)
						|| (ctx.getCurrentEnvt()
								.getStore()
								.getKeywordParameterType(
										getValue().getConstructorType(), sname) != null),
						ctx);
	}

	@Override
	public Result<IBool> isDefined(Name name) {
	    try {
	        String sname = Names.name(name);
	        return ResultFactory.bool(getValue().has(sname)
	            || getValue().asWithKeywordParameters().hasParameter(sname),
	            ctx);
	    }
	    catch (Throw e) { // NoSuchAnnotation
	        // TODO Can only happen due to the simulation of annotations by kw parameters
	        return ResultFactory.bool(false, ctx);
	    }
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues,
			Map<String, IValue> keyArgValues) {
		throw new UnsupportedOperation("Can not call a constructed "
				+ getStaticType() + " node as a function", ctx.getCurrentAST());
	}

	@Override
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store) {
	    Type consType = getValue().getConstructorType();

	    try {
	        if (getStaticType().hasField(name, store)) {
	            return positionalFieldAccess(consType, name);
	        }
	        else if (getValue().getUninstantiatedConstructorType().hasKeywordField(name, store)) {
	            return keywordFieldAccess(consType, name, store);
	        }
	        else {
	            // If the keyword field was defined on any of the other constructors, then 
	            // we see this as a dynamic error. (the programmer could not have known since
	            // constructor types are not first class citizens in Rascal). Otherwise the programmer
	            // used a completely unknown field name and we flag it as static error.
	            for (Type alt : store.lookupAlternatives(getValue().getUninstantiatedConstructorType().getAbstractDataType())) {
	                if (store.hasKeywordParameter(alt, name)) {
	                    throw RuntimeExceptionFactory.noSuchField(name, ctx.getCurrentAST(), null); 
	                }
	            }

	            throw new UndeclaredField(name, getStaticType(), ctx.getCurrentAST());
	        }
	    }
	    catch (UndeclaredAbstractDataTypeException e) {
	        // this may happen when a value leaks via another module via a run-time dependency (a function call),
	        // while the type of the value has not actually been defined (via import or extend). It happens
	        // for example when "import" is used instead of "extend" and the programmer accidentally expected
	        // names to be transitively imported.
	        
	        throw new UndeclaredType(getStaticType().getName(), ctx.getCurrentAST());
	    }
	}
	
	public <U extends IValue> Result<U> positionalFieldAccess(Type consType, String name) {
	    if (consType.hasField(name)) {
	        int index = consType.getFieldIndex(name);
	        return makeResult(consType.getFieldType(index),
	            getValue().get(index), ctx);
	    }
	    else {
	        throw RuntimeExceptionFactory.noSuchField(name, ctx.getCurrentAST(), null);
	    }
	}
	
	@SuppressWarnings("unchecked")
	public <U extends IValue> Result<U> keywordFieldAccess(Type consType, String name, TypeStore store) {
	    try {
	        if (getValue().mayHaveKeywordParameters()) { 
	            Type kwType = store.getKeywordParameterType(getValue().getUninstantiatedConstructorType(), name);
	            
	            if (kwType == null) {
	                throw new UndeclaredKeywordParameter(getValue().getUninstantiatedConstructorType().getName(), name, ctx.getCurrentAST());
	            }
	            
	            IValue parameter = getValue().asWithKeywordParameters().getParameter(name);

	            if (parameter == null) { // the 'default' case, the field is not present, but it is declared 
	                ConstructorFunction cons = ctx.getCurrentEnvt().getConstructorFunction(consType);

	                if (cons != null) {
	                    return (Result<U>) cons.computeDefaultKeywordParameter(name, getValue(), ctx.getCurrentEnvt());
	                }
	                else {
	                    // The constructor is not in scope, but there might be a generic keyword parameter in scope nevertheless
	                    return (Result<U>) computeGenericDefaultKeywordParameter(name);
	                }
	            } else {
	                return makeResult(kwType, parameter, ctx);
	            }
	        }  

	        throw new UndeclaredField(name, getStaticType(), ctx.getCurrentAST());
	        
	    } catch (UndeclaredAbstractDataTypeException e) {
	        throw new UndeclaredType(getStaticType().toString(), ctx.getCurrentAST());
	    }
	}

	private Result<IValue> computeGenericDefaultKeywordParameter(String label) {
	    Set<GenericKeywordParameters> kwps = ctx.getCurrentEnvt().lookupGenericKeywordParameters(getStaticType());
	    IWithKeywordParameters<? extends IConstructor> wkw = getValue().asWithKeywordParameters();
	    Environment old = ctx.getCurrentEnvt();
	    
        for (GenericKeywordParameters gkw : kwps) {
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
                        kwResult = def.interpret(ctx.getEvaluator());
                    }
                    
                    if (name.equals(label)) {
                        // we have the one we need, bail out quickly
                        return kwResult;
                    }
                    else {
                        // we may need these in case they are used in the next definition
                        env.declareVariable(kwResult.getStaticType(), name);
                        env.storeVariable(name, kwResult);
                    }
                }
            }
            finally {
                ctx.setCurrentEnvt(old);
            }
        }
        
       throw new UndeclaredField(label, getStaticType(), ctx.getCurrentAST());
    }

    @Override
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store) {
		ConstructorFunction cons = ctx.getCurrentEnvt().getConstructorFunction(
				getValue().getConstructorType());
		Type kwTypes = cons.getKeywordArgumentTypes(ctx.getCurrentEnvt());

		if (!getStaticType().hasField(name, store) && store.hasKeywordParameter(getStaticType(), name)) {
			throw new UndeclaredField(name, getStaticType(), ctx.getCurrentAST());
		}

		Type nodeType = getValue().getConstructorType();
		if (!nodeType.hasField(name) && !kwTypes.hasField(name)) {
			throw RuntimeExceptionFactory.noSuchField(name, ctx.getCurrentAST(), null);
		}

		if (kwTypes.hasField(name)) {
			Type fieldType = kwTypes.getFieldType(name);
			if (!repl.getStaticType().isSubtypeOf(fieldType)) {
				throw new UnexpectedType(fieldType, repl.getStaticType(),
						ctx.getCurrentAST());
			}

			return makeResult(getStaticType(), getValue().asWithKeywordParameters().setParameter(name, repl.getValue()), ctx);
		} else {
		    // normal field
			int index = nodeType.getFieldIndex(name);
			Type fieldType = nodeType.getFieldType(index);
			if (!repl.getStaticType().isSubtypeOf(fieldType)) {
				throw new UnexpectedType(fieldType, repl.getStaticType(),
						ctx.getCurrentAST());
			}

			return makeResult(getStaticType(),
					getValue().set(index, repl.getValue()), ctx);
		}
	}

	@Override
	public <U extends IValue> Result<U> getAnnotation(String annoName, Environment env) {
	    // TODO: still simulating annotations with kw fields here
	    if (getValue().getType().isSubtypeOf(RascalValueFactory.Tree) && "loc".equals(annoName)) {
	        annoName = "src";
        }
	    try {
	        return keywordFieldAccess(getValue().getConstructorType(), annoName, env.getStore());
	    }
	    catch (UndeclaredField e) {
	        // this happens due to the simulation of annotations by keyword parameters if the parameter is not
	        // present (and we did not generate a default since that does not simulate the behavior of annotations)
	        throw RuntimeExceptionFactory.noSuchAnnotation(annoName, ctx.getCurrentAST(), ctx.getStackTrace());
	    }
	}

}
