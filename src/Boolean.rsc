@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

module Boolean

@doc{
Name:     arbBool
Synopsis: Return an arbitrary Boolean value.
Function: `bool arbBool( )`

Examples:
<screen>
import Boolean;
arbBool();
arbBool();
arbBool();
</screen>

Benefits:
<tt>arbInt</tt> is a convenient generator for arbitrary binary choices.
}

@javaClass{org.rascalmpl.library.Boolean}
public java bool arbBool();

@deprecated{Useless function that will be removed}
@doc{
Name: 	  fromInt
Synopsis: Convert from integer to Boolean.

Function: `bool fromInt(int n)`

Description:
Convert an integer to a Boolean value: all non-zero integers are mapped to `true`, zero is mapped to `false`.

Examples:
<screen>
import Boolean;
fromInt(13);
fromInt(0);
</screen>
}
public bool fromInt(int i)
{
  return i != 0;
}

@doc{
Name: fromString
Synopsis: Convert the strings "true" or "false" to a bool.
Function: `bool fromString(str s)`
}

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

@doc{
Name:     toInt
Synopsis: Convert a Boolean value to integer.
Function: int toInt(bool b)

Description:
Maps `true` to `1` and `false` to 0.

Examples:
<screen>
import Boolean;
toInt(true);
toInt(false);
</screen>
}
public int toInt(bool b)
{
  return b ? 1 : 0;
}

@doc{
Name:     toReal
Synopsis: Convert Boolean value to real.
Function: `real toReal(bool b)`

Description:
Maps `true` to `1,0` and `false` to `0.0`.

Examples:
<screen>
import Boolean;
toReal(true);
toReal(false);
</screen>

}
public real toReal(bool b)
{
  return b ? 1.0 : 0.0;
}

@doc{
Name:     toString
Synopsis: Convert Boolean value to string.
Function: `str toString(bool b)`

Description:
Maps `true` to `"true"` and `false` to `"false"`.

Examples:
<screen>
import Boolean;
toString(true);
toString(false);
</screen>

}
public str toString(bool b)
{
  return b ? "true" : "false";
}

