/*******************************************************************************
* Copyright (c) 2007, 2008, 2012 IBM Corporation & CWI
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation
*    Jurgen Vinju (jurgen@vinju.org)
*    Anya Helene Bagge - rational support, labeled maps and tuples
*******************************************************************************/

package org.rascalmpl.value.impl.reference;

import java.util.Objects;

import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.exceptions.UnexpectedElementTypeException;
import org.rascalmpl.value.impl.primitive.AbstractPrimitiveValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

/**
 * This is a reference implementation for an @{link IValueFactory}. It uses
 * the Java standard library to implement it in a most straightforward but
 * not necessarily very efficient manner.
 */
public class ValueFactory extends AbstractPrimitiveValueFactory {
	private static final ValueFactory sInstance = new ValueFactory();
	public static ValueFactory getInstance() {
		return sInstance;
	}

	protected ValueFactory() {
		super();
	}

	protected static void checkNull(Object... args) {
		for (Object a : args) {
			Objects.requireNonNull(a);
		}
	}

	protected static void checkNull(java.util.Map<Object, Object> args) {
		for (java.util.Map.Entry<Object, Object> entry : args.entrySet()) {
			if (entry == null || entry.getKey() == null || entry.getValue() == null) {
				throw new NullPointerException();
			}
		}
	}

	@Override
	public ISet relation(Type tupleType) {
		checkNull(tupleType);
		return relationWriter(tupleType).done();
	}
	
	@Override
	public ISet relation(IValue... tuples) {
		checkNull((Object[]) tuples);
		Type elementType = lub(tuples);
	
		if (!elementType.isFixedWidth()) {
			TypeFactory tf = TypeFactory.getInstance();
			throw new UnexpectedElementTypeException(tf.tupleType(tf.voidType()), elementType);
		}
		
		ISetWriter rw = setWriter(elementType);
		rw.insert(tuples);
		return rw.done();
	}
	
	@Override
	public ISetWriter relationWriter(Type tupleType) {
		checkNull(tupleType);
		return new SetWriter(tupleType);
	}
	
	@Override
	public ISetWriter relationWriter() {
		return new SetWriter();
	}

	@Override
	public ISet set(Type eltType){
		checkNull(eltType);
		return setWriter(eltType).done();
	}
	
	@Override
	public ISetWriter setWriter(Type eltType) {
		checkNull(eltType);
		return new SetWriter(eltType);
	}
	
	@Override
	public ISetWriter setWriter() {
		return new SetWriter();
	}

	@Override
	public ISet set(IValue... elems) throws FactTypeUseException {
		checkNull((Object[]) elems);
		Type elementType = lub(elems);
		
		ISetWriter sw = setWriter(elementType);
		sw.insert(elems);
		return sw.done();
	}

	@Override
	public IList list(Type eltType) {
		checkNull(eltType);
		return listWriter(eltType).done();
	}
	
	@Override
	public IListWriter listWriter(Type eltType) {
		checkNull(eltType);
		return new ListWriter(eltType);
	}
	
	@Override
	public IListWriter listWriter() {
		return new ListWriter();
	}

	@Override
	public IList list(IValue... rest) {
		checkNull((Object[]) rest);
		Type eltType = lub(rest);
		IListWriter lw =  listWriter(eltType);
		lw.append(rest);
		return lw.done();
	}

	private Type lub(IValue... elems) {
		checkNull((Object[]) elems);
		Type elementType = TypeFactory.getInstance().voidType();
		for (IValue elem : elems) {
			elementType = elementType.lub(elem.getType());
		}
		return elementType;
	}

	@Override
	public ITuple tuple() {
		return new Tuple(new IValue[0]);
	}

	@Override
	public ITuple tuple(IValue... args) {
		checkNull((Object[]) args);

		return new Tuple(args.clone());
	}

	@Override
	public ITuple tuple(Type type, IValue... args) {
		checkNull((Object[]) args);

		return new Tuple(type, args.clone());
	}
	
	@Override
	public INode node(String name) {
		checkNull(name);
		return new Node(name);
	}
	
	@Override
	public INode node(String name, java.util.Map<String, IValue> annotations, IValue... children) {
		checkNull(name);
		checkNull(annotations);
		checkNull((Object[]) children);
				
		return new Node(name, children).asAnnotatable().setAnnotations(annotations);
	}
	
	@Override
	public INode node(String name, IValue... children) {
		checkNull(name);
		checkNull((Object[]) children);
		return new Node(name, children);
	}
	
	@Override
	public INode node(String name,  IValue[] children, java.util.Map<String, IValue> keyArgValues)
			throws FactTypeUseException {
		checkNull(name);
		checkNull((Object[]) children);
//		checkNull(keyArgValues); // fails; are null values allowed?
		
		return new Node(name, children.clone(), keyArgValues);
	}
		
	@Override
	public IConstructor constructor(Type constructorType, IValue... children) {
		checkNull(constructorType);
		checkNull((Object[]) children);
		Type instantiatedType = inferInstantiatedTypeOfConstructor(constructorType, children);
		return new Constructor(instantiatedType, children);
	}
	
	@Override
	public IConstructor constructor(Type constructorType, java.util.Map<String,IValue> annotations, IValue... children) {
		checkNull(constructorType);
		checkNull(annotations);
		checkNull((Object[]) children);
				
		return new Constructor(constructorType, children).asAnnotatable().setAnnotations(annotations);
	}
	
	@Override
  public IConstructor constructor(Type constructorType,  IValue[] children, java.util.Map<String,IValue> kwParams) {
    checkNull(constructorType);
    checkNull(kwParams);
    checkNull((Object[]) children);
        
    return new Constructor(constructorType, children, kwParams);
  }
	
	@Override
	public IConstructor constructor(Type constructorType) {
		checkNull(constructorType);
		Type instantiatedType = inferInstantiatedTypeOfConstructor(constructorType, new IValue[0]);		
		return new Constructor(instantiatedType);
	}

	@Override
	public IMap map(Type keyType, Type valueType) {
		checkNull(keyType);
		checkNull(valueType);
		return mapWriter(keyType, valueType).done();
	}
	
	@Override
	public IMap map(Type mapType) {
		checkNull(mapType);
		return mapWriter(mapType).done();
	}
	@Override
	public IMapWriter mapWriter(Type keyType, Type valueType) {
		checkNull(keyType);
		checkNull(valueType);
		return new MapWriter(TypeFactory.getInstance().mapType(keyType, valueType));
	}
	
	@Override
	public IMapWriter mapWriter(Type mapType) {
		checkNull(mapType);
		return new MapWriter(mapType);
	}

	@Override
	public IMapWriter mapWriter() {
		return new MapWriter();
	}

	@Override
	public IListWriter listRelationWriter(Type tupleType) {
		checkNull(tupleType);
		return new ListWriter(tupleType);
	}

	@Override
	public IListWriter listRelationWriter() {
		return new ListWriter();
	}

	@Override
	public IList listRelation(Type tupleType) {
		checkNull(tupleType);
		return listWriter(tupleType).done();
	}

	@Override
	public IList listRelation(IValue... tuples) {
		checkNull((Object[]) tuples);
		Type elementType = lub(tuples);
	
		if (!elementType.isFixedWidth()) {
			TypeFactory tf = TypeFactory.getInstance();
			throw new UnexpectedElementTypeException(tf.tupleType(tf.voidType()), elementType);
		}
		
		IListWriter rw = listRelationWriter(elementType);
		rw.append(tuples);
		return rw.done();
	}
	
	@Override
	public String toString() {
		return "VF_PDB_REFERENCE";
	}
	
}
