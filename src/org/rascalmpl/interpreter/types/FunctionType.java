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
package org.rascalmpl.interpreter.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.ExternalType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

/**
 * Function types are an extension of the pdb's type system, especially tailored to Rascal's functions 
 */
public class FunctionType extends ExternalType {
	private final Type returnType;
	private final Type argumentTypes;
	
	/*package*/ FunctionType(Type returnType, Type argumentTypes) {
		this.argumentTypes = argumentTypes.isTupleType() ? argumentTypes : TypeFactory.getInstance().tupleType(argumentTypes);
		this.returnType = returnType;
	}
	
	public Type getReturnType() {
		return returnType;
	}

	public Type getArgumentTypes() {
		return argumentTypes;
	}
	
	@Override
	public int getArity() {
		return argumentTypes.getArity();
	}
	
	@Override
	public boolean isSubtypeOf(Type other) {
		// Rascal functions are co-variant in the return type position and
		// contra-variant in the argument positions, such that a sub-function
		// can safely simulate a super function.
		
		if (other == this) {
			return true;
		}

		if (other instanceof FunctionType) {
			FunctionType otherType = (FunctionType) other;

			if (getReturnType().isSubtypeOf(otherType.getReturnType())) {
				if (otherType.getArgumentTypes().isSubtypeOf(getArgumentTypes())) {
					return true;
				}

				// type parameterized functions are never sub-types before instantiation
				// because the argument types are co-variant. This would be weird since
				// instantiated functions are supposed to be substitutable for their generic
				// counter parts. So, we try to instantiate first, and then check again.
				Map<Type,Type> bindings = new HashMap<Type,Type>();

				if (!otherType.match(this, bindings)) {
					return false;
				}
				if (bindings.size() != 0) {
					return isSubtypeOf(otherType.instantiate(bindings));
				}
			}
			
		
			return false;
		}

		// to support aliases, etc.
		return super.isSubtypeOf(other);
	}

	@Override
	public Type lub(Type other) {
		if (other == this) {
			return this;
		}
		
		if (other instanceof FunctionType) {
			FunctionType o = (FunctionType) other;
			
			if (this.returnType == o.returnType) {
				Set<FunctionType> alts = new HashSet<FunctionType>();
				alts.add(this);
				alts.add(o);
				return RascalTypeFactory.getInstance().overloadedFunctionType(alts);
			}
			
			Type newReturnType = this.returnType.lub(o.returnType);

			switch(argumentTypes.compareTo(o.argumentTypes)) {
			case -1:
				return RascalTypeFactory.getInstance().functionType(newReturnType, argumentTypes);
			case 1:
				return RascalTypeFactory.getInstance().functionType(newReturnType, o.argumentTypes);
			case 0:
				return TypeFactory.getInstance().valueType();
			}
		}
		
		if (other instanceof OverloadedFunctionType) {
			return other.lub(this);
		}
		
		return super.lub(other);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(returnType);
		sb.append(' ');
		sb.append('(');
		int i = 0;
		for (Type arg : argumentTypes) {
			if (i++ > 0) {
				sb.append(", ");
			}
			sb.append(arg.toString());
		}
		sb.append(')');
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return 19 + 19 * returnType.hashCode() + 23 * argumentTypes.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof FunctionType) {
			FunctionType other = (FunctionType) o;
			return returnType == other.returnType && argumentTypes == other.argumentTypes;
		}
		return false;
	}
	
	@Override
	public Type instantiate(Map<Type, Type> bindings) {
		return RascalTypeFactory.getInstance().functionType(returnType.instantiate(bindings), argumentTypes.instantiate(bindings));
	}
	
	@Override
	public boolean match(Type matched, Map<Type, Type> bindings)
			throws FactTypeUseException {
//		super.match(matched, bindings); match calls isSubTypeOf which calls match, watch out for infinite recursion
		if (matched.isVoidType()) {
			return returnType.match(matched, bindings);
		} else {
			// Fix for cases where we have aliases to function types, aliases to aliases to function types, etc
			while (matched.isAliasType()) {
				matched = matched.getAliased();
			}
	
			if (matched instanceof OverloadedFunctionType) {
				OverloadedFunctionType of = (OverloadedFunctionType) matched;
				// at least one needs to match (also at most one can match)
				
				for (Type f : of.getAlternatives()) {
					if (this.match(f, bindings)) {
						return true;
					}
				}
				
				return false;
			}
			else if (matched instanceof FunctionType) {
				return argumentTypes.match(((FunctionType) matched).getArgumentTypes(), bindings)
						&& returnType.match(((FunctionType) matched).getReturnType(), bindings);
			}
			else {
				return false;
			}
		}
	}
	
	@Override
	public Type compose(Type right) {
		if(right.isVoidType())
			return right;
		Set<FunctionType> newAlternatives = new HashSet<FunctionType>();
		if(right instanceof FunctionType) {
			if(TypeFactory.getInstance().tupleType(((FunctionType) right).returnType).isSubtypeOf(this.argumentTypes)) {
				return RascalTypeFactory.getInstance().functionType(this.returnType, ((FunctionType) right).getArgumentTypes());
			}
		} else if(right instanceof OverloadedFunctionType) {
			for(FunctionType ftype : ((OverloadedFunctionType) right).getAlternatives()) {
				if(TypeFactory.getInstance().tupleType(ftype.getReturnType()).isSubtypeOf(this.argumentTypes)) {
					newAlternatives.add((FunctionType) RascalTypeFactory.getInstance().functionType(this.returnType, ftype.getArgumentTypes()));
				}
			}
		} else {
			throw new IllegalOperationException("compose", this, right);
		}
		if(!newAlternatives.isEmpty()) 
			return RascalTypeFactory.getInstance().overloadedFunctionType(newAlternatives);
		return TypeFactory.getInstance().voidType();
	}
}
