/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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


import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UndeclaredAnnotationError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptArityError;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class NodeResult extends ElementResult<INode> {

	public NodeResult(Type type, INode node, IEvaluatorContext ctx) {
		super(type, node, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToNode(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToNode(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result) {
		return result.lessThanNode(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualNode(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result) {
		return result.greaterThanNode(this);
	}
	
	@Override
	public <U extends IValue> Result<U> is(Name name) {
		return ResultFactory.bool(getValue().getName().equals(Names.name(name)), ctx);
	}
	
	@Override
	public <U extends IValue> Result<U> has(Name name) {
		INode node = getValue();
		if(node instanceof IConstructor)
			return ResultFactory.bool(((IConstructor) node).has(Names.name(name)), ctx);
		else
			return ResultFactory.bool(false, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result) {
		return result.greaterThanOrEqualNode(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(
			Result<V> that) {
		return that.compareNode(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts) {
		if (subscripts.length != 1) {
			throw new UnsupportedSubscriptArityError(getType(), subscripts.length, ctx.getCurrentAST());
		}
		if (!((Result<IValue>)subscripts[0]).getType().isIntegerType()) {
			throw new UnexpectedTypeError(getTypeFactory().integerType(), 
					((Result<IValue>)subscripts[0]).getType(), ctx.getCurrentAST());
		}
		IInteger index = ((IntegerResult)subscripts[0]).getValue();
		if (index.intValue() >= getValue().arity()) {
			throw RuntimeExceptionFactory.indexOutOfBounds(index, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		Type elementType = getTypeFactory().valueType();
		return makeResult(elementType, getValue().get(index.intValue()), ctx);
	}
	
	//////
	
	@Override
	protected <U extends IValue> Result<U> lessThanNode(NodeResult that) {
		// note reversed args: we need that < this
		return bool((that.comparisonInts(this) < 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualNode(NodeResult that) {
		// note reversed args: we need that <= this
		return bool((that.comparisonInts(this) <= 0), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanNode(NodeResult that) {
		// note reversed args: we need that > this
		return bool((that.comparisonInts(this) > 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualNode(NodeResult that) {
		// note reversed args: we need that >= this
		return bool((that.comparisonInts(this) >= 0), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> equalToNode(NodeResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToNode(NodeResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareNode(NodeResult that) {
		// Note reversed args
		INode left = that.getValue();
		INode right = this.getValue();
		
		if (left.isEqual(right)) {
			return makeIntegerResult(0);
		}
		
		int str = left.getName().compareTo(right.getName());
		
		if (str == 0) {
			int leftArity = left.arity();
			int rightArity = right.arity();
			
			if (leftArity == rightArity) {
				Type valueType = getTypeFactory().valueType();
				
				for (int i = 0; i < leftArity; i++) {
					Result<IInteger> comp = makeResult(valueType, left.get(i), ctx).compare(makeResult(valueType, right.get(i), ctx));
					int val = comp.getValue().intValue();
					
					if (val != 0) {
						return makeIntegerResult(val);
					}
				}
				
				// this may happen when comparing nodes to constructors
				return makeIntegerResult(0);
			}
			
			if (left.arity() < right.arity()) {
				return makeIntegerResult(-1);
			}
			
			return makeIntegerResult(1);
		}
		
		return makeIntegerResult(str);
	}
	
	@Override
	public <U extends IValue> Result<U> getAnnotation(String annoName, Environment env) {
		Type annoType = env.getAnnotationType(getType(), annoName);
	
		if (annoType == null) {
			throw new UndeclaredAnnotationError(annoName, getType(), ctx.getCurrentAST());
		}
	
		IValue annoValue = getValue().getAnnotation(annoName);
		if (annoValue == null) {
			throw RuntimeExceptionFactory.noSuchAnnotation(annoName, ctx.getCurrentAST(), null);
		}
		// TODO: applyRules?
		return makeResult(annoType, annoValue, ctx);
	}

}
