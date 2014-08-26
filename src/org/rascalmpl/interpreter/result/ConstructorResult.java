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

import java.util.ArrayList;
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
		return ResultFactory.bool(getValue().has(Names.name(name)), ctx);
	}
	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) {
		throw new UnsupportedOperation("Can not call a constructed " + getType() + " node as a function", ctx.getCurrentAST());
	}
	
	@Override
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store) {
		try {
			if (!getType().hasField(name, store) && !getType().hasKeywordParameter(name, store)) {
				throw new UndeclaredField(name, getType(), ctx.getCurrentAST());
			}

			Type nodeType = getValue().getConstructorType();
			if (!nodeType.hasField(name) && !nodeType.hasKeywordParameter(name)) {
				throw RuntimeExceptionFactory.noSuchField(name, ctx.getCurrentAST(), null);
			}

			if (nodeType.hasKeywordParameter(name)) {
				IValue parameter = getValue().asWithKeywordParameters().getParameter(name);
				
				if (parameter == null) {
					// then its time to compute defaults.
					Map<String, IValue> kwArgs = ctx.getCurrentEnvt().getConstructorFunction(getValue().getConstructorType()).computeKeywordArgs(childrenAsArray(), getValue().asWithKeywordParameters().getParameters());
					parameter = kwArgs.get(name);

					assert parameter != null; // this shouldn't happen because defaults are defined 
				}
				return makeResult(nodeType.getKeywordParameterType(name), parameter, ctx);
			}
			else {
				int index = nodeType.getFieldIndex(name);
				return makeResult(nodeType.getFieldType(index), getValue().get(index), ctx);
			}
		} catch (UndeclaredAbstractDataTypeException e) {
			throw new UndeclaredType(getType().toString(), ctx.getCurrentAST());
		}
	}
	
	private IValue[] childrenAsArray() {
		IValue[] result = new IValue[getValue().arity()];
		
		for (int i = 0; i < getValue().arity(); i++) {
			result[i] = getValue().get(i);
		}
		
		return result;
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store) {
		if (!getType().hasField(name, store) && !getValue().getConstructorType().hasKeywordParameter(name)) {
			throw new UndeclaredField(name, getType(), ctx.getCurrentAST());
		}
		
		Type nodeType = getValue().getConstructorType();
		if (!nodeType.hasField(name) && !nodeType.hasKeywordParameter(name)) {
			throw RuntimeExceptionFactory.noSuchField(name, ctx.getCurrentAST(), null);
		}				
		
		if (nodeType.hasKeywordParameter(name)) {
			Type fieldType = nodeType.getKeywordParameterType(name);
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
