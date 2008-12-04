module String

public int java charAt(str s, int i) throws out_of_range(str msg)
@doc{charAt returns the character at position i in string s.}
@java-imports{import java.lang.String;}
{
  try {
    return values.integer(s.getValue().charAt(i.getValue()));
  }
  catch (IndexOutOfBoundsException e) {
    IString msg = values.string(i + " is out of range in " + s);
    throw new RascalException(values.tree("out_of_range", msg));
  }
}

public bool java endsWith(str s, str suffix)
@doc{endWith(s, suffix) returns true if string s ends with the string suffix.}
@java-imports{import java.lang.String;}
{
  return values.bool(s.getValue().endsWith(suffix.getValue()));
}

// public str java reverse(str s)
// @doc{reverse returns string with all characters in s in reverse order.}
// @java-imports{import java.lang.String;}
// {
//  return values.string(s.getValue().reverse());
//}

public int java size(str s)
@doc{size returns the length of string s.}
@java-imports{import java.lang.String;}
{
  return values.integer(s.getValue().length());
}

public bool java startsWith(str s, str prefix)
@doc{startsWith(s, prefix) returns true if string s starts with the string prefix.}
@java-imports{import java.lang.String;}
{
  return values.bool(s.getValue().startsWith(prefix.getValue()));
}

public str java toLowerCase(str s)
@java-imports{import java.lang.String;}
{
  return values.string(s.getValue().toLowerCase());
}

public int java toUpperCase(str s)
@java-imports{import java.lang.String;}
{
  return values.string(s.getValue().toUpperCase());
}

