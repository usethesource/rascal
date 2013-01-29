/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

import java.util.HashMap;
import java.util.Map;

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
import org.rascalmpl.interpreter.staticErrors.ArgumentsMismatch;
import org.rascalmpl.interpreter.types.FunctionType;

public class ComposedFunctionResult extends Result<IValue> implements IExternalValue, ICallableValue {
	private final static TypeFactory TF = TypeFactory.getInstance();
	
	private final Result<IValue> left;
	private final Result<IValue> right;
	private final boolean isStatic;
	
	private Type type;
	
	private boolean isOpenRecursive = false;
	private boolean isMixin = false;
	
	private Result<IValue> self;
	private Map<String, Result<IValue>> openFunctions;
	
	public <T extends Result<IValue> & IExternalValue & ICallableValue, 
			U extends Result<IValue> & IExternalValue & ICallableValue> 
				ComposedFunctionResult(T left, U right, Map<String, Result<IValue>> openFunctions, Type type, IEvaluatorContext ctx) {
					super(type, null, ctx);
					this.left = left;
					this.right = right;
					if(isComposedWithMixin(left, right)) {
						this.type = ((FunctionType) left.getType()).getReturnType();
						this.isMixin = true;
					}
					this.type = type;
					this.isStatic = left.isStatic() && right.isStatic();
					
					this.self = null;
					this.openFunctions = openFunctions;
					if(this.openFunctions != null 
						&& right.getName() != null && !right.getName().equals("")) 
						this.openFunctions.put(right.getName(), this);
				}
	
	public <T extends Result<IValue> & IExternalValue & ICallableValue, 
			U extends Result<IValue> & IExternalValue & ICallableValue> 
				ComposedFunctionResult(T left, U right, Map<String, Result<IValue>> openFunctions, IEvaluatorContext ctx) {
					this(left, right, openFunctions, computeType(left.getType(), right.getType()), ctx);
				}
	
	// Wrapper to compose with mixins under open recursion
	private ComposedFunctionResult(Result<IValue> right, Map<String, Result<IValue>> openFunctions, Result<IValue> self, IEvaluatorContext ctx) {
		super(right.getType(), null, ctx);
		this.left = null;
		this.right = right;
		this.type = super.type;
		this.isStatic = ((ICallableValue) right).isStatic();
		this.self = self;
		this.openFunctions = openFunctions;
	}
	
