module String

@doc{Return the character at position i in string s.}
@javaClass{org.meta_environment.rascal.std.String}
public int java charAt(str s, int i) throws out_of_range(str msg);

@doc{Returns true if string s ends with given string suffix.}
@javaClass{org.meta_environment.rascal.std.String}
public bool java endsWith(str s, str suffix);

@doc{Return string of length n, with s placed according to dir (left/center/right) and padded with pad}
@javaClass{org.meta_environment.rascal.std.String}
private str java format(str s, str dir, int n, str pad);

@doc{Center s in string of length n using spaces}
public str center(str s, int n)
{
  return format(s, "center", n, " ");
}

@doc{Center s in string of length n using pad}
public str center(str s, int n, str pad)
{
  return format(s, "center", n, pad);
}

@doc{Is string empty?}
@javaClass{org.meta_environment.rascal.std.String}
public bool java isEmpty(str s);

@doc{Left align s in string of length n using spaces}
public str left(str s, int n)
{
  return format(s, "left", n, " ");
}

@doc{Left align s in string of length n using pad}
public str left(str s, int n, str pad)
{
  return format(s, "left", n, pad);
}

@doc{Right align s in string of length n using spaces}
public str right(str s, int n)
{
  return format(s, "right", n, " ");
}

@doc{Right align s in string of length n using pad}
public str right(str s, int n, str pad)
{
  return format(s, "right", n, pad);
}

@doc{Return string with all characters in reverse order.}
@javaClass{org.meta_environment.rascal.std.String}
public str java reverse(str s);

@doc{Return the length of string s.}
@javaClass{org.meta_environment.rascal.std.String}
public int java size(str s);

@doc{Return true if string s starts with the string prefix.}
@javaClass{org.meta_environment.rascal.std.String}
public bool java startsWith(str s, str prefix);

@doc{Convert a string s to integer}
@javaClass{org.meta_environment.rascal.std.String}
public int java toInt(str s);

@doc{Convert all characters in string s to lowercase.}
@javaClass{org.meta_environment.rascal.std.String}
public str java toLowerCase(str s);

@doc{Convert a string s to a real}
@javaClass{org.meta_environment.rascal.std.String}
public real java toReal(str s);

@doc{Convert all characters in string s to uppercase.}
@javaClass{org.meta_environment.rascal.std.String}
public str java toUpperCase(str s);

@doc{Extract a substring from string s from begin to the end of s}
@javaClass{org.meta_environment.rascal.std.String}
public str java substring(str s, int begin);

@doc{Extract a substring from string s from begin to end}
@javaClass{org.meta_environment.rascal.std.String}
public str java substring(str s, int begin, int end);

