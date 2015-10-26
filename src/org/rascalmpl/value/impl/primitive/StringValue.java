/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Arnold Lankamp - interfaces and implementation
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.value.impl.primitive;

import java.nio.CharBuffer;

import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.impl.AbstractValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.visitors.IValueVisitor;

/**
 * Implementation of IString.
 * 
 * @author Arnold Lankamp
 */
/*package*/ class StringValue {
	private final static Type STRING_TYPE = TypeFactory.getInstance().stringType();

	/*package*/ static IString newString(String value) {
		if (value ==null) value = "";
		return newString(value, containsSurrogatePairs(value));
	}
	/*package*/ static IString newString(String value, boolean fullUnicode) {
		if (value ==null) value = "";
		if (fullUnicode) {
			return new FullUnicodeString(value);
		}
		return new SimpleUnicodeString(value);
	}

	private static boolean containsSurrogatePairs(String str) {
		if (str == null) {
			return false;
		}
		int len = str.length(); 
		for (int i = 1; i < len; i++) {
			if (Character.isSurrogatePair(str.charAt(i - 1), str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	private static class FullUnicodeString  extends AbstractValue implements IString {
		protected final String value;
	
	
		private FullUnicodeString(String value){
			super();
			
			this.value = value;
		}
	
		@Override
		public Type getType(){
			return STRING_TYPE;
		}
		
		@Override
		public String getValue(){
			return value;
		}
		
		@Override
		public IString concat(IString other){
			StringBuilder buffer = new StringBuilder();
			buffer.append(value);
			buffer.append(other.getValue());
			
			return StringValue.newString(buffer.toString(), true);
		}
		
		@Override
		public int compare(IString other){
			int result = value.compareTo(other.getValue());
			
			if(result > 0) return 1;
			if(result < 0) return -1;
			
			return 0;
		}
		
		@Override
		public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E{
			return v.visitString(this);
		}
		
		public int hashCode(){
			return value.hashCode();
		}
		
		public boolean equals(Object o){
			if(o == null) return false;
			if(this == o) return true;
			if(o.getClass() == getClass()){
				FullUnicodeString otherString = (FullUnicodeString) o;
				return value.equals(otherString.value);
			}
			
			return false;
		}
		
		@Override
		public boolean isEqual(IValue value){
			return equals(value);
		}
		
		@Override
		public IString reverse() {
			StringBuilder b = new StringBuilder(value);
			return newString(b.reverse().toString(), true);
		}
	
		@Override
		public int length() {
			return value.codePointCount(0, value.length());
		}
	
		private int codePointAt(java.lang.String str, int i) {
			return str.codePointAt(str.offsetByCodePoints(0,i));
		}
		
		@Override
		public IString substring(int start, int end) {
			 return newString(value.substring(value.offsetByCodePoints(0, start),value.offsetByCodePoints(0, end)));
		}
	
		@Override
		public IString substring(int start) {
			 return newString(value.substring(value.offsetByCodePoints(0, start)));
		}
		
		@Override
		public int charAt(int index) {
			return codePointAt(value, index);
		}
		
		private int nextCP(CharBuffer cbuf){
			int cp = Character.codePointAt(cbuf, 0); 
			if(cbuf.position() < cbuf.capacity()){
				cbuf.position(cbuf.position() + Character.charCount(cp));
			}
			return cp;
		}
		
		private void skipCP(CharBuffer cbuf){
			if(cbuf.hasRemaining()){
				int cp = Character.codePointAt(cbuf, 0); 
				cbuf.position(cbuf.position() + Character.charCount(cp));
			}
		}
		
		@Override
		public IString replace(int first, int second, int end, IString repl) {
			StringBuilder buffer = new StringBuilder();
		
			int valueLen = value.codePointCount(0, value.length());
			CharBuffer valueBuf;
			
			int replLen = repl.length();
			CharBuffer replBuf = CharBuffer.wrap(repl.getValue());
			
			int increment = Math.abs(second - first);
			if(first <= end){ 
				valueBuf = CharBuffer.wrap(value);
				int valueIndex = 0;
				// Before begin (from left to right)
				while(valueIndex < first){
					buffer.appendCodePoint(nextCP(valueBuf)); valueIndex++;
				}
				int replIndex = 0;
				boolean wrapped = false;
				// Between begin and end
				while(valueIndex < end){
					buffer.appendCodePoint(nextCP(replBuf)); replIndex++;
					if(replIndex == replLen){
						replBuf.position(0); replIndex = 0;
						wrapped = true;
					}
					skipCP(valueBuf); valueIndex++; //skip the replaced element
					for(int j = 1; j < increment && valueIndex < end; j++){
						buffer.appendCodePoint(nextCP(valueBuf)); valueIndex++;
					}
				}
				if(!wrapped){
					while(replIndex < replLen){
						buffer.appendCodePoint(nextCP(replBuf)); replIndex++;
					}
				}
				// After end
				
				while( valueIndex < valueLen){
					buffer.appendCodePoint(nextCP(valueBuf)); valueIndex++;
				}
			} else { 
				// Before begin (from right to left)
				
				// Place reversed value of fValue in valueBuffer for better sequential code point access
				// Also add code points to buffer in reverse order and reverse again at the end
				valueBuf = CharBuffer.wrap(new StringBuilder(value).reverse().toString());
				
				int valueIndex = valueLen - 1;
				while(valueIndex > first){
					buffer.appendCodePoint(nextCP(valueBuf));
					valueIndex--;
				}
				// Between begin (right) and end (left)
				int replIndex = 0;
				boolean wrapped = false;
				while(valueIndex > end){
					buffer.appendCodePoint(nextCP(replBuf)); replIndex++;
					if(replIndex == repl.length()){
						replBuf.position(0); replIndex = 0; 
						wrapped = true;
					}
					skipCP(valueBuf); valueIndex--; //skip the replaced element
					for(int j = 1; j < increment && valueIndex > end; j++){
						buffer.appendCodePoint(nextCP(valueBuf)); valueIndex--;
					}
				}
				if(!wrapped){
					while(replIndex < replLen){
						buffer.appendCodePoint(nextCP(replBuf)); replIndex++;
					}
				}
				// Left of end
				while(valueIndex >= 0){
					buffer.appendCodePoint(nextCP(valueBuf)); valueIndex--;
				}
				buffer.reverse();
			}
			
			String res = buffer.toString();
			return StringValue.newString(res);
		}
	}
	
	private static class SimpleUnicodeString extends FullUnicodeString {
		public SimpleUnicodeString(String value) {
			super(value);
		}
		@Override
		public boolean equals(Object o) {
			if(o == null) return false;
			if(this == o) return true;
			if(o.getClass() == getClass()){
				SimpleUnicodeString otherString = (SimpleUnicodeString) o;
				return value.equals(otherString.value);
			}
			
			return false;
		}

		// Common operations which do not need to be slow
		@Override
		public int length() {
			return value.length();
		}
		@Override
		public int charAt(int index) {
			return value.charAt(index);
		}

		@Override
		public IString substring(int start) {
			return newString(value.substring(start), false); 
		}
		
		@Override
		public IString substring(int start, int end) {
			return newString(value.substring(start, end), false); 
		}
		
		@Override
		public IString reverse() {
			return newString(new StringBuilder(value).reverse().toString(), false); 
		}
		
		@Override
		public IString concat(IString other) {
			StringBuilder buffer = new StringBuilder();
			buffer.append(value);
			buffer.append(other.getValue());
			
			return StringValue.newString(buffer.toString(), other.getClass() != getClass());
		}
	}
}