	private static Type computeType(Type left, Type right) {
		Type result = TF.voidType();
		try {
			// trying to compose types
			result = left.compose(right);
		} catch(IllegalOperationException e) {
			// if the type of any of the arguments is 'value' (e.g., the type of an overloaded function)
		}
		return result;
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
	public String getName() {
		return null;
	}
	
	@Override
	public boolean isComposedFunctionResult() {
		return true;
	}
	
	@Override
	public boolean isOpenRecursive() {
		return this.isOpenRecursive;
	}
	
	public void setOpenRecursive(boolean isOpenRecursive) {
		this.isOpenRecursive = isOpenRecursive;
	}
		
	@Override
	public Type getType() {
		return this.type;
	}
	
	@SuppressWarnings("unchecked")
	public <U extends Result<IValue> & IExternalValue & ICallableValue> U getLeft() {
		return (U) this.left;
	}
	
	@SuppressWarnings("unchecked")
	public <U extends Result<IValue> & IExternalValue & ICallableValue> U getRight() {
		return (U) this.right;
	}
	
	public Map<String, Result<IValue>> getOpenFunctions(){
		return this.openFunctions;
	}
		
	@Override
	public Result<IValue> call(IRascalMonitor monitor, Type[] argTypes, 
			IValue[] argValues, Map<String, Result<IValue>> keyArgValues) {
		return call(monitor, argTypes, argValues, keyArgValues, null, null);
	}
	
	@Override
	public Result<IValue> call(IRascalMonitor monitor, Type[] argTypes,
			IValue[] argValues, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
		IRascalMonitor old = ctx.getEvaluator().setMonitor(monitor);
		try {
			return call(argTypes, argValues, keyArgValues, self, openFunctions);
		}
		finally {
			ctx.getEvaluator().setMonitor(old);
		}
	}
	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, Result<IValue>> keyArgValues) {
		return call(argTypes, argValues, keyArgValues, null, null);
	}
	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
		if(isMixin) {
			ComposedFunctionResult right_ = null;
			if(isOpenRecursive) {
				if(self != null) right_ = new ComposedFunctionResult(this.right, this.openFunctions, self, ctx); 
				else right_ = new ComposedFunctionResult(this.right, this.openFunctions, this, ctx);
				right_.setOpenRecursive(isOpenRecursive);
			}
			right_ = new ComposedFunctionResult(this.right, this.openFunctions, null, ctx);
			return left.call(new Type[] { right_.getType() }, new IValue[] { right_.getValue() }, null)
					   .call(argTypes, argValues, keyArgValues, null, null);
		}
		if(this.left == null)
				return right.call(argTypes, argValues, keyArgValues, this.self, this.openFunctions);
		if(self == null && isOpenRecursive) self = this;
		Result<IValue> rightResult = right.call(argTypes, argValues, keyArgValues, self, this.openFunctions);
		return left.call(new Type[] { rightResult.getType() }, new IValue[] { rightResult.getValue() }, null);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> right) {
		return right.addFunctionNonDeterministic(this, true);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> right, boolean isOpenRecursive) {
		return right.addFunctionNonDeterministic(this, isOpenRecursive);
	}
	
	@Override
	public ComposedFunctionResult addFunctionNonDeterministic(AbstractFunction that, boolean isOpenRecursive) {
		ComposedFunctionResult result = new ComposedFunctionResult.NonDeterministic(that, this, ctx);
		result.setOpenRecursive(isOpenRecursive);
		return result;
	}
	
	@Override
	public ComposedFunctionResult addFunctionNonDeterministic(OverloadedFunction that, boolean isOpenRecursive) {
		ComposedFunctionResult result = new ComposedFunctionResult.NonDeterministic(that, this, ctx);
		result.setOpenRecursive(isOpenRecursive);
		return result;
	}
	
	@Override
	public ComposedFunctionResult addFunctionNonDeterministic(ComposedFunctionResult that, boolean isOpenRecursive) {
		ComposedFunctionResult result = new ComposedFunctionResult.NonDeterministic(that, this, ctx);
		result.setOpenRecursive(isOpenRecursive);
		return result;
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right) {
		return right.composeFunction(this, new HashMap<String, Result<IValue>>(), true);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right, Map<String, Result<IValue>> openFunctions, boolean isOpenRecursive) {
		return right.composeFunction(this, openFunctions, isOpenRecursive);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <U extends IValue> Result<U> composeFunction(AbstractFunction that, Map<String, Result<IValue>> openFunctions, boolean isOpenRecursive) {
		ComposedFunctionResult result = new ComposedFunctionResult(that, this, openFunctions, ctx);
		result.setOpenRecursive(isOpenRecursive);
		return (Result<U>) result;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <U extends IValue> Result<U> composeFunction(OverloadedFunction that, Map<String, Result<IValue>> openFunctions, boolean isOpenRecursive) {
		ComposedFunctionResult result = new ComposedFunctionResult(that, this, openFunctions, ctx);
		result.setOpenRecursive(isOpenRecursive);
		return (Result<U>) result;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <U extends IValue> Result<U> composeFunction(ComposedFunctionResult that, Map<String, Result<IValue>> openFunctions, boolean isOpenRecursive) {
		ComposedFunctionResult result = new ComposedFunctionResult(that, this, openFunctions, ctx);
		result.setOpenRecursive(isOpenRecursive);
		return (Result<U>) result;
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
		return ( (left != null) ? left.toString() + (isOpenRecursive ? " 'o' " : " 'o.' ") : "") + right.toString();
	}

	@Override
	public IEvaluator<Result<IValue>> getEval() {
		return (Evaluator) ctx;
	}
	
	private static <T extends Result<IValue> & IExternalValue & ICallableValue, 
					U extends Result<IValue> & IExternalValue & ICallableValue> 
				boolean isComposedWithMixin(T left, U right) {
					if(left.getType() instanceof FunctionType && right.getType() instanceof FunctionType) {
						FunctionType ltype = (FunctionType) left.getType();
						FunctionType rtype = (FunctionType) right.getType();
						if(ltype.isMixin() && !rtype.isMixin()) return true;
					}
					return false;
				}
	
	public static class NonDeterministic extends ComposedFunctionResult {
		private final static TypeFactory TF = TypeFactory.getInstance();
		
		public <T extends Result<IValue> & IExternalValue & ICallableValue, 
				U extends Result<IValue> & IExternalValue & ICallableValue> 
					NonDeterministic(T left, U right, IEvaluatorContext ctx) {	
						super(left, right, null, TF.voidType().lub(left.getType()).lub(right.getType()), ctx);
					}
		
		@Override
		public boolean isNonDeterministic() {
			return true;
		}
				
		@Override
		public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
			Failure f1 = null;
			ArgumentsMismatch e1 = null;
			if(this.isOpenRecursive() && self == null) self = this;
			try {
				try {
					return getRight().call(argTypes, argValues, null, self, this.getOpenFunctions());
				} catch(ArgumentsMismatch e) {
					// try another one
					e1 = e;
				} catch(Failure e) {
					// try another one
					f1 = e;
				}
				return getLeft().call(argTypes, argValues, null, self, this.getOpenFunctions());
			} catch(ArgumentsMismatch e2) {
				throw new ArgumentsMismatch(
						"The called signature does not match signatures in the '+' composition:\n" 
							+ ((e1 != null) ? (e1.getMessage() + e2.getMessage()) : e2.getMessage()), ctx.getCurrentAST());
			} catch(Failure f2) {
				throw new Failure("Both functions in the '+' composition have failed:\n " 
									+ ((f1 != null) ? f1.getMessage() + f2.getMessage() : f2.getMessage()));
			}
		}
		
		@Override
		public String toString() {
			return getLeft().toString() + (isOpenRecursive() ? " '+' " : " '.+.' ") + getRight().toString();
		}

	}

	@Override
	public boolean hasKeywordArgs() {
		return false;
	}
	
}
