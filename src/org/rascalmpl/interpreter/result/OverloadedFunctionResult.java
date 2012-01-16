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
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.staticErrors.ArgumentsMismatchError;
import org.rascalmpl.interpreter.staticErrors.UnguardedFailError;

public class OverloadedFunctionResult extends Result<IValue> implements IExternalValue, ICallableValue {
	private final static TypeFactory TF = TypeFactory.getInstance();
	
	private final List<AbstractFunction> primaryCandidates; // it should be a list to allow proper shadowing
	private final List<AbstractFunction> defaultCandidates; // it should be a list to allow proper shadowing
	private final String name;
	private final boolean isStatic;

	public OverloadedFunctionResult(String name, Type type, List<AbstractFunction> candidates, List<AbstractFunction> defaults, IEvaluatorContext ctx) {
		super(type, null, ctx);
		
		if (candidates.size() + defaults.size() <= 0) {
			throw new ImplementationError("at least need one function");
		}
		this.name = name;
		
		this.primaryCandidates = new ArrayList<AbstractFunction>(candidates.size());
		this.defaultCandidates = new ArrayList<AbstractFunction>(candidates.size());
		
	    primaryCandidates.addAll(candidates);
	    defaultCandidates.addAll(defaults);

	    isStatic = checkStatic(primaryCandidates) && checkStatic(defaultCandidates);
	}
	
	public OverloadedFunctionResult(AbstractFunction function) {
		super(function.getType(), null, function.getEval());
		this.name = function.getName();
		
		this.primaryCandidates = new ArrayList<AbstractFunction>(1);
		this.defaultCandidates = new ArrayList<AbstractFunction>(1);
		
		if (function.isDefault()) {
			defaultCandidates.add(function);
		}
		else {
			primaryCandidates.add(function);
		}
		
		this.isStatic = function.isStatic();
	}
	
	@Override
	public boolean isStatic() {
		return isStatic;
	}

