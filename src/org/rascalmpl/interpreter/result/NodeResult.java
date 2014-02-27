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

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UndeclaredAnnotation;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptArity;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class NodeResult extends ElementResult<INode> {

	public NodeResult(Type type, INode node, IEvaluatorContext ctx) {
		super(type, node, ctx);
	}

	@Override
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return that.equalToNode(this);
	}

	@Override
	public <V extends IValue> Result<IBool> nonEquals(Result<V> that) {
		return that.nonEqualToNode(this);
	}

	@Override
	public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualNode(this);
	}
	
	@Override
	public Result<IBool> is(Name name) {
		return ResultFactory.bool(getValue().getName().equals(Names.name(name)), ctx);
	}
	
	@Override
	public Result<IBool> has(Name name) {
		INode node = getValue();
		if(node instanceof IConstructor)
			return ResultFactory.bool(((IConstructor) node).has(Names.name(name)), ctx);
		else
			return ResultFactory.bool(false, ctx);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts) {
		if (subscripts.length != 1) {
			throw new UnsupportedSubscriptArity(getType(), subscripts.length, ctx.getCurrentAST());
		}
		if (!((Result<IValue>)subscripts[0]).getType().isInteger()) {
			throw new UnexpectedType(getTypeFactory().integerType(), 
					((Result<IValue>)subscripts[0]).getType(), ctx.getCurrentAST());
		}
		IInteger index = ((IntegerResult)subscripts[0]).getValue();
		int idx = index.intValue();
		if (idx < 0){
			idx = idx + getValue().arity();
		}
		if ( (idx >= getValue().arity()) || (idx < 0)) {
			throw RuntimeExceptionFactory.indexOutOfBounds(index, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		Type elementType = getTypeFactory().valueType();
		return makeResult(elementType, getValue().get(idx), ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> slice(Result<?> first, Result<?> second, Result<?> end) {
		return super.slice(first, second, end, getValue().arity());
	}
	
	public Result<IValue> makeSlice(int first, int second, int end){
		IListWriter w = getValueFactory().listWriter();
		int increment = second - first;
		if(first == end || increment == 0){
			// nothing to be done
		} else
		if(first <= end){
			for(int i = first; i >= 0 && i < end; i += increment){
				w.append(getValue().get(i));
			}
		} else {
			for(int j = first; j >= 0 && j > end && j < getValue().arity(); j += increment){
				w.append(getValue().get(j));
			}
		}
		TypeFactory tf = TypeFactory.getInstance();
		return makeResult(tf.listType(tf.valueType()), w.done(), ctx);
	}
	
	//////
	
	@Override
	protected LessThanOrEqualResult lessThanOrEqualNode(NodeResult that) {
	  INode left = that.getValue();
	  INode right = getValue();
	  
	  int compare = left.getName().compareTo(right.getName());
	  
	  if (compare <= -1) {
	    return new LessThanOrEqualResult(true, false, ctx);
	  }
	  
    if (compare >= 1){
      return new LessThanOrEqualResult(false, false, ctx);
    }
    
    // if the names are not ordered, then we order lexicographically on the arguments:
    
    int leftArity = left.arity();
    int rightArity = right.arity();
    
    for (int i = 0; i < Math.min(leftArity, rightArity); i++) {
       IValue leftArg = left.get(i);
       IValue rightArg = right.get(i);
       LessThanOrEqualResult loe = makeResult(leftArg.getType(), leftArg, ctx).lessThanOrEqual(makeResult(rightArg.getType(), rightArg,ctx));
       
       if (loe.getLess() && !loe.getEqual()) {
         return new LessThanOrEqualResult(true, false, ctx);
       }
       
       if (!loe.getEqual()) { 
         return new LessThanOrEqualResult(false, false, ctx);
       }
    }
    
    return new LessThanOrEqualResult(leftArity < rightArity, leftArity == rightArity, ctx);
	}

	@Override
	protected Result<IBool> equalToNode(NodeResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected Result<IBool> nonEqualToNode(NodeResult that) {
		return that.nonEqualityBoolean(this);
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
