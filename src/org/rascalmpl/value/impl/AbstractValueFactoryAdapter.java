package org.rascalmpl.value.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.type.Type;

/**
 * This class provides a default way of easily reusing existing implementations of IValueFactory without having
 * to extend them again and again using inheritance. Clients extend this class and override the methods that need 
 * special handling.
 * 
 * Note: this class is intended to be sub-classed. It should not be abstract because we want the compiler to 
 * check that it provides a facade for the full IValueFactory interface.
 */
@SuppressWarnings("deprecation")
public /*abstract*/ class AbstractValueFactoryAdapter implements IValueFactory {
	protected final IValueFactory adapted;
	
	public AbstractValueFactoryAdapter(IValueFactory adapted) {
		this.adapted = adapted;
	}
	
	public IBool bool(boolean value) {
		return adapted.bool(value);
	}
	

	public IConstructor constructor(Type constructor) {
		return adapted.constructor(constructor);
	}

	public IConstructor constructor(Type constructor, IValue... children)
			throws FactTypeUseException {
		return adapted.constructor(constructor, children);
	}

	public IDateTime date(int year, int month, int day) {
		return adapted.date(year, month, day);
	}

	public IDateTime datetime(int year, int month, int day, int hour,
			int minute, int second, int millisecond) {
		return adapted.datetime(year, month, day, hour, minute, second, millisecond);
	}

	public IDateTime datetime(int year, int month, int day, int hour,
			int minute, int second, int millisecond, int hourOffset,
			int minuteOffset) {
		return adapted.datetime(year, month, day, hour, minute, second, millisecond, hourOffset, minuteOffset);
	}

	public IDateTime datetime(long instant) {
		return adapted.datetime(instant);
	}
	
	public IDateTime datetime(long instant, int timezoneHours, int timezoneMinutes) {
		return adapted.datetime(instant, timezoneHours, timezoneMinutes);
	}

	public IInteger integer(String i) throws NumberFormatException {
		return adapted.integer(i);
	}

	public IInteger integer(int i) {
		return adapted.integer(i);
	}

	public IInteger integer(long i) {
		return adapted.integer(i);
	}

	public IInteger integer(byte[] a) {
		return adapted.integer(a);
	}

	public IList list(Type eltType) {
		return adapted.list(eltType);
	}

	public IList list(IValue... elems) {
		return adapted.list(elems);
	}

	public IListWriter listWriter(Type eltType) {
		return adapted.listWriter(eltType);
	}

	public IListWriter listWriter() {
		return adapted.listWriter();
	}

	public IMap map(Type key, Type value) {
		return adapted.map(key, value);
	}

	public IMapWriter mapWriter(Type key, Type value) {
		return adapted.mapWriter(key, value);
	}

	public IMapWriter mapWriter() {
		return adapted.mapWriter();
	}

	public INode node(String name) {
		return adapted.node(name);
	}

	public INode node(String name, IValue... children) {
		return adapted.node(name, children);
	}

	public IReal real(String s) throws NumberFormatException {
		return adapted.real(s);
	}

	public IReal real(double d) {
		return adapted.real(d);
	}

	public ISet relation(Type tupleType) {
		return adapted.relation(tupleType);
	}

	public ISet relation(IValue... elems) {
		return adapted.relation(elems);
	}

	public ISetWriter relationWriter(Type type) {
		return adapted.relationWriter(type);
	}

	public ISetWriter relationWriter() {
		return adapted.relationWriter();
	}

	public ISet set(Type eltType) {
		return adapted.set(eltType);
	}

	public ISet set(IValue... elems) {
		return adapted.set(elems);
	}

	public ISetWriter setWriter(Type eltType) {
		return adapted.setWriter(eltType);
	}

	public ISetWriter setWriter() {
		return adapted.setWriter();
	}

	public ISourceLocation sourceLocation(URI uri, int offset, int length,
			int beginLine, int endLine, int beginCol, int endCol) {
		return adapted.sourceLocation(uri, offset, length, beginLine, endLine, beginCol, endCol);
	}

	public ISourceLocation sourceLocation(String path, int offset, int length,
			int beginLine, int endLine, int beginCol, int endCol) {
		return adapted.sourceLocation(path, offset, length, beginLine, endLine, beginCol, endCol);
	}

	public ISourceLocation sourceLocation(URI uri) {
		return adapted.sourceLocation(uri);
	}

	public ISourceLocation sourceLocation(String path) {
		return adapted.sourceLocation(path);
	}
	
	@Override
	public ISourceLocation sourceLocation(ISourceLocation loc, int offset, int length, int beginLine, int endLine, int beginCol, int endCol) {
		return adapted.sourceLocation(loc, offset, length, beginLine, endLine, beginCol, endCol);
	}
	
	@Override
	public ISourceLocation sourceLocation(ISourceLocation loc, int offset, int length)  {
		return adapted.sourceLocation(loc, offset, length);
	}
	
	@Override
	public ISourceLocation sourceLocation(String scheme, String authority, String path) throws URISyntaxException {
		return adapted.sourceLocation(scheme, authority, path);
	}
	
	@Override
	public ISourceLocation sourceLocation(String scheme, String authority, String path, String query, String fragment) throws URISyntaxException {
		return adapted.sourceLocation(scheme, authority, path, query, fragment);
	}

	public IString string(String s) {
		return adapted.string(s);
	}

	public IDateTime time(int hour, int minute, int second, int millisecond) {
		return adapted.time(hour, minute, second, millisecond);
	}

	public IDateTime time(int hour, int minute, int second, int millisecond,
			int hourOffset, int minuteOffset) {
		return adapted.time(hour, minute, second, millisecond, hourOffset, minuteOffset);
	}

	public ITuple tuple() {
		return adapted.tuple();
	}

	public ITuple tuple(IValue... args) {
		return adapted.tuple(args);
	}
	
	@Override
	public IRational rational(int a, int b) {
	 return adapted.rational(a,b);
	}

	@Override
	public IRational rational(long a, long b) {
	 return adapted.rational(a, b);
	}

	@Override
	public IRational rational(IInteger a, IInteger b) {
	 return adapted.rational(a, b);
	}

	@Override
	public IRational rational(String rat) throws NumberFormatException {
	  return adapted.rational(rat);
	}

	@Override
	public IReal real(String s, int p) throws NumberFormatException {
	  return adapted.real(s, p);
	}

	@Override
	public IReal real(double d, int p) {
	  return adapted.real(d, p);
	}

	@Override
	public int getPrecision() {
	  return adapted.getPrecision();
	}

	@Override
	public int setPrecision(int p) {
	  return adapted.setPrecision(p);
	}

	@Override
	public IReal pi(int precision) {
	  return adapted.pi(precision);
	}

	@Override
	public IReal e(int precision) {
	  return adapted.e(precision);
	}

	@Override
	public IString string(int[] chars) throws IllegalArgumentException {
	  return adapted.string(chars);
	}

	@Override
	public IString string(int ch) throws IllegalArgumentException {
	  return adapted.string(ch);
	}

	@Override
	public ISourceLocation sourceLocation(URI uri, int offset, int length) {
	  return adapted.sourceLocation(uri, offset, length);
	}

	@Override
	public ITuple tuple(Type type, IValue... args) {
	  return adapted.tuple(type, args);
	}

	@Override
	public INode node(String name, Map<String, IValue> annotations, IValue... children) throws FactTypeUseException {
	  return adapted.node(name, annotations, children);
	}

	@Override
	public INode node(String name, IValue[] children, Map<String, IValue> keyArgValues) throws FactTypeUseException {
	  return adapted.node(name, children, keyArgValues);
	}

	@Override
	public IList listRelation(Type tupleType) {
	  return adapted.listRelation(tupleType);
	}

	@Override
	public IList listRelation(IValue... elems) {
	  return adapted.listRelation(elems);
	}

	@Override
	public IListWriter listRelationWriter(Type type) {
	  return adapted.listRelationWriter(type);
	}

	@Override
	public IListWriter listRelationWriter() {
	  return adapted.listRelationWriter();
	}

	@Override
	public IMap map(Type mapType) {
	  return adapted.map(mapType);
	}

	@Override
	public IMapWriter mapWriter(Type mapType) {
	 return adapted.mapWriter(mapType);
	}

  @Override
  public IConstructor constructor(Type constructor, Map<String, IValue> annotations, IValue... children)
      throws FactTypeUseException {
   return adapted.constructor(constructor, annotations, children);
  }
  
  @Override
  public IConstructor constructor(Type constructor, IValue[] children, Map<String, IValue> kwParams)
      throws FactTypeUseException {
   return adapted.constructor(constructor, children, kwParams);
  }

}
