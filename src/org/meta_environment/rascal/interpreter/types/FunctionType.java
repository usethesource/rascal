package org.meta_environment.rascal.interpreter.types;

import java.util.Map;

import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;

/**
 * Function types are just aliases for map types.
 * 
 * TODO: this type is not used yet, but it should replace the encoding of closure types as a certain kind constructors,
 * this will allow us to define operators on functions using the result hierarchy.
 */
public class FunctionType extends Type {
	private final Type fAliased;
	
	public FunctionType(Type returnType, Type argumentTypes) {
		argumentTypes = argumentTypes.isTupleType() ? argumentTypes : TypeFactory.getInstance().tupleType(argumentTypes);
		this.fAliased = TypeFactory.getInstance().mapType(argumentTypes, returnType);
	}
	
	@Override
	public boolean isAliasType() {
		return true;
	}
	
	@Override
	public String getName() {
		return "";
	}
	
	@Override
	public Type getAliased() {
		return fAliased;
	}
	
	/**
	 * @return the first super type of this type that is not a AliasType.
	 */
	@Override
	public Type getHiddenType() {
		return fAliased;
	}

	@Override
	public boolean isSubtypeOf(Type other) {
		if (other == this) {
			return true;
		}
		else if (other.isAliasType() && other.getName().equals(getName())) {
			return fAliased.isSubtypeOf(other);
		}
		else {
			return fAliased.isSubtypeOf(other);
		}
	}

	@Override
	public Type lub(Type other) {
		if (other == this) {
			return this;
		}
		
		if (other instanceof FunctionType) {
			Type lub = fAliased.lub(other);
			
			if (lub.isMapType()) {
				return new FunctionType(lub.getValueType(), lub.getKeyType());
			}
			
			return lub;
		}
		
		if (other.isAliasType() && other.getHiddenType() instanceof FunctionType) {
			return lub(other.getHiddenType());
		}
		
		if (other.isAliasType() && other.getName().equals(getName())) {
			Type aliased = fAliased.lub(other.getAliased());
			return TypeFactory.getInstance().aliasTypeFromTuple(new TypeStore(), getName(), aliased, TypeFactory.getInstance().voidType());
		}
		else {
			return fAliased.lub(other);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(fAliased.getValueType());
		sb.append(' ');
		sb.append('(');
		int i = 0;
		for (Type arg : fAliased.getKeyType()) {
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
		return fAliased.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof FunctionType) {
			FunctionType other = (FunctionType) o;
			return fAliased.equals(other.fAliased);
		}
		return false;
	}
	
	@Override
	public <T> T accept(ITypeVisitor<T> visitor) {
		return visitor.visitAlias(this);
	}
	
	@Override
	public Type getKeyType() {
		return fAliased.getKeyType();
	}
	
	@Override
	public Type getValueType() {
		return fAliased.getValueType();
	}

	@Override
	public boolean comparable(Type other) {
		return fAliased.comparable(other);
	}

	@Override
	public Type compose(Type other) {
		return fAliased.compose(other);
	}

	@Override
	public Type instantiate(TypeStore store, Map<Type, Type> bindings) {
		return new FunctionType(fAliased.getValueType().instantiate(store, bindings), fAliased.getKeyType().instantiate(store, bindings));
	}
	
	@Override
	public void match(Type matched, Map<Type, Type> bindings)
			throws FactTypeUseException {
		super.match(matched, bindings);
		fAliased.match(matched, bindings);
	}

	@Override
	public boolean isMapType() {
		return true;
	}
}
