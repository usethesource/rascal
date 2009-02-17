module String

public int java charAt(str s, int i) throws out_of_range(str msg)
@doc{charAt -- return the character at position i in string s.}
@javaImports{
	import java.lang.String;
	import org.meta_environment.rascal.interpreter.errors.Error;
}
{
  try {
    return values.integer(s.getValue().charAt(i.getValue()));
  }
  catch (IndexOutOfBoundsException e) {
    IString msg = values.string(i + " is out of range in " + s);
    throw new IndexOutOfBoundsError("charAt", null);
  }
}

public bool java endsWith(str s, str suffix)
@doc{endWith -- returns true if string s ends with given string suffix.}
@javaImports{import java.lang.String;}
{
  return values.bool(s.getValue().endsWith(suffix.getValue()));
}

private str java format(str s, str dir, int n, str pad)
@doc{format -- return string of length n, with s placed according to dir (left/center/right) and padded with pad}
@javaImports{
	import java.lang.String;
	import java.lang.StringBuffer;
}
{
    StringBuffer res = new StringBuffer();
    int sLen = s.getValue().length();
    int nVal = n.getValue();
    if(sLen > nVal){
       return s;
    }
    int padLen = pad.getValue().length();
    String dirVal = dir.getValue();
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

public str center(str s, int n)
@doc{center -- center s in string of length n using spaces}
{
  return format(s, "center", n, " ");
}

public str center(str s, int n, str pad)
@doc{center -- center s in string of length n using pad}
{
  return format(s, "center", n, pad);
}

public str left(str s, int n)
@doc{left -- left align s in string of length n using spaces}
{
  return format(s, "left", n, " ");
}

public str left(str s, int n, str pad)
@doc{left -- left align s in string of length n using pad}
{
  return format(s, "left", n, pad);
}

public str right(str s, int n)
@doc{right -- right align s in string of length n using spaces}
{
  return format(s, "right", n, " ");
}

public str right(str s, int n, str pad)
@doc{right -- right align s in string of length n using pad}
{
  return format(s, "right", n, pad);
}

public str java reverse(str s)
@doc{reverse -- return string with all characters in reverse order.}
@javaImports{import java.lang.String;}
{
   String sval = s.getValue();
   char [] chars = new char[sval.length()];
   int n = sval.length();

   for(int i = 0; i < n; i++){
     chars[n - i  - 1] = sval.charAt(i);
   }
   return values.string(new String(chars));
}

public int java size(str s)
@doc{size -- return the length of string s.}
@javaImports{import java.lang.String;}
{
  return values.integer(s.getValue().length());
}

public bool java startsWith(str s, str prefix)
@doc{startsWith -- return true if string s starts with the string prefix.}
@javaImports{import java.lang.String;}
{
  return values.bool(s.getValue().startsWith(prefix.getValue()));
}

public str java toLowerCase(str s)
@doc{toLowerCase -- convert all characters in string s to lowercase.}
@javaImports{import java.lang.String;}
{
  return values.string(s.getValue().toLowerCase());
}

public str java toUpperCase(str s)
@doc{toUpperCase -- convert all characters in string s to uppercase.}
@javaImports{import java.lang.String;}
{
  return values.string(s.getValue().toUpperCase());
}

