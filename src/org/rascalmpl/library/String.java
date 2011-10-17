/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library;

import java.math.BigInteger;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.OriginValueFactory;

public class String {
	private static final TypeFactory types = TypeFactory.getInstance();
	private final IValueFactory values;
	
	public String(IValueFactory values){
		super();
		
		this.values = values;
	}
	
	
	public IList origins(IString str) {
		return ((OriginValueFactory.TString)str).getOrigins();
	}
	
	
	public IValue stringChar(IInteger i){
		byte ccode[] = { (byte) i.intValue()};
		return values.string(new java.lang.String(ccode));
	}
	
	public IValue stringChars(IList lst){
		int n = lst.length();
		byte ccodes[] = new byte[n];
		for(int i = 0; i < n; i ++){
			ccodes[i] = (byte) ((IInteger) lst.get(i)).intValue();
		}
		return values.string(new java.lang.String(ccodes));
	}
	
	public IValue charAt(IString s, IInteger i) throws IndexOutOfBoundsException
	//@doc{charAt -- return the character at position i in string s.}
	{
	  try {
	    return values.integer(s.getValue().charAt(i.intValue()));
	  }
	  catch (IndexOutOfBoundsException e) {
	    throw RuntimeExceptionFactory.indexOutOfBounds(i, null, null);
	  }
	}

	public IValue endsWith(IString s, IString suffix)
	//@doc{endWith -- returns true if string s ends with given string suffix.}
	{
	  return values.bool(s.getValue().endsWith(suffix.getValue()));
	}
	
	public IString trim(IString s) {
		return values.string(s.getValue().trim());
	}

	public IValue format(IString s, IString dir, IInteger n, IString pad)
	//@doc{format -- return string of length n, with s placed according to dir (left/center/right) and padded with pad}
	{
	    StringBuffer res = new StringBuffer();
	    int sLen = s.getValue().length();
	    int nVal = n.intValue();
	    if(sLen > nVal){
	       return s;
	    }
	    int padLen = pad.getValue().length();
	    java.lang.String dirVal = dir.getValue();
	    int start;
	    
	    if(dirVal.equals("right"))
	       start = nVal - sLen;
	    else if(dirVal.equals("center"))
	       start = (nVal - sLen)/2;
	    else
	       start = 0;
	    
	    int i = 0;
	    while(i < start){
	         if(i + padLen < start){
	         	res.append(pad.getValue());
	         	i+= padLen;
	         } else {
	         	res.append(pad.getValue().substring(0, start - i));
	         	i += start -i;
	         }
	    }
	    res.append(s.getValue());
	    i = start + sLen;
	    while(i < nVal){
	         if(i + padLen < nVal){
	         	res.append(pad.getValue());
	         	i += padLen;
	         } else {
	         	res.append(pad.getValue().substring(0, nVal - i));
	         	i += nVal - i;
	         }
	    }
	    return values.string(res.toString());
	}
	
	public IValue isEmpty(IString s)
	//@doc{isEmpty -- is string empty?}
	{
		return values.bool(s.getValue().length() == 0);
	}

	public IValue reverse(IString s)
	//@doc{reverse -- return string with all characters in reverse order.}
	{
	   java.lang.String sval = s.getValue();
	   char [] chars = new char[sval.length()];
	   int n = sval.length();

	   for(int i = 0; i < n; i++){
	     chars[n - i  - 1] = sval.charAt(i);
	   }
	   return values.string(new java.lang.String(chars));
	}

	public IValue size(IString s)
	//@doc{size -- return the length of string s.}
	{
	  return values.integer(s.getValue().length());
	}

	public IValue startsWith(IString s, IString prefix)
	//@doc{startsWith -- return true if string s starts with the string prefix.}
	{
	  return values.bool(s.getValue().startsWith(prefix.getValue()));
	}

	public IValue substring(IString s, IInteger begin) {
		if (s instanceof OriginValueFactory.TString) {
			return ((OriginValueFactory.TString)s).substring(begin.intValue(), ((OriginValueFactory.TString)s).length());
		}
		try {
			return values.string(s.getValue().substring(begin.intValue()));
		} catch (IndexOutOfBoundsException e) {
			throw RuntimeExceptionFactory.indexOutOfBounds(begin, null, null);
		}
	}
	
	public IValue substring(IString s, IInteger begin, IInteger end) {
		if (s instanceof OriginValueFactory.TString) {
			return ((OriginValueFactory.TString)s).substring(begin.intValue(), end.intValue());
		}
		try {
			return values.string(s.getValue().substring(begin.intValue(), end.intValue()));
		} catch (IndexOutOfBoundsException e) {
			int bval = begin.intValue();
			IInteger culprit = (bval < 0 || bval >= s.getValue().length()) ? begin : end;
		    throw RuntimeExceptionFactory.indexOutOfBounds(culprit, null, null);
		  }
	
	}
	
