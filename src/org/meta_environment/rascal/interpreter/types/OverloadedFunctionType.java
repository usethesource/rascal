package org.meta_environment.rascal.interpreter.types;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.imp.pdb.facts.type.ExternalType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public class OverloadedFunctionType extends ExternalType {
	private final Set<FunctionType> alternatives;
	private final Type returnType;

	public OverloadedFunctionType(Set<FunctionType> alternatives) {
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

			// this has more functions, so it can't be a sub-type
			if (size() > of.size()) {
				return false;
			}
			
			// if this has less functions, and all of them are sub-types, then yes
			for (FunctionType a : alternatives) {
				if (!a.isSubtypeOf(other)) {
					return false;
				}
			}
			
			return true;
		}
		
		if (other instanceof FunctionType) {
			for (FunctionType a : alternatives) {
				if (!a.isSubtypeOf(other)) {
					return false;
				}
			}
			return true;
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
