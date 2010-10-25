module Boolean

@doc{Get an arbitrary boolean value.}
@javaClass{org.rascalmpl.library.Boolean}
public bool java arbBool();

@deprecated{Useless function that will be removed}
@doc{Convert an integer to a bool}
public bool fromInt(int i)
{
  return i != 0;
}

@doc{Convert the strings "true" or "false" to a bool}
public bool fromString(str s)
{ 
  if (s == "true") {
    return true;
  }
  if (s == "false") {
    return false;
  }
//  throw s + " is not \"true\" or \"false\";
}

@doc{Convert a boolean value to integer.}
public int toInt(bool b)
{
  return b ? 1 : 0;
}



@doc{Convert a boolean value to a real value.}
public real toReal(bool b)
{
  return b ? 1.0 : 0.0;
}

@doc{Convert a boolean value to a string.}
public str toString(bool b)
{
  return b ? "true" : "false";
}

