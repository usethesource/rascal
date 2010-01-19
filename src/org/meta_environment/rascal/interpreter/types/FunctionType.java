package org.meta_environment.rascal.interpreter.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.exceptions.FactMatchException;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.ExternalType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;

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
				try {
					Map<Type,Type> bindings = new HashMap<Type,Type>();
					otherType.match(this, bindings);
					if (bindings.size() != 0) {
						return isSubtypeOf(otherType.instantiate(new TypeStore(), bindings));
					}
				}
				catch (FactMatchException e) {
					return false;
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
	public Type instantiate(TypeStore store, Map<Type, Type> bindings) {
		return RascalTypeFactory.getInstance().functionType(returnType.instantiate(store, bindings), argumentTypes.instantiate(store, bindings));
	}
	
	@Override
	public void match(Type matched, Map<Type, Type> bindings)
			throws FactTypeUseException {
//		super.match(matched, bindings); match calls isSubTypeOf which calls match, watch out for infinite recursion
		if (matched.isVoidType()) {
			returnType.match(matched, bindings);
		}
		else if (matched instanceof OverloadedFunctionType) {
			OverloadedFunctionType of = (OverloadedFunctionType) matched;
			// at least one needs to match (also at most one can match)
			FactTypeUseException ex = null;
			
			for (Type f : of.getAlternatives()) {
				try {
					this.match(f, bindings);
					return;
				}
				catch (FactMatchException e) {
					ex = e;
				}
			}
			
			if (ex != null) {
				throw ex;
			}
		}
		else if (matched instanceof FunctionType) {
			argumentTypes.match(((FunctionType) matched).getArgumentTypes(), bindings);
			returnType.match(((FunctionType) matched).getReturnType(), bindings);
		}
		else {
			throw new FactMatchException(matched, this);
		}
	}
}
