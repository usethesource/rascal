/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.types;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public class OverloadedFunctionType extends RascalType {
	private final Set<FunctionType> alternatives;
	private final Type returnType;

	/*package*/ OverloadedFunctionType(Set<FunctionType> alternatives) {
		this.alternatives = alternatives;
		this.returnType = alternatives.iterator().next().getReturnType();
	}
	
	public int size() {
		return alternatives.size();
	}
	
	public Type getReturnType() {
		return returnType;
	}
	
	@Override
	public <T, E extends Throwable> T accept(IRascalTypeVisitor<T, E> visitor) throws E {
	  return visitor.visitOverloadedFunction(this);
	}
	
	@Override
	protected boolean isSupertypeOf(RascalType type) {
	  return type.isSubtypeOfOverloadedFunction(this);
	}
	
	@Override
	protected Type lub(RascalType type) {
	  return type.lubWithOverloadedFunction(this);
	}
	
	public Set<FunctionType> getAlternatives() {
		return Collections.unmodifiableSet(alternatives);
	}
	
	@Override
	protected boolean isSubtypeOfOverloadedFunction(RascalType type) {
	  OverloadedFunctionType of = (OverloadedFunctionType) type;

	  // if this has at least one alternative that is a sub-type of the other, 
	  // then yes, this function can act as the other and should be a sub-type
	  
	  // TODO: this is broken because of defaults. We should distinguish!
	  for (FunctionType a : of.alternatives) {
	    if (this.isSubtypeOf(a)) {
	      return true;
	    }
	  }

	  return false;
	}
	
	@Override
	protected boolean isSubtypeOfFunction(RascalType type) {
	// TODO: this is broken because of defaults. We should distinguish!
	  
	  for (FunctionType a : alternatives) {
	    if (a.isSubtypeOf(type)) {
	      return true;
	    }
	  }
	  return false;
	}
	
	@Override
	protected Type lubWithOverloadedFunction(RascalType type) {
	  OverloadedFunctionType of = (OverloadedFunctionType) type;

	  if (of.getReturnType() != getReturnType()) {
	    return TypeFactory.getInstance().valueType();
	  }

	  Set<FunctionType> newAlternatives = new HashSet<FunctionType>();
	  newAlternatives.addAll(alternatives);
	  newAlternatives.addAll(of.alternatives);
	  return RascalTypeFactory.getInstance().overloadedFunctionType(newAlternatives);
	}
		
	@Override
	protected Type lubWithFunction(RascalType type) {
	  FunctionType f = (FunctionType) type;

	  if (getReturnType() != f.getReturnType()) {
	    return TypeFactory.getInstance().valueType();
	  }

	  Set<FunctionType> newAlternatives = new HashSet<FunctionType>();
	  newAlternatives.addAll(alternatives);
	  newAlternatives.add(f);
	  return RascalTypeFactory.getInstance().overloadedFunctionType(newAlternatives);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if (obj.getClass().equals(getClass())) {
			OverloadedFunctionType f = (OverloadedFunctionType) obj;
			return alternatives.equals(f.alternatives);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		// TODO: better hashCode?
		return alternatives.hashCode();
	}
	
	@Override
	public String toString() {
		return getReturnType() + " (...)";
	}
	
	@Override
	public Type compose(Type right) {
		if (right.isBottom()) {
			return right;
		}
		Set<FunctionType> newAlternatives = new HashSet<FunctionType>();
		if(right instanceof FunctionType) {
			for(FunctionType ftype : this.alternatives) {
				if(TypeFactory.getInstance().tupleType(((FunctionType) right).getReturnType()).isSubtypeOf(ftype.getArgumentTypes())) {
					newAlternatives.add((FunctionType) RascalTypeFactory.getInstance().functionType(ftype.getReturnType(), ((FunctionType) right).getArgumentTypes()));
				}
			}
		} else if(right instanceof OverloadedFunctionType) {
			for(FunctionType ftype : ((OverloadedFunctionType) right).getAlternatives()) {
				for(FunctionType gtype : this.alternatives) {
					if(TypeFactory.getInstance().tupleType(ftype.getReturnType()).isSubtypeOf(gtype.getArgumentTypes())) {
						newAlternatives.add((FunctionType) RascalTypeFactory.getInstance().functionType(gtype.getReturnType(), ftype.getArgumentTypes()));
					}
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
