package org.rascalmpl.library;

import java.math.BigInteger;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class String {
	private final IValueFactory values;
	
	public String(IValueFactory values){
		super();
		
		this.values = values;
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
		try {
			return values.string(s.getValue().substring(begin.intValue()));
		} catch (IndexOutOfBoundsException e) {
			throw RuntimeExceptionFactory.indexOutOfBounds(begin, null, null);
		}
	}
	
	public IValue substring(IString s, IInteger begin, IInteger end) {
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
	  return values.string(s.getValue().toUpperCase());
	}
	
	public IValue escape(IString str, IMap substitutions) {
		StringBuilder b = new StringBuilder(); 
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
}
