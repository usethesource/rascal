/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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
import org.eclipse.imp.pdb.facts.exceptions.UndeclaredAnnotationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.values.uptr.Factory;

/**
 * A reified type is the type of a value that represents a type. It is parametrized by the type
 * it represents.
 * 
 * For example, the '#int' expression produces the 'type(int(),())' value which is of type 'type[int]'
 */
public class ReifiedType extends RascalType {
	private final Type arg;

	public ReifiedType(Type arg) {
		this.arg = arg;
	}
	
	@Override
	public Type asAbstractDataType() {
		return Factory.Type;
	}
	
	@Override
	public String getName() {
		return "type";
	}

	@Override
	public boolean hasField(String fieldName) {
		return fieldName.equals("symbol") || fieldName.equals("definitions"); 
	}
	
	@Override
	public boolean isParameterized() {
		return true;
	}
	
	@Override
	public boolean isOpen() {
	  return arg.isOpen();
	}
	
	@Override
	public <T, E extends Throwable> T accept(IRascalTypeVisitor<T, E> visitor) throws E {
	  return visitor.visitReified(this);
	}
	
	@Override
	protected boolean isSupertypeOf(RascalType type) {
	  return type.isSubtypeOfReified(this);
	}
	
	@Override
	protected Type lub(RascalType type) {
	  return type.lubWithReified(this);
	}
	
	@Override
	protected Type glb(RascalType type) {
		return type.glbWithReified(this);
	}
	
	@Override
	public Type getTypeParameters() {
		return TypeFactory.getInstance().tupleType(arg);
	}
	
	@Override
	protected boolean isSubtypeOfNode(Type type) {
	  return true;
	}
	
	@Override
	protected boolean isSubtypeOfReified(RascalType type) {
	  return arg.isSubtypeOf(((ReifiedType) type).arg);
	}
	
	@Override
	protected Type lubWithNode(Type type) {
	  return type;
	}
	
	@Override
	protected Type lubWithReified(RascalType type) {
	  return RascalTypeFactory.getInstance().reifiedType(arg.lub(((ReifiedType) type).arg));
	}
	
	@Override
	protected Type glbWithNode(Type type) {
	  return this;
	}
	
	@Override
	protected Type glbWithReified(RascalType type) {
	  return RascalTypeFactory.getInstance().reifiedType(arg.glb(((ReifiedType) type).arg));
	}
	
	@Override
	public boolean match(Type matched, Map<Type, Type> bindings)
			throws FactTypeUseException {
		return super.match(matched, bindings)
				&& (matched instanceof ReifiedType) 
				&& arg.match(((ReifiedType) matched).arg, bindings);
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
		if(obj == null)
			return false;
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
	public boolean declaresAnnotation(TypeStore store, String label) {
		return false;
	}
	
	@Override
	public Type getAnnotationType(TypeStore store, String label) throws FactTypeUseException {
		throw new UndeclaredAnnotationException(this, label);
	}
}
