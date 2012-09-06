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
*******************************************************************************/
package org.rascalmpl.values;

import java.net.URI;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;

/**
 * This is a value factory wrapper class, which is intended to take care of 'local' sharing. <br />
 * <br />
 * All values that are constructed using this factory are shared; i.e. when their 'equals(Object)'
 * method returns true, they are guaranteed to be pointer equal. <br />
 * <br />
 * Note however that this factory does not take care of garbage collection. All created values will
 * remain reachable as long as this factory exists.
 * 
 * @author Arnold Lankamp
 */
public class LocalSharingValueFactory implements IValueFactory{
	private final IValueFactory valueFactory;
	
	private final IBool trueValue;
	private final IBool falseValue;
	private final ValueCache<IInteger> cachedIntegers;
	private final ValueCache<IReal> cachedReals;
	private final ValueCache<IRational> cachedRationals;
	private final ValueCache<IString> cachedStrings;
	private final ValueCache<ISourceLocation> cachedSourceLocations;
	private final ValueCache<ITuple> cachedTuples;
	private final ValueCache<INode> cachedNodes;
	private final ValueCache<IConstructor> cachedConstructors;
	private final ValueCache<IList> cachedLists;
	private final ValueCache<ISet> cachedSets;
	private final ValueCache<IRelation> cachedRelations;
	private final ValueCache<IMap> cachedMaps;
	private final ValueCache<IDateTime> cachedDateTimes;
	
	public LocalSharingValueFactory(IValueFactory valueFactory){
		super();
		
		this.valueFactory = valueFactory;
		
		trueValue = valueFactory.bool(true);
		falseValue = valueFactory.bool(false);

		cachedIntegers = new ValueCache<IInteger>();
		cachedRationals = new ValueCache<IRational>();
		cachedReals = new ValueCache<IReal>();
		cachedStrings = new ValueCache<IString>();
		cachedSourceLocations = new ValueCache<ISourceLocation>();
		cachedTuples = new ValueCache<ITuple>();
		cachedNodes = new ValueCache<INode>();
		cachedConstructors = new ValueCache<IConstructor>();
		cachedLists = new ValueCache<IList>();
		cachedSets = new ValueCache<ISet>();
		cachedRelations = new ValueCache<IRelation>();
		cachedMaps = new ValueCache<IMap>();
		cachedDateTimes = new ValueCache<IDateTime>();
	}

	public IBool bool(boolean value){
		return value ? trueValue : falseValue;
	}

	public IInteger integer(byte[] a){
		return cachedIntegers.cache(valueFactory.integer(a));
	}

	public IInteger integer(int i){
		return cachedIntegers.cache(valueFactory.integer(i));
	}

	public IInteger integer(long i){
		return cachedIntegers.cache(valueFactory.integer(i));
	}

	public IInteger integer(String i) throws NumberFormatException{
		return cachedIntegers.cache(valueFactory.integer(i));
	}

	public IRational rational(int a, int b) {
		return cachedRationals.cache(valueFactory.rational(a, b));
	}

	public IRational rational(long a, long b) {
		return cachedRationals.cache(valueFactory.rational(a, b));
	}

	public IRational rational(IInteger a, IInteger b) {
		return cachedRationals.cache(valueFactory.rational(a, b));
	}

	public IRational rational(String rat) throws NumberFormatException {
		return cachedRationals.cache(valueFactory.rational(rat));
	}

	public IReal real(double d){
		return cachedReals.cache(valueFactory.real(d));
	}

	public IReal real(String s) throws NumberFormatException{
		return cachedReals.cache(valueFactory.real(s));
	}

	public IString string(String s){
		return cachedStrings.cache(valueFactory.string(s));
	}

	public ISourceLocation sourceLocation(String path){
		return cachedSourceLocations.cache(valueFactory.sourceLocation(path));
	}

	public ISourceLocation sourceLocation(URI uri){
		return cachedSourceLocations.cache(valueFactory.sourceLocation(uri));
	}

	public ISourceLocation sourceLocation(String path, int offset, int length, int beginLine, int endLine, int beginCol, int endCol){
		return cachedSourceLocations.cache(valueFactory.sourceLocation(path, offset, length, beginLine, endLine, beginCol, endCol));
	}

	public ISourceLocation sourceLocation(URI uri, int offset, int length, int beginLine, int endLine, int beginCol, int endCol){
		return cachedSourceLocations.cache(valueFactory.sourceLocation(uri, offset, length, beginLine, endLine, beginCol, endCol));
	}
	
