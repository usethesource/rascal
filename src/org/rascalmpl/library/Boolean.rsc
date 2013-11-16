@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

module Boolean

import Exception;

@doc{
Synopsis: Return an arbitrary Boolean value.

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

@javaClass{org.rascalmpl.library.Prelude}
public java bool arbBool();

@doc{
Synopsis: Convert the strings "true" or "false" to a bool.
}
public bool fromString(str s)
{ 
  if (s == "true") {
    return true;
  }
  if (s == "false") {
    return false;
  }
  throw IllegalArgument(s, "not \"true\" or \"false\"");
}

@doc{
Synopsis: Convert a Boolean value to integer.

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
Synopsis: Convert Boolean value to real.

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
Synopsis: Convert Boolean value to string.

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

