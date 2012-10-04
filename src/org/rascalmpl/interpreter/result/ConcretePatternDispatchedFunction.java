/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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

import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class ConcretePatternDispatchedFunction extends AbstractFunction {
	private final Map<IConstructor, List<AbstractFunction>> alternatives;
	private final Type type;
	private final int arity;
	private final boolean isStatic;
	private final String name;

	public ConcretePatternDispatchedFunction(IEvaluator<Result<IValue>> eval, String name, Type type, Map<IConstructor, List<AbstractFunction>> alternatives) {
		super(null, eval, (FunctionType) RascalTypeFactory.getInstance().functionType(TypeFactory.getInstance().voidType(), TypeFactory.getInstance().voidType()), checkVarArgs(alternatives), null); // ?? I don't know if this will work..
		this.type = type;
		this.alternatives = alternatives;
		this.arity = minArity(alternatives);
		this.isStatic = checkStatic(alternatives);
		this.name = name;
	}
	
	public Map<IConstructor,List<AbstractFunction>> getMap() {
		return alternatives;
	}
	
	@Override
	public boolean isPublic() {
		throw new UnsupportedOperationException();
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
	public Type getType() {
		return type;
	}
	
	/* 
	 * The super getFunctionType() uses unsafe downcast in its body: (FunctionType) getType();
	 * @see org.rascalmpl.interpreter.result.AbstractFunction#getFunctionType()
	 */
	@Override 
	public FunctionType getFunctionType() {
		return (FunctionType) super.getType();
	}

	@Override
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitExternal((IExternalValue) this);
	}

	@Override
	public boolean isEqual(IValue other) {
		return equals(other);
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 == null)
			return false;
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
	public Result<IValue> call(IRascalMonitor monitor, Type[] argTypes, IValue[] argValues) {
		IConstructor label = null;
		
		if (argTypes.length == 0) {
			throw new MatchFailed();
		}
		
		if (argTypes[0].isSubtypeOf(Factory.Tree)) {
			label = TreeAdapter.getProduction((IConstructor) argValues[0]);
			List<AbstractFunction> funcs = alternatives.get(label);
			
			if (funcs != null) {
				for (AbstractFunction candidate : funcs) {
					if ((candidate.hasVarArgs() && argValues.length >= candidate.getArity() - 1)
							|| candidate.getArity() == argValues.length) {
						try {
							return candidate.call(argTypes, argValues);
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
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		return call(null, argTypes, argValues);
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