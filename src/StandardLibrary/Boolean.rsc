module Boolean

public bool java arbBool()
@doc{arbBool -- get an arbitrary boolean value.}
@javaClass{org.meta_environment.rascal.std.Boolean};

public bool fromInt(int i)
@doc{fromInt -- convert an integer to a bool}
{
  return i != 0;
}

public bool fromString(str s)
@doc{fromString -- convert the strings "true" or "false" to a bool}
{
  if (s == "true") {
    return true;
  }
  if (s == "false") {
    return false;
  }
//  throw s + " is not \"true\" or \"false\";
}

public int toInt(bool b)
@doc{toInt -- convert a boolean value to integer.}
{
  return b ? 1 : 0;
}



public real toReal(bool b)
@doc{toReal -- convert a boolean value to a real value.}
{
  return b ? 1.0 : 0.0;
}

public str toString(bool b)
@doc{toString -- convert a boolean value to a string.}
{
  return b ? "true" : "false";
}