	private boolean checkStatic(List<AbstractFunction> l) {
		for (ICallableValue f : l) {
			if (!f.isStatic()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public IValue getValue() {
		return this;
	}

	private Type lub(List<AbstractFunction> candidates) {
		Type lub = TF.voidType();
		
		for (AbstractFunction l : candidates) {
			lub = lub.lub(l.getType());
		}
		
		return lub;
	}

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
		Result<IValue> result = callWith(primaryCandidates, argTypes, argValues, defaultCandidates.size() <= 0);
		
		if (result == null && defaultCandidates.size() > 0) {
			result = callWith(defaultCandidates, argTypes, argValues, true);
		}

		if (result == null) {
			List<AbstractFunction> all = new ArrayList<AbstractFunction>(primaryCandidates.size() + defaultCandidates.size());
			all.addAll(primaryCandidates);
			all.addAll(defaultCandidates);
			throw new ArgumentsMismatchError(name, all, argTypes, ctx.getCurrentAST());
		}
		
		return result;
	}

	private Result<IValue> callWith(List<AbstractFunction> candidates, Type[] argTypes, IValue[] argValues, boolean mustSucceed) {
		AbstractFunction failed = null;
		Failure failure = null;
		
		for (AbstractFunction candidate : candidates) {
			try {
				return candidate.call(argTypes, argValues);
			}
			catch (MatchFailed m) {
				// could happen if pattern dispatched
			}
			catch (Failure e) {
				failed = candidate;
				failure = e;
				// could happen if function body throws fail
			}
		}
		
		if (failed != null && mustSucceed) {
			throw new UnguardedFailError(failed.ast, failure);
		}
		
		return null;
	}
	
	public OverloadedFunctionResult join(OverloadedFunctionResult other) {
		List<AbstractFunction> joined = new ArrayList<AbstractFunction>(other.primaryCandidates.size() + primaryCandidates.size());
		List<AbstractFunction> defJoined = new ArrayList<AbstractFunction>(other.defaultCandidates.size() + defaultCandidates.size());
		
		joined.addAll(primaryCandidates);
		defJoined.addAll(defaultCandidates);
		
		for (AbstractFunction cand : other.primaryCandidates) {
			if (!joined.contains(cand)) {
				joined.add(cand);
			}
		}
		
		for (AbstractFunction cand : other.defaultCandidates) {
			if (!defJoined.contains(cand)) {
				defJoined.add(cand);
			}
		}
		
		return new OverloadedFunctionResult(name, lub(joined).lub(lub(defJoined)), joined, defJoined, ctx);
	}
	
	public OverloadedFunctionResult add(AbstractFunction candidate) {
		List<AbstractFunction> joined = new ArrayList<AbstractFunction>(primaryCandidates.size() + 1);
		joined.addAll(primaryCandidates);
		List<AbstractFunction> defJoined = new ArrayList<AbstractFunction>(defaultCandidates.size() + 1);
		defJoined.addAll(defaultCandidates);
		
		if (candidate.isDefault() && !defJoined.contains(candidate)) {
			defJoined.add(candidate);
		}
		else if (!candidate.isDefault() && !joined.contains(candidate)) {
			joined.add(candidate);
		}
		
		return new OverloadedFunctionResult(name, lub(joined).lub(lub(defJoined)), joined, defJoined, ctx);
	}

	public Iterable<AbstractFunction> iterable() {
		return new Iterable<AbstractFunction>() {
			public Iterator<AbstractFunction> iterator() {
				return new Iterator<AbstractFunction> () {
					private final Iterator<AbstractFunction> primary = primaryCandidates.iterator();
					private final Iterator<AbstractFunction> defaults = defaultCandidates.iterator();
					
					public boolean hasNext() {
						return primary.hasNext() || defaults.hasNext();
					}

					public AbstractFunction next() {
						if (primary.hasNext()) {
							return primary.next();
						}
						return defaults.next();
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult other = (OverloadedFunctionResult) obj;
			return primaryCandidates.containsAll(other.primaryCandidates)
			&& other.primaryCandidates.containsAll(primaryCandidates)
			&& defaultCandidates.containsAll(other.defaultCandidates)
			&& other.defaultCandidates.containsAll(defaultCandidates);
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (AbstractFunction l : iterable()) {
			b.append(l.toString());
			b.append(' ');
		}
		
		return b.toString();
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitExternal(this);
	}

	public boolean isEqual(IValue other) {
		if (other instanceof OverloadedFunctionResult) {
			return primaryCandidates.equals(((OverloadedFunctionResult) other).primaryCandidates);
		}
		return false;
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(
			Result<V> that) {
		return that.equalToOverloadedFunction(this);
	}
	
	@Override
	public <U extends IValue> Result<U> equalToOverloadedFunction(
			OverloadedFunctionResult that) {
		return ResultFactory.bool(primaryCandidates.equals(that.primaryCandidates) && defaultCandidates.equals(that.defaultCandidates), ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(
			Result<V> that) {
		return that.compareOverloadedFunction(this);
	}
	
	@Override
	public <U extends IValue> Result<U> compareOverloadedFunction(
			OverloadedFunctionResult that) {
		if (that == this) {
			return ResultFactory.makeResult(TF.integerType(), getValueFactory().integer(0), ctx);
		}
		
		if (primaryCandidates.size() > that.primaryCandidates.size()) {
			return  ResultFactory.makeResult(TF.integerType(), getValueFactory().integer(1), ctx);
		}
		
		if (primaryCandidates.size() < that.primaryCandidates.size()) {
			 ResultFactory.makeResult(TF.integerType(), getValueFactory().integer(-1), ctx);
		}
		
		for (AbstractFunction f : primaryCandidates) {
			for (AbstractFunction g : that.primaryCandidates) {
				Result<U> result = f.compare(g);
				
				if (!((IInteger) result.getValue()).getStringRepresentation().equals("0")) {
					return result;
				}
			}
		}
		
		if (defaultCandidates.size() > that.defaultCandidates.size()) {
			return  ResultFactory.makeResult(TF.integerType(), getValueFactory().integer(1), ctx);
		}
		
		if (defaultCandidates.size() < that.defaultCandidates.size()) {
			 ResultFactory.makeResult(TF.integerType(), getValueFactory().integer(-1), ctx);
		}
		
		for (AbstractFunction f : defaultCandidates) {
			for (AbstractFunction g : that.defaultCandidates) {
				Result<U> result = f.compare(g);
				
				if (!((IInteger) result.getValue()).getStringRepresentation().equals("0")) {
					return result;
				}
			}
		}
		
		return ResultFactory.makeResult(TF.integerType(), getValueFactory().integer(0), ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right) {
		return right.composeOverloadedFunction(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U extends IValue> Result<U> composeOverloadedFunction(OverloadedFunctionResult that) {
		List<AbstractFunction> newAlternatives = new ArrayList<AbstractFunction>(primaryCandidates.size());
		
		
		for (AbstractFunction f : primaryCandidates) {
			for (AbstractFunction g : that.primaryCandidates) {
				if (getTypeFactory().tupleType(f.getReturnType()).isSubtypeOf(g.getFunctionType().getArgumentTypes())) {
					newAlternatives.add(new ComposedFunctionResult(f, g, ctx));
				}
			}
		}
		
		List<AbstractFunction> newDefaults = new ArrayList<AbstractFunction>(defaultCandidates.size());
		
		for (AbstractFunction f : defaultCandidates) {
			for (AbstractFunction g : that.defaultCandidates) {
				if (getTypeFactory().tupleType(f.getReturnType()).isSubtypeOf(g.getFunctionType().getArgumentTypes())) {
					newDefaults.add(new ComposedFunctionResult(f, g, ctx));
				}
			}
		}
		
		if (newAlternatives.size() + newDefaults.size() == 0) {
			return undefinedError("composition", that);
		}
		
		return (Result<U>) new OverloadedFunctionResult(name, getType(), newAlternatives, newDefaults, ctx);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U extends IValue> Result<U> composeFunction(AbstractFunction g) {
		List<AbstractFunction> newAlternatives = new ArrayList<AbstractFunction>(primaryCandidates.size());

		for (AbstractFunction f : primaryCandidates) {
			if (getTypeFactory().tupleType(f.getReturnType()).isSubtypeOf(g.getFunctionType().getArgumentTypes())) {
				newAlternatives.add(new ComposedFunctionResult(f, g, ctx));
			}
		}
		
		List<AbstractFunction> newDefaults = new ArrayList<AbstractFunction>(defaultCandidates.size());

		for (AbstractFunction f : defaultCandidates) {
			if (getTypeFactory().tupleType(f.getReturnType()).isSubtypeOf(g.getFunctionType().getArgumentTypes())) {
				newAlternatives.add(new ComposedFunctionResult(f, g, ctx));
			}
		}
		
		if (newAlternatives.size() == 0) {
			return undefinedError("composition", g);
		}
		
		return (Result<U>) new OverloadedFunctionResult(name, getType(), newAlternatives, newDefaults, ctx);
	}
	
	public List<AbstractFunction> getTests() {
		List<AbstractFunction> result = new LinkedList<AbstractFunction>();
		for (AbstractFunction f : primaryCandidates) {
			if (f.isTest()) {
				result.add(f);
			}
		}
		
		for (AbstractFunction f : defaultCandidates) {
			if (f.isTest()) {
				result.add(f);
			}
		}
		
		return result;
	}
	
	public String getName() {
		return name;
	}
	
	public Evaluator getEval(){
		return (Evaluator) ctx;
	}
}