	public ISourceLocation sourceLocation(URI uri, int offset, int length){
		return cachedSourceLocations.cache(valueFactory.sourceLocation(uri, offset, length));
	}

	public ITuple tuple(){
		return cachedTuples.cache(valueFactory.tuple());
	}

	public ITuple tuple(IValue... args){
		return cachedTuples.cache(valueFactory.tuple(args));
	}

	public INode node(String name, IValue... children){
		return cachedNodes.cache(valueFactory.node(name, children));
	}

	public INode node(String name, Map<String,IValue> annos, IValue... children){
		return cachedNodes.cache(valueFactory.node(name, annos, children));
	}
	
	public INode node(String name){
		return cachedNodes.cache(valueFactory.node(name));
	}

	public IConstructor constructor(Type constructor, IValue... children) throws FactTypeUseException{
		return cachedConstructors.cache(valueFactory.constructor(constructor, children));
	}
	
	public IConstructor constructor(Type constructor, Map<String, IValue> annotations, IValue... children) throws FactTypeUseException {
		return cachedConstructors.cache(valueFactory.constructor(constructor, annotations, children));
	}
	
	public IConstructor constructor(Type constructor){
		return cachedConstructors.cache(valueFactory.constructor(constructor));
	}

	public IList list(IValue... elems){
		return cachedLists.cache(valueFactory.list(elems));
	}

	public IList list(Type eltType){
		return cachedLists.cache(valueFactory.list(eltType));
	}

	public IListWriter listWriter(Type eltType){
		return new ListCachingWriter(this, valueFactory.listWriter(eltType));
	}
	
	public IListWriter listWriter(){
		return new ListCachingWriter(this, valueFactory.listWriter());
	}

	public IMap map(Type key, Type value){
		return cachedMaps.cache(valueFactory.map(key, value));
	}

	public IMapWriter mapWriter(Type key, Type value){
		return new MapCachingWriter(this, valueFactory.mapWriter(key, value));
	}
	
	public IMapWriter mapWriter(){
		return new MapCachingWriter(this, valueFactory.mapWriter());
	}

	public ISet set(IValue... elems){
		return cachedSets.cache(valueFactory.set(elems));
	}

	public ISet set(Type eltType){
		return cachedSets.cache(valueFactory.set(eltType));
	}

	public ISetWriter setWriter(Type eltType){
		return new SetCachingWriter(this, valueFactory.setWriter(eltType));
	}
	
	public ISetWriter setWriter(){
		return new SetCachingWriter(this, valueFactory.setWriter());
	}

	public IRelation relation(IValue... elems){
		return cachedRelations.cache(valueFactory.relation(elems));
	}

	public IRelation relation(Type tupleType){
		return cachedRelations.cache(valueFactory.relation(tupleType));
	}

	public IRelationWriter relationWriter(Type type){
		return new RelationCachingWriter(this, valueFactory.relationWriter(type));
	}
	
	public IRelationWriter relationWriter(){
		return new RelationCachingWriter(this, valueFactory.relationWriter());
	}
	
	private static class ListCachingWriter implements IListWriter{
		private final LocalSharingValueFactory localSharingValueFactory;
		private final IListWriter listWriter;
		
		public ListCachingWriter(LocalSharingValueFactory localSharingValueFactory, IListWriter listWriter){
			super();
			
			this.localSharingValueFactory = localSharingValueFactory;
			this.listWriter = listWriter;
		}

		public IList done(){
			return localSharingValueFactory.cachedLists.cache(listWriter.done());
		}

		public void append(IValue... value) throws FactTypeUseException{
			listWriter.append(value);
		}

		public void appendAll(Iterable<? extends IValue> collection) throws FactTypeUseException{
			listWriter.appendAll(collection);
		}

		public void delete(int i){
			listWriter.delete(i);
		}

		public void delete(IValue elem){
			listWriter.delete(elem);
		}

		public void insert(IValue... value) throws FactTypeUseException{
			listWriter.insert(value);
		}

		public void insert(IValue[] elems, int start, int length) throws FactTypeUseException, IndexOutOfBoundsException{
			listWriter.insert(elems, start, length);
		}

		public void insertAll(Iterable<? extends IValue> collection) throws FactTypeUseException{
			listWriter.insertAll(collection);
		}

		public void insertAt(int index, IValue... value) throws FactTypeUseException, IndexOutOfBoundsException{
			listWriter.insertAt(index, value);
		}

		public void insertAt(int index, IValue[] elems, int start, int length) throws FactTypeUseException, IndexOutOfBoundsException{
			listWriter.insertAt(index, elems, start, length);
		}

