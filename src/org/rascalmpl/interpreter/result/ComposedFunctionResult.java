/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;

public class ComposedFunctionResult extends Result<IValue> implements IExternalValue, ICallableValue {
	
	private final Result<IValue> left;
	private final Result<IValue> right;
	private final boolean isStatic;
	private Type type;
	
	
	public <T extends Result<IValue> & IExternalValue & ICallableValue, 
			U extends Result<IValue> & IExternalValue & ICallableValue> 
		ComposedFunctionResult(T left, U right, IEvaluatorContext ctx) {
			super(RascalTypeFactory.getInstance().functionType(TypeFactory.getInstance().voidType(), TypeFactory.getInstance().voidType()), null, ctx);
			this.left = left;
			this.right = right;
			type = super.type;
			try {
				// trying to compute the composed type 
				type = left.getType().compose(right.getType());
			} catch(IllegalOperationException e) {
				// only function and overloaded function types are composable
				// void (void): when unable to compose the types or the results can not be composed due to type mismatch 
			}
		this.isStatic = left.isStatic() && right.isStatic();
	}
		
	@Override
	public int getArity() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasVarArgs() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isStatic() {
		return isStatic;
	}
	
	@Override
	public Type getType() {
		return this.type;
	}
	
	@Override
	public Result<IValue> call(IRascalMonitor monitor, Type[] argTypes,
			IValue[] argValues) {
		IRascalMonitor old = ctx.getEvaluator().setMonitor(monitor);
		try {
			return call(argTypes, argValues);
		}
		finally {
			ctx.getEvaluator().setMonitor(old);
		}
	}
	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		Result<IValue> rightResult = right.call(argTypes, argValues);
		return left.call(new Type[] { rightResult.getType() }, new IValue[] { rightResult.getValue() });
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right) {
		return right.composeFunction(this);
	}
	
	@Override
	public ComposedFunctionResult composeFunction(AbstractFunction that) {
		return new ComposedFunctionResult(that, this, ctx);
	}
	
	@Override
	public ComposedFunctionResult composeFunction(OverloadedFunction that) {
		return new ComposedFunctionResult(that, this, ctx);
	}
	
	@Override
	public ComposedFunctionResult composeFunction(ComposedFunctionResult that) {
		return new ComposedFunctionResult(that, this, ctx);
	}

	
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitExternal(this);
	}

	public boolean isEqual(IValue other) {
		return other == this;
	}
	
	@Override
	public IValue getValue() {
		return this;
	}
	
	@Override
	public String toString() {
		return left.toString() + " 'o' " + right.toString();
	}

	@Override
	public IEvaluator<Result<IValue>> getEval() {
		return (Evaluator) ctx;
	}
	
}
