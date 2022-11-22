/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *   * Anya Helene Bagge - anya@ii.uib.no
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.visitors.IValueVisitor;

public class ConcretePatternDispatchedFunction extends AbstractFunction {
	private final Map<IConstructor, List<AbstractFunction>> alternatives;
	private final int arity;
	private final boolean isStatic;
	private final String name;
	private final int index;

	public ConcretePatternDispatchedFunction(IEvaluator<Result<IValue>> eval, int index,  String name, Map<IConstructor, List<AbstractFunction>> alternatives) {
		super(null
				, eval
				, alternatives.values().stream().flatMap(l -> l.stream()).map(f -> f.getStaticType()).reduce(TF.voidType(), (it, n) -> it.lub(n))
				, alternatives.values().stream().flatMap(l -> l.stream()).map(f -> f.getType()).reduce(TF.voidType(), (it, n) -> it.lub(n))
				, Collections.<KeywordFormal>emptyList()
				, checkVarArgs(alternatives)
				, null); // ?? I don't know if this will work..
		this.index = index;
		this.alternatives = alternatives;
		this.arity = minArity(alternatives);
		this.isStatic = checkStatic(alternatives);
		this.name = name;
	}
	
	@Override
	public int getIndexedArgumentPosition() {
		return index;
	}
	
	@Override
	public ConcretePatternDispatchedFunction cloneInto(Environment env) {
		Map<IConstructor, List<AbstractFunction>> newAlts = new HashMap<>();
		for (IConstructor name: alternatives.keySet()) {
			List<AbstractFunction> alts = new ArrayList<>();
			for (AbstractFunction alt: newAlts.get(name)) {
				alts.add((AbstractFunction) alt.cloneInto(env));
			}
			newAlts.put(name, alts);
		}
		return new ConcretePatternDispatchedFunction(getEval(), index, name, newAlts);
	}
	
	public Map<IConstructor,List<AbstractFunction>> getMap() {
		return alternatives;
	}
	
	@Override
	public boolean isConcretePatternDispatched() {
		return true;
	}

	/**
	 * We check if the different alternatives for this function have different arities here as well.
	 */
	private static boolean checkVarArgs(Map<IConstructor, List<AbstractFunction>> alts) {
		int arity = -1;
		for (List<AbstractFunction> l : alts.values()) {
			for (AbstractFunction f : l) {
				if (arity != -1) {
					if (arity != f.getArity()) { 
						return true;
					}
				}
				else {
					arity = f.getArity();
				}
				
				if (f.hasVarArgs()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static int minArity(Map<IConstructor, List<AbstractFunction>> alts) {
		int min = Integer.MAX_VALUE;
		for (List<AbstractFunction> l : alts.values()) {
			for (AbstractFunction f : l) {
				if (f.getArity() < min) {
					min = f.getArity();
				}
			}
		}
		return min;
	}

	@Override
	public Type getStaticType() {
		return type;
	}

	@Override 
	public Type getFunctionType() {
		return super.getStaticType();
	}

	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E {
		return v.visitExternal((IExternalValue) this);
	}

	@Override
	public boolean equals(Object arg0) {
		if(arg0 == null) {
			return false;
		}
		
		if (arg0.getClass() == getClass()) {
			ConcretePatternDispatchedFunction other = (ConcretePatternDispatchedFunction) arg0;
			return other.alternatives.equals(alternatives);
		}
		return false;
	}

	@Override
	/**
	 * In this case we produce the minimum arity of the overloaded alternatives
	 */
	public int getArity() {
		return arity;
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) throws MatchFailed {
	  return call(null, argTypes, argValues, keyArgValues);
	}
	
	@Override
  public Result<IValue> call(IRascalMonitor monitor, Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) {
    IConstructor label = null;
    
    if (argTypes.length < index) {
      throw new MatchFailed();
    }
    
    if (argTypes[0].isSubtypeOf(RascalValueFactory.Tree)) {
      if (!TreeAdapter.isAppl((ITree) argValues[index])) {
        throw new MatchFailed();
      }
      label = TreeAdapter.getProduction((ITree) argValues[index]);
      List<AbstractFunction> funcs = alternatives.get(label);
      
      if (funcs != null) {
        for (AbstractFunction candidate : funcs) {
          if ((candidate.hasVarArgs() && argValues.length >= candidate.getArity() - 1)
              || candidate.getArity() == argValues.length) {
            try {
              return candidate.call(argTypes, argValues, keyArgValues);
            }
            catch (MatchFailed m) {
              // could happen if pattern dispatched
            }
            catch (Failure e) {
              // could happen if function body throws fail
            }
          }
        }

        throw new MatchFailed();
      }
    }
    
    throw new MatchFailed();
  }

	@Override
	public boolean isStatic() {
		return isStatic;
	}
	
	private static boolean checkStatic(Map<IConstructor, List<AbstractFunction>> m) {
		for(List<AbstractFunction> l : m.values()) {
			for(AbstractFunction f : l)
				if(!f.isStatic())
					return false;
		}
		return true;
	}

	@Override
	public boolean isDefault() {
		return false;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (List<AbstractFunction> l : alternatives.values()) {
			for(AbstractFunction f : l)
				b.append(f.toString() + " (concrete pattern); ");
				b.append(' ');
		}
		return b.toString();
	}
	
}