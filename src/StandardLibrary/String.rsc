module String

public int java charAt(str s, int i)
throws out_of_range(str msg)
@JavaImports{java.util.String;}
{
  return s.charAt(i);
}

public bool java endsWith(str s, str suffix)
@JavaImports{java.util.String;}
{
  return s.endsWith(suffix);
}

public str java reverse(str s)
@JavaImports{java.util.String;}
{
  return s.reverse();
}

public int java size(str s)
@JavaImports{java.util.String;}
{
  return s.length();
}

public bool java startsWith(str s, str prefix)
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

