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
 *   * Wietse Venema - wietsevenema@gmail.com - CWI
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.interpreter.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IWithKeywordParameters;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UnguardedFail;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;

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

	@Override
	public Type getKeywordArgumentTypes() {
	  ArrayList<String> labels = new ArrayList<>();
	  ArrayList<Type> types = new ArrayList<>();
	  // TODO: I am not sure this is what we want. Double names will end up twice in the tuple type...
	  
	  for (AbstractFunction c : primaryCandidates) {
	    Type args = c.getKeywordArgumentTypes();
	    
	    for (String label : args.getFieldNames()) {
	    	labels.add(label);
	    	types.add(args.getFieldType(label));
	    }
	  }
	  
	  for (AbstractFunction c : defaultCandidates) {
		  Type args = c.getKeywordArgumentTypes();

		  for (String label : args.getFieldNames()) {
			  labels.add(label);
			  types.add(args.getFieldType(label));
		  }  
	  }
	  
	  return TF.tupleType(types, labels); 
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
	
	private OverloadedFunction(String name, Type type, List<AbstractFunction> candidates, List<AbstractFunction> defaults, boolean isStatic, IEvaluatorContext ctx) {
		super(type, null, ctx);
		this.name = name;
		this.primaryCandidates = candidates;
		this.defaultCandidates = defaults;
		this.isStatic = isStatic;
	}
	
	
	@Override
	public OverloadedFunction cloneInto(Environment env) {
		List<AbstractFunction> newCandidates = new ArrayList<>();
		for (AbstractFunction f: primaryCandidates) {
			newCandidates.add((AbstractFunction) f.cloneInto(env));
		}
		
		List<AbstractFunction> newDefaultCandidates = new ArrayList<>();
		for (AbstractFunction f: defaultCandidates) {
			newDefaultCandidates.add((AbstractFunction) f.cloneInto(env));
		}
		OverloadedFunction of = new OverloadedFunction(name, getType(), newCandidates, newDefaultCandidates, isStatic, ctx);
		of.setPublic(isPublic());
		return of;
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
	public boolean hasKeywordArgs() {
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
		Set<FunctionType> alternatives = new HashSet<FunctionType>();
		Iterator<AbstractFunction> iter = candidates.iterator();
		if(!iter.hasNext()) {
			return TF.voidType();
		}
		FunctionType first = iter.next().getFunctionType();
		Type returnType = first.getReturnType();
		alternatives.add(first);
		
		AbstractFunction l = null;
		while(iter.hasNext()) {
			l = iter.next();
			if(l.getFunctionType().getReturnType() == returnType) {
				alternatives.add(l.getFunctionType());
			} else {
				return TF.valueType();
			}
		}

		return RascalTypeFactory.getInstance().overloadedFunctionType(alternatives);
	}

	 @Override
   public Result<IValue> call(IRascalMonitor monitor, Type[] argTypes, IValue[] argValues,
       Map<String, IValue> keyArgValues) {
    IRascalMonitor old = ctx.getEvaluator().setMonitor(monitor);
     try {
       return call(argTypes, argValues, keyArgValues);
     }
     finally {
       ctx.getEvaluator().setMonitor(old);
     }
   }

	@Override 
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) {
		Result<IValue> result = callWith(primaryCandidates, argTypes, argValues, keyArgValues, defaultCandidates.size() <= 0);

		if (result == null && defaultCandidates.size() > 0) {
			result = callWith(defaultCandidates, argTypes, argValues, keyArgValues, true);
		}

		if (result == null) {
			throw new MatchFailed();
		}

		return result;
	}

	private static Result<IValue> callWith(List<AbstractFunction> candidates, Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues, boolean mustSucceed) {
		AbstractFunction failed = null;
		Failure failure = null;

		for (AbstractFunction candidate : candidates) {
			if ((candidate.hasVarArgs() && argValues.length >= candidate.getArity() - 1)
					|| candidate.getArity() == argValues.length
					|| candidate.hasKeywordArgs()) {
				try {
					return candidate.call(argTypes, argValues, keyArgValues);
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
			throw new UnguardedFail(failed.ast, failure);
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
	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E {
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
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return that.equalToOverloadedFunction(this);
	}

	@Override
	public Result<IBool> equalToOverloadedFunction(
			OverloadedFunction that) {
		return ResultFactory.bool(primaryCandidates.equals(that.primaryCandidates) && defaultCandidates.equals(that.defaultCandidates), ctx);
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
	
	@Override
	public boolean isAnnotatable() {
		return false;
	}

	@Override
	public IAnnotatable<? extends IValue> asAnnotatable() {
		throw new IllegalOperationException(
				"Cannot be viewed as annotatable.", getType());
	}
	
	 @Override
   public boolean mayHaveKeywordParameters() {
     return false;
   }
   
   @Override
   public IWithKeywordParameters<? extends IValue> asWithKeywordParameters() {
     throw new IllegalOperationException(
         "Cannot be viewed as with keyword parameters", getType());
   }
}
