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

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.UndeclaredAbstractDataTypeException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UndeclaredAnnotation;
import org.rascalmpl.interpreter.staticErrors.UndeclaredField;
import org.rascalmpl.interpreter.staticErrors.UndeclaredType;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperation;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class ConstructorResult extends NodeResult {

	public ConstructorResult(Type type, IConstructor cons, IEvaluatorContext ctx) {
		super(type, cons, ctx);
	}
	
	@Override
	public IConstructor getValue() {
		return (IConstructor)super.getValue();
	}
	
	@Override
	public Result<IBool> is(Name name) {
		return ResultFactory.bool(getValue().getName().equals(Names.name(name)), ctx);
	}
	
	@Override
	public Result<IBool> has(Name name) {
		String sname = Names.name(name);
		return ResultFactory.bool(getValue().has(sname) || (ctx.getCurrentEnvt().getStore().getKeywordParameterType(getValue().getConstructorType(), sname) != null), ctx);
	}
	
	@Override
	public Result<IBool> isDefined(Name name) {
		String sname = Names.name(name);
		return ResultFactory.bool(getValue().has(sname) || getValue().asWithKeywordParameters().hasParameter(sname), ctx);
	}
	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) {
		throw new UnsupportedOperation("Can not call a constructed " + getType() + " node as a function", ctx.getCurrentAST());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store) {
		try {
			Type consType = getValue().getConstructorType();
			ConstructorFunction cons = ctx.getCurrentEnvt().getConstructorFunction(consType);
			Type kwTypes = cons != null ? cons.getKeywordArgumentTypes(ctx.getCurrentEnvt()) : getTypeFactory().voidType();
			
			
			if (!getType().hasField(name, store) && !kwTypes.hasField(name)) {
				throw new UndeclaredField(name, getType(), ctx.getCurrentAST());
			}

			if (!consType.hasField(name) && !kwTypes.hasField(name)) {
				throw RuntimeExceptionFactory.noSuchField(name, ctx.getCurrentAST(), null);
			}
			
			if (kwTypes.hasField(name)) { // it's a keyword parameter
				IValue parameter = getValue().asWithKeywordParameters().getParameter(name);
				
				if (parameter == null) {  
					return (Result<U>) cons.computeDefaultKeywordParameter(name, getValue(), ctx.getCurrentEnvt());
				}
				else {
					return makeResult(kwTypes.getFieldType(name), parameter, ctx);
				}
			}
			else { // it is a normal parameter
				int index = consType.getFieldIndex(name);
				return makeResult(consType.getFieldType(index), getValue().get(index), ctx);
			}
		} catch (UndeclaredAbstractDataTypeException e) {
			throw new UndeclaredType(getType().toString(), ctx.getCurrentAST());
		}
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store) {
		ConstructorFunction cons = ctx.getCurrentEnvt().getConstructorFunction(getValue().getConstructorType());
		Type kwTypes = cons.getKeywordArgumentTypes(ctx.getCurrentEnvt());
		
		if (!getType().hasField(name, store) && !kwTypes.hasField(name)) {
			throw new UndeclaredField(name, getType(), ctx.getCurrentAST());
		}
		
		Type nodeType = getValue().getConstructorType();
		if (!nodeType.hasField(name) && !kwTypes.hasField(name)) {
			throw RuntimeExceptionFactory.noSuchField(name, ctx.getCurrentAST(), null);
		}				
		
		if (kwTypes.hasField(name)) {
			Type fieldType = kwTypes.getFieldType(name);
			if (!repl.getType().isSubtypeOf(fieldType)) {
				throw new UnexpectedType(fieldType, repl.getType(), ctx.getCurrentAST());
			}
			
			return makeResult(getType(), getValue().asWithKeywordParameters().setParameter(name, repl.getValue()), ctx);
		}
		else {
		  int index = nodeType.getFieldIndex(name);
		  Type fieldType = nodeType.getFieldType(index);
		  if (!repl.getType().isSubtypeOf(fieldType)) {
		    throw new UnexpectedType(fieldType, repl.getType(), ctx.getCurrentAST());
		  }

		  return makeResult(getType(), getValue().set(index, repl.getValue()), ctx);
		}
	}

	
	@Override
	public <U extends IValue> Result<U> getAnnotation(String annoName, Environment env) {
		Type annoType = env.getAnnotationType(getType(), annoName);
	
		if (annoType == null) {
			throw new UndeclaredAnnotation(annoName, getType(), ctx.getCurrentAST());
		}
	
		IValue annoValue = getValue().asAnnotatable().getAnnotation(annoName);
		if (annoValue == null) {
			throw RuntimeExceptionFactory.noSuchAnnotation(annoName, ctx.getCurrentAST(), null);
		}
		// TODO: applyRules?
		return makeResult(annoType, annoValue, ctx);
	}
	
}
