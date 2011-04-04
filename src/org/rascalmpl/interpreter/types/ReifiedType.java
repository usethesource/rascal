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

import java.util.Map;

import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

/**
 * A reified type is the type of a value that represents a type. It is parametrized by the type
 * it represents in order to allow type parameters to be instantiated.
 * 
 * For example, the '#int' expression produces the 'int()' value which is of type 'type[int]'
 * 
 * ReifiedType should mimick the behavior of AbstractDataType *exactly*, such that
 * pattern matching and such on reified types can be reused. Therefore this class extends Type
 * directly and does not extend ExternalType.
 */
public class ReifiedType extends Type {
	private final Type arg;

	public ReifiedType(Type arg) {
		this.arg = arg;
	}
	
	@Override
	public boolean isAbstractDataType() {
		return true;
	}
	
	@Override
	public boolean isNodeType() {
		return true;
	}
	
	@Override
	public String getName() {
		return "type";
	}
	
	@Override
	public Type getTypeParameters() {
		return TypeFactory.getInstance().tupleType(arg);
	}
	
	@Override
	public boolean isSubtypeOf(Type other) {
		if (other.isAbstractDataType()) {
			if (other instanceof ReifiedType) {
				// used to be: return true;
				return arg.isSubtypeOf(((ReifiedType) other).arg);
			}
		}
		else if (other.isNodeType()) {
			return true;
		}
		return super.isSubtypeOf(other);
	}
	
	@Override
	public Type lub(Type other) {
		if (other == this) {
			return this;
		}
		
		if (other.isAbstractDataType()) {
			if (other instanceof ReifiedType) {
				return RascalTypeFactory.getInstance().reifiedType(arg.lub(((ReifiedType) other).arg));
			}
		}
		
		return super.lub(other);
	}
	
	@Override
	public void match(Type matched, Map<Type, Type> bindings)
			throws FactTypeUseException {
		super.match(matched, bindings);
		arg.match(((ReifiedType) matched).arg, bindings);
	}
	
	@Override
	public Type instantiate(Map<Type, Type> bindings) {
		return RascalTypeFactory.getInstance().reifiedType(arg.instantiate(bindings));
	}
	
	@Override
	public String toString() {
		return "type[" + arg.toString() + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == getClass()) {
			return arg.equals(((ReifiedType) obj).arg);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return arg.hashCode();
	}

	@Override
	public <T> T accept(ITypeVisitor<T> visitor) {
		return visitor.visitAbstractData(this);
	}
}