	public IValue toInt(IString s)
	//@doc{toInt -- convert a string s to integer}
	{
		try {
			java.lang.String sval = s.getValue();
			boolean isNegative = false;
			int radix = 10;
			
			if (sval.equals("0")) {
				return values.integer(0);
			}
			
			if (sval.startsWith("-")) {
				isNegative = true;
				sval = sval.substring(1);
			}
			if (sval.startsWith("0x") || sval.startsWith("0X")) {
				radix = 16;
				sval = sval.substring(2);
			} else if (sval.startsWith("0")) {
				radix = 8;
				sval = sval.substring(1);
			}
			BigInteger bi = new BigInteger(isNegative ? "-" + sval : sval, radix);
			return values.integer(bi.toString());
		}
		catch (NumberFormatException e){
			throw RuntimeExceptionFactory.illegalArgument(null, null);
		}
	}
	
	public IValue toInt(IString s, IInteger r)
	{
		try {
			java.lang.String sval = s.getValue();
			boolean isNegative = false;
			int radix = r.intValue();
			
			if (sval.equals("0")) {
				return values.integer(0);
			}
			
			if (sval.startsWith("-")) {
				isNegative = true;
				sval = sval.substring(1);
			}
			BigInteger bi = new BigInteger(isNegative ? "-" + sval : sval, radix);
			return values.integer(bi.toString());
		}
		catch (NumberFormatException e){
			throw RuntimeExceptionFactory.illegalArgument(null, null);
		}
	}
	
	public IValue toReal(IString s)
	//@doc{toReal -- convert a string s to a real}
	{
		try {
			return values.real(s.getValue());
		}
		catch (NumberFormatException e){
			throw RuntimeExceptionFactory.illegalArgument(null, null);
		}
	}

	public IValue toLowerCase(IString s)
	//@doc{toLowerCase -- convert all characters in string s to lowercase.}
	{
	  return values.string(s.getValue().toLowerCase());
	}

	public IValue toUpperCase(IString s)
	//@doc{toUpperCase -- convert all characters in string s to uppercase.}
	{
		if (s instanceof OriginValueFactory.TString) {
			return ((OriginValueFactory.TString)s).toUpperCase();
		}
	  return values.string(s.getValue().toUpperCase());
	}
	
	private boolean match(char[] subject, int i, char [] pattern){
		if(i + pattern.length > subject.length)
			return false;
		for(int k = 0; k < pattern.length; k++){
			if(subject[i] != pattern[k])
				return false;
			i++;
		}
		return true;
	}
	
	public IValue replaceAll(IString str, IString find, IString replacement){
		StringBuilder b = new StringBuilder(str.getValue().length() * 2); 
		char [] input = str.getValue().toCharArray();
		char [] findChars = find.getValue().toCharArray();
		char [] replChars = replacement.getValue().toCharArray();
		
		int i = 0;
		boolean matched = false;
		while(i < input.length){
			if(match(input,i,findChars)){
				matched = true;
				b.append(replChars);
				i += findChars.length;
			} else {
				b.append(input[i]);
				i++;
			}
		}
		return (!matched) ? str : values.string(b.toString());
	}
	
	public IValue replaceFirst(IString str, IString find, IString replacement){
		StringBuilder b = new StringBuilder(str.getValue().length() * 2); 
		char [] input = str.getValue().toCharArray();
		char [] findChars = find.getValue().toCharArray();
		char [] replChars = replacement.getValue().toCharArray();
		
		int i = 0;
		boolean matched = false;
		while(i < input.length){
			if(!matched && match(input,i,findChars)){
				matched = true;
				b.append(replChars);
				i += findChars.length;
				
			} else {
				b.append(input[i]);
				i++;
			}
		}
		return (!matched) ? str : values.string(b.toString());
	}
	
	public IValue replaceLast(IString str, IString find, IString replacement){
		StringBuilder b = new StringBuilder(str.getValue().length() * 2); 
		char [] input = str.getValue().toCharArray();
		char [] findChars = find.getValue().toCharArray();
		char [] replChars = replacement.getValue().toCharArray();
		
		int i = input.length - findChars.length;
		while(i >= 0){
			if(match(input,i,findChars)){
				for(int j = 0; j < i; j++)
					b.append(input[j]);
				b.append(replChars);
				for(int j = i + findChars.length; j < input.length; j++)
					b.append(input[j]);
				return values.string(b.toString());
			}
			i--;
		}
		return str;
	}
	
	
	public IValue escape(IString str, IMap substitutions) {
		StringBuilder b = new StringBuilder(str.getValue().length() * 2); 
		char[] input = str.getValue().toCharArray();
		
		for (char c : input) {
			IString sub = (IString) substitutions.get(values.string(Character.toString(c)));
			if (sub != null) {
				b.append(sub.getValue());
			}
			else {
				b.append(c);
			}
		}
		return values.string(b.toString());
	}
	
	public IValue contains(IString str, IString find){
		return values.bool(str.getValue().indexOf(find.getValue()) >= 0);
	}
	
	public IValue findAll(IString str, IString find){
		char[] input = str.getValue().toCharArray();
		char [] findChars = find.getValue().toCharArray();
		IListWriter w = values.listWriter(types.integerType());
		
		for(int i = 0; i <= input.length - findChars.length; i++){
			if(match(input, i, findChars)){
				w.append(values.integer(i));
			}
		}
		return w.done();
	}
}
