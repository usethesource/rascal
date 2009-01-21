module String

public int java charAt(str s, int i) throws out_of_range(str msg)
@doc{charAt -- return the character at position i in string s.}
@java-imports{import java.lang.String;}
{
  try {
    return values.integer(s.getValue().charAt(i.getValue()));
  }
  catch (IndexOutOfBoundsException e) {
    IString msg = values.string(i + " is out of range in " + s);
    throw new RascalException(values.node("out_of_range", msg));
  }
}

public bool java endsWith(str s, str suffix)
@doc{endWith -- returns true if string s ends with given string suffix.}
@java-imports{import java.lang.String;}
{
  return values.bool(s.getValue().endsWith(suffix.getValue()));
}

public str java reverse(str s)
@doc{reverse -- return string with all characters in reverse order.}
@java-imports{import java.lang.String;}
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
@java-imports{import java.lang.String;}
{
  return values.integer(s.getValue().length());
}

public bool java startsWith(str s, str prefix)
@doc{startsWith -- return true if string s starts with the string prefix.}
@java-imports{import java.lang.String;}
{
  return values.bool(s.getValue().startsWith(prefix.getValue()));
}

public str java toLowerCase(str s)
@doc{toLowerCase -- convert all characters in string s to lowercase.}
@java-imports{import java.lang.String;}
{
  return values.string(s.getValue().toLowerCase());
}

public str java toUpperCase(str s)
@doc{toUpperCase -- convert all characters in string s to uppercase.}
@java-imports{import java.lang.String;}
{
  return values.string(s.getValue().toUpperCase());
}

