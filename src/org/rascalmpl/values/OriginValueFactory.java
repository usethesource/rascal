/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.values;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.values.origins.Factory;

public class OriginValueFactory extends ValueFactory {
	private final static Type STRING_TYPE = TypeFactory.getInstance().stringType();

	private static class InstanceKeeper{
		public final static ValueFactory instance = new OriginValueFactory();
	}
	
	public static ValueFactory getInstance(){
		return InstanceKeeper.instance;
	}
	
	public abstract class TString implements IString {
		
		public Type getType() {
			return STRING_TYPE;
		}
		
		public <T> T accept(IValueVisitor<T> v) throws VisitorException {
			return v.visitString(this);
		}
		
		public TString concat(IString other) {
			return new Concat(this, (TString)other);
		}
		
		public boolean isEqual(IValue other) {
			if (!(other instanceof IString)) {
				return false;
			}
			return getValue().equals(((IString)other).getValue());
		}
		
		public int compare(IString other){
			int result = getValue().compareTo(other.getValue());
			
			if(result > 0) return 1;
			if(result < 0) return -1;
			
			return 0;
		}
		
		public abstract int length();
		
		public abstract IList getOrigins();
		
		public abstract TString substring(int begin, int end);
		
		public abstract TString toUpperCase();
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof IString)) {
				return false;
			}
			return compare((IString)obj) == 0;
		}
	}
	
	private abstract class Unit extends TString {
		protected final String value;
		
		public Unit(String value) {
			this.value = value;
		}
		
		public int length() {
			return value.length();
		}
		
		public String getValue() {
			return value;
		}
	}
	
	private abstract class OrgUnit extends Unit {
		protected final ISourceLocation origin;
		
		protected OrgUnit(ISourceLocation origin, String value) {
			super(value);
			this.origin = origin;
		}
		
		public ISourceLocation getOrigin() {
			return origin;
		}
		
	}
	
	private class Expression extends OrgUnit{
		
		protected Expression(ISourceLocation origin, String value){
			super(origin, value);
		}

		public IList getOrigins(){
			return list(tuple(this, constructor(Factory.Origin_expression, getOrigin())));
		}
		
		public TString substring(int begin, int end){
			// You lose line and column information if you do this.
			ISourceLocation updatedOrigin = sourceLocation(origin.getURI(), origin.getOffset() + begin, end - begin, -1, -1, -1, -1);
			
			return new Expression(updatedOrigin, value.substring(begin, end));
		}
		
		public TString toUpperCase(){
			return new Expression(origin, value.toUpperCase());
		}
	}
	
	private class Literal extends OrgUnit{
		
		protected Literal(ISourceLocation origin, String value){
			super(origin, value);
		}
		
		public IList getOrigins(){
			return list(tuple(this, constructor(Factory.Origin_literal, getOrigin())));
		}
		
		public TString substring(int begin, int end){
			// You lose line and column information if you do this.
			ISourceLocation updatedOrigin = sourceLocation(origin.getURI(), origin.getOffset() + begin, end - begin, -1, -1, -1, -1);
			
			return new Literal(updatedOrigin, value.substring(begin, end));
		}
		
		public TString toUpperCase(){
			return new Literal(origin, value.toUpperCase());
		}
	}
	
	private class Anonymous extends Unit{
		
		protected Anonymous(String value){
			super(value);
		}
		
		public IList getOrigins(){
			return list(tuple(this, constructor(Factory.Origin_none)));
		}
		
		public TString substring(int begin, int end){
			return new Anonymous(value.substring(begin, end));
		}
		
		public TString toUpperCase(){
			return new Anonymous(value.toUpperCase());
		}
	}
	
	
	
	private class Concat extends TString {
		private final TString lhs;
		private final TString rhs;
		private final int length;

		public Concat(TString lhs, TString rhs) {
			this.lhs = lhs;
			this.rhs = rhs;
			this.length = lhs.length() + rhs.length();
		}

		@Override
		public int length() {
			return length;
		}
		
		public String getValue() {
			return lhs.getValue() + rhs.getValue();
		}

		@Override
		public IList getOrigins() {
			return lhs.getOrigins().concat(rhs.getOrigins());
		}
		
		public TString substring(int begin, int end){
			int lhsLength = lhs.length();
			if(lhsLength >= begin){
				if(lhsLength >= end){
					return new Concat(lhs.substring(begin, end), rhs);
				}
				
				return new Concat(lhs.substring(begin, lhsLength), rhs.substring(0, end - lhsLength));
			}
			
			return new Concat(lhs, rhs.substring(begin - lhsLength, end - lhsLength));
		}
		
		public TString toUpperCase(){
			return new Concat(lhs.toUpperCase(), rhs.toUpperCase());
		}
	}
	
	@Override
	public IString string(String value) {
		return new Anonymous(value);
	}
	
	public IString literal(ISourceLocation origin, String s) {
		return new Literal(origin, s);
	}

	public IString expression(ISourceLocation origin, String s) {
		return new Expression(origin, s);
	}
	
	

}
