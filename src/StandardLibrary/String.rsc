module String

public int java charAt(str s, int i) throws out_of_range(str msg)
@doc{charAt -- return the character at position i in string s.}
@javaClass{org.meta_environment.rascal.std.String};

public bool java endsWith(str s, str suffix)
@doc{endWith -- returns true if string s ends with given string suffix.}
@javaClass{org.meta_environment.rascal.std.String};

private str java format(str s, str dir, int n, str pad)
@doc{format -- return string of length n, with s placed according to dir (left/center/right) and padded with pad}
@javaClass{org.meta_environment.rascal.std.String};

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

public bool java isEmpty(str s)
 @doc{isEmpty -- is string empty?}
 @javaClass{org.meta_environment.rascal.std.String};


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
@javaClass{org.meta_environment.rascal.std.String};

public int java size(str s)
@doc{size -- return the length of string s.}
@javaClass{org.meta_environment.rascal.std.String};

public bool java startsWith(str s, str prefix)
@doc{startsWith -- return true if string s starts with the string prefix.}
@javaClass{org.meta_environment.rascal.std.String};

public str java toLowerCase(str s)
@doc{toLowerCase -- convert all characters in string s to lowercase.}
@javaClass{org.meta_environment.rascal.std.String};

public str java toUpperCase(str s)
@doc{toUpperCase -- convert all characters in string s to uppercase.}
@javaClass{org.meta_environment.rascal.std.String};