		public void replaceAt(int index, IValue elem) throws FactTypeUseException, IndexOutOfBoundsException{
			listWriter.replaceAt(index, elem);
		}
	}
	
	private static class SetCachingWriter implements ISetWriter{
		private final LocalSharingValueFactory localSharingValueFactory;
		private final ISetWriter setWriter;
		
		public SetCachingWriter(LocalSharingValueFactory localSharingValueFactory, ISetWriter setWriter){
			super();
			
			this.localSharingValueFactory = localSharingValueFactory;
			this.setWriter = setWriter;
		}

		public ISet done(){
			return localSharingValueFactory.cachedSets.cache(setWriter.done());
		}

		public void delete(IValue v){
			setWriter.delete(v);
		}

		public void insert(IValue... v) throws FactTypeUseException{
			setWriter.insert(v);
		}

		public void insertAll(Iterable<? extends IValue> collection) throws FactTypeUseException{
			setWriter.insertAll(collection);
		}

		public int size(){
			return setWriter.size();
		}
	}
	
	private static class RelationCachingWriter implements IRelationWriter{
		private final LocalSharingValueFactory localSharingValueFactory;
		private final IRelationWriter relationWriter;
		
		public RelationCachingWriter(LocalSharingValueFactory localSharingValueFactory, IRelationWriter relationWriter){
			super();
			
			this.localSharingValueFactory = localSharingValueFactory;
			this.relationWriter = relationWriter;
		}

		public IRelation done(){
			return localSharingValueFactory.cachedRelations.cache(relationWriter.done());
		}

		public void delete(IValue v){
			relationWriter.delete(v);
		}

		public void insert(IValue... v) throws FactTypeUseException{
			relationWriter.insert(v);
		}

		public void insertAll(Iterable<? extends IValue> collection) throws FactTypeUseException{
			relationWriter.insertAll(collection);
		}

		public int size(){
			return relationWriter.size();
		}
	}
	
	private static class MapCachingWriter implements IMapWriter{
		private final LocalSharingValueFactory localSharingValueFactory;
		private final IMapWriter mapWriter;
		
		public MapCachingWriter(LocalSharingValueFactory localSharingValueFactory, IMapWriter mapWriter){
			super();
			
			this.localSharingValueFactory = localSharingValueFactory;
			this.mapWriter = mapWriter;
		}

		public IMap done(){
			return localSharingValueFactory.cachedMaps.cache(mapWriter.done());
		}

		public void put(IValue key, IValue value) throws FactTypeUseException{
			mapWriter.put(key, value);
		}

		public void putAll(IMap map) throws FactTypeUseException{
			mapWriter.putAll(map);
		}

		public void putAll(Map<IValue, IValue> map) throws FactTypeUseException{
			mapWriter.putAll(map);
		}

		public void insert(IValue... value) throws FactTypeUseException{
			mapWriter.insert(value);
		}

		public void insertAll(Iterable<? extends IValue> collection) throws FactTypeUseException{
			mapWriter.insertAll(collection);
		}
	}

	public IDateTime date(int year, int month, int day) {
		return cachedDateTimes.cache(valueFactory.date(year, month, day));
	}

	public IDateTime datetime(int year, int month, int day, int hour, int minute, int second, int millisecond) {
		return cachedDateTimes.cache(valueFactory.datetime(year, month, day, hour, minute, second, millisecond));
	}

	public IDateTime datetime(int year, int month, int day, int hour, int minute, int second, int millisecond, int hourOffset,
			int minuteOffset) {
		return cachedDateTimes.cache(valueFactory.datetime(year, month, day, hour, minute, second, millisecond, hourOffset, minuteOffset));
	}

	public IDateTime datetime(long instant) {
		return cachedDateTimes.cache(valueFactory.datetime(instant));
	}
	
	public IDateTime time(int hour, int minute, int second, int millisecond) {
		return cachedDateTimes.cache(valueFactory.time(hour, minute, second, millisecond));
	}

	public IDateTime time(int hour, int minute, int second, int millisecond, int hourOffset, int minuteOffset) {
		return cachedDateTimes.cache(valueFactory.time(hour, minute, second, millisecond, hourOffset, minuteOffset));
	}

	@Override
	public IReal pi(int precision) {
		return cachedReals.cache(valueFactory.pi(precision));
	}

	@Override
	public IReal e(int precision) {
		return cachedReals.cache(valueFactory.e(precision));
	}

	@Override
	public IString string(int[] chars) {
		return cachedStrings.cache(valueFactory.string(chars));
	}

	@Override
	public IString string(int ch) {
		return cachedStrings.cache(valueFactory.string(ch));
	}
}
