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
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.staticErrors.ArgumentsMismatchError;

public class ComposedFunctionResult extends Result<IValue> implements IExternalValue, ICallableValue {
	private final static TypeFactory TF = TypeFactory.getInstance();
	
	private final Result<IValue> left;
	private final Result<IValue> right;
	private final boolean isStatic;
	private Type type;
	
	
	public <T extends Result<IValue> & IExternalValue & ICallableValue, 
			U extends Result<IValue> & IExternalValue & ICallableValue> 
				ComposedFunctionResult(T left, U right, Type type, IEvaluatorContext ctx) {
					super(type, null, ctx);
					this.left = left;
					this.right = right;
					this.type = type;
					this.isStatic = left.isStatic() && right.isStatic();
				}
	
	public <T extends Result<IValue> & IExternalValue & ICallableValue, 
	U extends Result<IValue> & IExternalValue & ICallableValue> 
		ComposedFunctionResult(T left, U right, IEvaluatorContext ctx) {
			super(TF.voidType(), null, ctx);
			this.left = left;
			this.right = right;
			this.type = super.type;
			try {
				// trying to compute the composed type 
				type = left.getType().compose(right.getType());
			} catch(IllegalOperationException e) {
				// if the type of one of the arguments is of the type 'value' (e.g., the type of an overloaded function can be of the type 'value')
			}
			this.isStatic = left.isStatic() && right.isStatic();
		}

	
	public boolean isNonDeterministic() {
		return false;
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
	
	public Result<IValue> getLeft() {
		return this.left;
	}
	
	public Result<IValue> getRight() {
		return this.right;
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
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> right) {
		return right.addFunctionNonDeterministic(this);
	}
	
	@Override
	public ComposedFunctionResult addFunctionNonDeterministic(AbstractFunction that) {
		return new ComposedFunctionResult.NonDeterministic(that, this, ctx);
	}
	
	@Override
	public ComposedFunctionResult addFunctionNonDeterministic(OverloadedFunction that) {
		return new ComposedFunctionResult.NonDeterministic(that, this, ctx);
	}
	
	@Override
	public ComposedFunctionResult addFunctionNonDeterministic(ComposedFunctionResult that) {
		return new ComposedFunctionResult.NonDeterministic(that, this, ctx);
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
	
	public static class NonDeterministic extends ComposedFunctionResult {
		private final static TypeFactory TF = TypeFactory.getInstance();
		
		public <T extends Result<IValue> & IExternalValue & ICallableValue, 
				U extends Result<IValue> & IExternalValue & ICallableValue> 
					NonDeterministic(T left, U right, IEvaluatorContext ctx) {	
						super(left, right, TF.voidType().lub(left.getType()).lub(right.getType()), ctx);
					}
		
		@Override
		public boolean isNonDeterministic() {
			return true;
		}
				
		@Override
		public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
			Failure f1 = null;
			ArgumentsMismatchError e1 = null;
			try {
				try {
					return getRight().call(argTypes, argValues);
				} catch(ArgumentsMismatchError e) {
					// try another one
					e1 = e;
				} catch(Failure e) {
					// try another one
					f1 = e;
				}
				return getLeft().call(argTypes, argValues);
			} catch(ArgumentsMismatchError e2) {
				throw new ArgumentsMismatchError(
						"The called signature does not match signatures in the '+' composition:\n" 
							+ ((e1 != null) ? (e1.getMessage() + e2.getMessage()) : e2.getMessage()), ctx.getCurrentAST());
			} catch(Failure f2) {
				throw new Failure("Both functions in the '+' composition have failed:\n " 
									+ ((f1 != null) ? f1.getMessage() + f2.getMessage() : f2.getMessage()));
			}
		}
		
		@Override
		public String toString() {
			return getLeft().toString() + " '+' " + getRight().toString();
		}

	}
	
}
