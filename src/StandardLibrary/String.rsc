module String

public int java charAt(str s, int i)
@doc{charAt returns the character at position i in string s.}
throws out_of_range(str msg)
@JavaImports{java.util.String;}
{
  return s.charAt(i);
}

public bool java endsWith(str s, str suffix)
@doc{endWith(s, suffix) returns true if string s ends with the string suffix.}
@JavaImports{java.util.String;}
{
  return s.endsWith(suffix);
}

public str java reverse(str s)
@doc{reverse returns string with all characters in s in reverse order.}
@JavaImports{java.util.String;}
{
  return s.reverse();
}

public int java size(str s)
@doc{size returns the length of string s.}
@JavaImports{java.util.String;}
{
  return s.length();
}

public bool java startsWith(str s, str prefix)
@doc{startsWith(s, prefix) returns true if string s starts with the string prefix.}
@JavaImports{java.util.String;}
{
  return s.startsWith(prefix);
}

public int java toLowerCase(str s)
@JavaImports{java.util.String;}
{
  return s.toLowerCase();
}

public int java toUpperCase(str s)
@JavaImports{java.util.String;}
{
  return s.toUpperCase();
}

