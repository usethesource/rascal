package org.meta_environment.rascal.std;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.RuntimeExceptionFactory;

public class String {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	
	public static IValue charAt(IString s, IInteger i) throws IndexOutOfBoundsException
	//@doc{charAt -- return the character at position i in string s.}
	{
	  try {
	    return values.integer(s.getValue().charAt(i.intValue()));
	  }
	  catch (IndexOutOfBoundsException e) {
	    throw RuntimeExceptionFactory.indexOutOfBounds(i, null);
	  }
	}

	public static IValue endsWith(IString s, IString suffix)
	//@doc{endWith -- returns true if string s ends with given string suffix.}
	{
	  return values.bool(s.getValue().endsWith(suffix.getValue()));
	}

	public static IValue format(IString s, IString dir, IInteger n, IString pad)
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
	
	public static IValue isEmpty(IString s)
	//@doc{isEmpty -- is string empty?}
	{
		return values.bool(s.getValue().length() == 0);
	}

	public static IValue reverse(IString s)
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

	public static IValue size(IString s)
	//@doc{size -- return the length of string s.}
	{
	  return values.integer(s.getValue().length());
	}

	public static IValue startsWith(IString s, IString prefix)
	//@doc{startsWith -- return true if string s starts with the string prefix.}
	{
	  return values.bool(s.getValue().startsWith(prefix.getValue()));
	}

	public static IValue toLowerCase(IString s)
	//@doc{toLowerCase -- convert all characters in string s to lowercase.}
	{
	  return values.string(s.getValue().toLowerCase());
	}

	public static IValue toUpperCase(IString s)
	//@doc{toUpperCase -- convert all characters in string s to uppercase.}
	{
	  return values.string(s.getValue().toUpperCase());
	}


}
