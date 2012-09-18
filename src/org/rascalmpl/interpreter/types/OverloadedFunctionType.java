/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.types;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.imp.pdb.facts.type.ExternalType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public class OverloadedFunctionType extends ExternalType {
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
	
	public Set<FunctionType> getAlternatives() {
		return Collections.unmodifiableSet(alternatives);
	}
	
	@Override
	public boolean isSubtypeOf(Type other) {
		if (other == this) {
			return true;
		}
		
		if (other instanceof OverloadedFunctionType) {
			OverloadedFunctionType of = (OverloadedFunctionType) other;

			// if this has at least one alternative that is a sub-type of the other, 
			// then yes, this function can act as the other and should be a sub-type
			for (FunctionType a : of.alternatives) {
				if (this.isSubtypeOf(a)) {
					return true;
				}
			}
			
			return false;
		}
		
		if (other instanceof FunctionType) {
			for (FunctionType a : alternatives) {
				if (a.isSubtypeOf(other)) {
					return true;
				}
			}
			return false;
		}
		
		return super.isSubtypeOf(other);
	}
	
	@Override
	public Type lub(Type other) {
		if (other == this) {
			return this;
		}
		
		if (other instanceof OverloadedFunctionType) {
			OverloadedFunctionType of = (OverloadedFunctionType) other;
			
			if (of.getReturnType() != getReturnType()) {
				return TypeFactory.getInstance().valueType();
			}
			
			Set<FunctionType> newAlternatives = new HashSet<FunctionType>();
			newAlternatives.addAll(alternatives);
			newAlternatives.addAll(of.alternatives);
			return RascalTypeFactory.getInstance().overloadedFunctionType(newAlternatives);
		}
		
		if (other instanceof FunctionType) {
			FunctionType f = (FunctionType) other;
			
			if (getReturnType() != f.getReturnType()) {
				return TypeFactory.getInstance().valueType();
			}
			
			Set<FunctionType> newAlternatives = new HashSet<FunctionType>();
			newAlternatives.addAll(alternatives);
			newAlternatives.add(f);
			return RascalTypeFactory.getInstance().overloadedFunctionType(newAlternatives);

		}
		
		return super.lub(other);
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
	
}
