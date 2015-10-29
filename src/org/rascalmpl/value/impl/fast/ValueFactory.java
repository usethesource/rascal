/*******************************************************************************
* Copyright (c) 2009, 2012-2013 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Jurgen Vinju - interface and implementation
*    Arnold Lankamp - implementation
*    Anya Helene Bagge - rational support, labeled maps and tuples
*    Davy Landman - added PI & E constants
*    Michael Steindorfer - extracted factory for numeric data   
*******************************************************************************/
package org.rascalmpl.value.impl.fast;

import java.util.Map;

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
 * Implementation of IValueFactory.
 */
public class ValueFactory extends AbstractPrimitiveValueFactory {
	private final static TypeFactory tf = TypeFactory.getInstance();
	
	private final static Type EMPTY_TUPLE_TYPE = TypeFactory.getInstance().tupleEmpty();
	
	protected ValueFactory() {
		super();
	}

	private static class InstanceKeeper{
		public final static ValueFactory instance = new ValueFactory();
	}
	
	public static ValueFactory getInstance(){
		return InstanceKeeper.instance;
	}
		
	@Deprecated
	public IListWriter listWriter(Type elementType){
		return new ListWriter(elementType);
	}
	
	public IListWriter listWriter(){
		return new ListWriter();
	}
	
	@Deprecated
	public IMapWriter mapWriter(Type keyType, Type valueType){
		return new MapWriter(TypeFactory.getInstance().mapType(keyType, valueType));
	}

	public IMapWriter mapWriter(Type mapType){
		return new MapWriter(mapType);
	}
	
	public IMapWriter mapWriter(){
		return new MapWriter();
	}
	
	public ISetWriter setWriter(Type elementType){
		return new SetWriter(elementType);
	}
	
	public ISetWriter setWriter(){
		return new SetWriter();
	}
	
	public ISetWriter relationWriter(Type tupleType){
		return new SetWriter(tupleType);
	}
	
	public ISetWriter relationWriter(){
		return new SetWriter();
	}
	
	public IListWriter listRelationWriter(Type tupleType) {
		return new ListWriter(tupleType);
	}

	public IListWriter listRelationWriter() {
		return new ListWriter();
	}
	
	public IList list(Type elementType){
		return listWriter(elementType).done();
	}
	
	public IList list(IValue... elements){
		IListWriter listWriter = listWriter(lub(elements));
		listWriter.append(elements);
		
		return listWriter.done();
	}
	
	public IMap map(Type mapType){
		return mapWriter(mapType).done();
	}

	public IMap map(Type keyType, Type valueType){
		return mapWriter(keyType, valueType).done();
	}
	
	public ISet set(Type elementType){
		return setWriter(TypeFactory.getInstance().voidType()).done();
	}
	
	public ISet set(IValue... elements){
		Type elementType = lub(elements);
		
		ISetWriter setWriter = setWriter(elementType);
		setWriter.insert(elements);
		return setWriter.done();
	}
	
	public ISet relation(Type tupleType){
		return relationWriter(tupleType).done();
	}
	
	public ISet relation(IValue... elements) {
		Type elementType = lub(elements);
		
		if (!elementType.isFixedWidth()) throw new UnexpectedElementTypeException(tf.tupleType(tf.voidType()), elementType);
		
		ISetWriter relationWriter = relationWriter(elementType);
		relationWriter.insert(elements);
		return relationWriter.done();
	}
	
	public IList listRelation(Type tupleType) {
		return listRelationWriter(tupleType).done();
	}

	public IList listRelation(IValue... elements) {
		Type elementType = lub(elements);
		
		if (!elementType.isFixedWidth()) throw new UnexpectedElementTypeException(tf.tupleType(tf.voidType()), elementType);
		
		IListWriter listRelationWriter = listRelationWriter(elementType);
		listRelationWriter.append(elements);
		return listRelationWriter.done();
	}
	
	public INode node(String name) {
		return Node.newNode(name, new IValue[0]);
	}

	public INode node(String name, Map<String, IValue> annos, IValue... children) {
		return Node.newNode(name, children.clone()).asAnnotatable().setAnnotations(annos);
	}

	public INode node(String name, IValue... children) {
		return Node.newNode(name, children.clone());
	}
	
	@Override
	public INode node(String name, IValue[] children, Map<String, IValue> keyArgValues)
			throws FactTypeUseException {
		return Node.newNode(name, children.clone(), keyArgValues);
	}
	
	@Override
	public IConstructor constructor(Type constructorType) {
		return Constructor.newConstructor(constructorType, new IValue[0]);
	}
	
	@Override
	public IConstructor constructor(Type constructorType, IValue... children){
		return Constructor.newConstructor(constructorType, children.clone());
	}
	
	@Override
  public IConstructor constructor(Type constructorType, IValue[] children, Map<String,IValue> kwParams){
    return Constructor.newConstructor(constructorType, children.clone(), kwParams);
  }
	
	@Override
	public IConstructor constructor(Type constructorType,
			Map<String, IValue> annotations, IValue... children)
			throws FactTypeUseException {
		return Constructor.newConstructor(constructorType, children.clone()).asAnnotatable().setAnnotations(annotations);
	}
	
	public ITuple tuple() {
		return Tuple.newTuple(EMPTY_TUPLE_TYPE, new IValue[0]);
	}

	public ITuple tuple(IValue... args) {
		return Tuple.newTuple(args.clone());
	}

	public ITuple tuple(Type type, IValue... args) {
		return Tuple.newTuple(type, args.clone());
	}

	private static Type lub(IValue... elements) {
		Type elementType = TypeFactory.getInstance().voidType();

		for (int i = elements.length - 1; i >= 0; i--) {
			elementType = elementType.lub(elements[i].getType());
		}

		return elementType;
	}

	@Override
	public String toString() {
		return "VF_PDB_FAST";
	}
	
}
