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
 *   * Wietse Venema - wietsevenema@gmail.com - CWI
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.interpreter.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
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

public class OverloadedFunction extends Result<IValue> implements IExternalValue, ICallableValue {
	private final static TypeFactory TF = TypeFactory.getInstance();

	private final List<AbstractFunction> primaryCandidates; // it should be a list to allow proper shadowing
	private final List<AbstractFunction> defaultCandidates; // it should be a list to allow proper shadowing
	private final String name;
	private final boolean isStatic;

	public OverloadedFunction(String name, Type type, List<AbstractFunction> candidates, List<AbstractFunction> defaults, IEvaluatorContext ctx) {
		super(type, null, ctx);

		if (candidates.size() + defaults.size() <= 0) {
			throw new ImplementationError("at least need one function");
		}
		this.name = name;

		this.primaryCandidates = new ArrayList<AbstractFunction>(candidates.size());
		this.defaultCandidates = new ArrayList<AbstractFunction>(candidates.size());

		addAll(primaryCandidates, candidates, true);
		addAll(defaultCandidates, defaults, false);

		isStatic = checkStatic(primaryCandidates) && checkStatic(defaultCandidates);
	}

	public OverloadedFunction(AbstractFunction function) {
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

	public OverloadedFunction(String name, List<AbstractFunction> funcs) {
		super(lub(funcs), null, funcs.iterator().next().getEval());
		this.name = name;

		this.primaryCandidates = new ArrayList<AbstractFunction>(1);
		this.defaultCandidates = new ArrayList<AbstractFunction>(1);

		addAll(primaryCandidates, funcs, true);
		addAll(defaultCandidates, funcs, false);

		this.isStatic = checkStatic(funcs);
	}

	/**
	 * This function groups  occurrences of pattern dispatched functions as one "PatternFunction" that
	 * has a hash table to look up based on outermost function symbol. A group is a bunch
	 * of functions that have an ADT as a first parameter type and a call or tree pattern with a fixed name as
	 * the first parameter pattern, or it is a singleton other case. The addAll function retains the order of
	 * the functions from the candidates list, in order to preserve shadowing rules!
	 */
	private void addAll(List<AbstractFunction> container, List<AbstractFunction> candidates, boolean nonDefault) {
		Map<String, List<AbstractFunction>> constructors = new HashMap<String,List<AbstractFunction>>();
		Map<IConstructor, List<AbstractFunction>> productions = new HashMap<IConstructor, List<AbstractFunction>>();
		List<AbstractFunction> other = new LinkedList<AbstractFunction>();


		for (AbstractFunction func : candidates) {
			if (nonDefault && func.isDefault()) {
				continue;
			}
			if (!nonDefault && !func.isDefault()) {
				continue;
			}

			String label = null;
			IConstructor prod = null;

			if (func.isPatternDispatched()) {
				// this one is already hashed, but we might find more to add to that map in the next round
				Map<String, List<AbstractFunction>> funcMap = ((AbstractPatternDispatchedFunction) func).getMap();
				for (String key : funcMap.keySet()) {
					addFuncsToMap(constructors, funcMap.get(key), key);
				}
			}
			else if (func.isConcretePatternDispatched()) {
				// this one is already hashed, but we might find more to add to that map in the next round
				Map<IConstructor, List<AbstractFunction>> funcMap = ((ConcretePatternDispatchedFunction) func).getMap();
				for (IConstructor key : funcMap.keySet()) {
					addProdsToMap(productions, funcMap.get(key), key);
				}
			}
			else {
				// a new function definition, that may be hashable
				label = func.getFirstOutermostConstructorLabel();
				prod = func.getFirstOutermostProduction();

				if (label != null) {
					// we found another one to hash
					addFuncToMap(constructors, func, label);
				}
				else if (prod != null) {
					addProdToMap(productions, func, prod);
				}
				else {
					other.add(func);
				}
			}
		}

		if (!constructors.isEmpty()) {
			container.add(new AbstractPatternDispatchedFunction(ctx.getEvaluator(), name, type, constructors));
		}
		if (!productions.isEmpty()) {
			container.add(new ConcretePatternDispatchedFunction(ctx.getEvaluator(), name, type, productions));
		}
		container.addAll(other);
	}

	private void addFuncToMap(Map<String, List<AbstractFunction>> map,
			AbstractFunction func, String label) {
		List<AbstractFunction> l = map.get(label);
		if (l == null) {
			l = new LinkedList<AbstractFunction>();
			map.put(label, l);
		}
		l.add(func);
	}

	private void addProdToMap(Map<IConstructor, List<AbstractFunction>> map,
			AbstractFunction func, IConstructor label) {
		List<AbstractFunction> l = map.get(label);
		if (l == null) {
			l = new LinkedList<AbstractFunction>();
			map.put(label, l);
		}
		l.add(func);
	}

	private void addFuncsToMap(Map<String, List<AbstractFunction>> map,
			List<AbstractFunction> funcs, String label) {
		List<AbstractFunction> l = map.get(label);
		if (l == null) {
			l = new LinkedList<AbstractFunction>();
			map.put(label, l);
		}
		l.addAll(funcs);
	}

	private void addProdsToMap(Map<IConstructor, List<AbstractFunction>> map,
			List<AbstractFunction> funcs, IConstructor label) {
		List<AbstractFunction> l = map.get(label);
		if (l == null) {
			l = new LinkedList<AbstractFunction>();
			map.put(label, l);
		}
		l.addAll(funcs);
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

	private static boolean checkStatic(List<AbstractFunction> l) {
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

	private static Type lub(List<AbstractFunction> candidates) {
		Type lub = TF.voidType();

		for (AbstractFunction l : candidates) {
			lub = lub.lub(l.getType());
		}

		return lub;
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

	private static Result<IValue> callWith(List<AbstractFunction> candidates, Type[] argTypes, IValue[] argValues, boolean mustSucceed) {
		AbstractFunction failed = null;
		Failure failure = null;

		for (AbstractFunction candidate : candidates) {
			if ((candidate.hasVarArgs() && argValues.length >= candidate.getArity() - 1)
					|| candidate.getArity() == argValues.length) {
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
		}

		if (failed != null && mustSucceed) {
			throw new UnguardedFailError(failed.ast, failure);
		}

		return null;
	}

	public OverloadedFunction join(OverloadedFunction other) {
		if (other == null) {
			return this;
		}
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

		return new OverloadedFunction("(" + name + "+" + other.getName() + ")", lub(joined).lub(lub(defJoined)), joined, defJoined, ctx);
	}

	public OverloadedFunction add(AbstractFunction candidate) {
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

		return new OverloadedFunction("(" + name + "+" + candidate.getName() + ")" , lub(joined).lub(lub(defJoined)), joined, defJoined, ctx);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OverloadedFunction) {
			OverloadedFunction other = (OverloadedFunction) obj;
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
		for (AbstractFunction l : primaryCandidates) {
			b.append(l.toString());
			b.append(' ');
		}
		for (AbstractFunction l : defaultCandidates) {
			b.append(l.toString());
			b.append(' ');
		}

		return b.toString();
	}

	@Override
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitExternal(this);
	}

	@Override
	public boolean isEqual(IValue other) {
		if (other instanceof OverloadedFunction) {
			return primaryCandidates.equals(((OverloadedFunction) other).primaryCandidates);
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
			OverloadedFunction that) {
		return ResultFactory.bool(primaryCandidates.equals(that.primaryCandidates) && defaultCandidates.equals(that.defaultCandidates), ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(
			Result<V> that) {
		return that.compareOverloadedFunction(this);
	}

	@Override
	public <U extends IValue> Result<U> compareOverloadedFunction(
			OverloadedFunction that) {
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
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> that) {
		return that.addFunctionNonDeterministic(this);
	}
	
	@Override
	public OverloadedFunction addFunctionNonDeterministic(AbstractFunction that) {
		return this.add(that);
	}

	@Override
	public OverloadedFunction addFunctionNonDeterministic(OverloadedFunction that) {
		return this.join(that);
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

	public List<AbstractFunction> getFunctions(){
		List<AbstractFunction> result = new LinkedList<AbstractFunction>();
		for (AbstractFunction f : primaryCandidates) {
			result.add(f);
		}

		for (AbstractFunction f : defaultCandidates) {
			result.add(f);
		}

		return result;
	}

	public List<AbstractFunction> getTests() {
		List<AbstractFunction> result = new LinkedList<AbstractFunction>();
		for (AbstractFunction f : getFunctions()) {
			if (f.isTest()) {
				result.add(f);
			}
		}
		return result;
	}

	public String getName() {
		return name;
	}

	@Override
	public Evaluator getEval(){
		return (Evaluator) ctx;
	}
}
