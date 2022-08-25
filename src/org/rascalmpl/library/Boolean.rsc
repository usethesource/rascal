@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

@doc{
.Synopsis
Library functions for Booleans.

.Description

For operators on Boolean values see [Boolean]((Rascal:Values-Boolean)) in the Rascal Language Reference.

The following functions are defined for Booleans:
(((TOC)))
}
module Boolean

import Exception;

@doc{
.Synopsis
Return an arbitrary Boolean value.

.Examples
```rascal-shell
import Boolean;
arbBool();
arbBool();
arbBool();
```

.Benefits
`arbInt` is a convenient generator for arbitrary binary choices.
}

@javaClass{org.rascalmpl.library.Prelude}
public java bool arbBool();

@doc{
.Synopsis
Convert the strings "true" or "false" to a bool.
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
.Synopsis
Convert a Boolean value to integer.

.Description
Maps `true` to `1` and `false` to `0`.

.Examples
```rascal-shell
import Boolean;
toInt(true);
toInt(false);
```
}
public int toInt(bool b)
{
  return b ? 1 : 0;
}

@doc{
.Synopsis
Convert Boolean value to real.

.Description
Maps `true` to `1.0` and `false` to `0.0`.

.Examples
```rascal-shell
import Boolean;
toReal(true);
toReal(false);
```

}
public real toReal(bool b)
{
  return b ? 1.0 : 0.0;
}

@doc{
.Synopsis
Convert Boolean value to string.

.Description
Maps `true` to `"true"` and `false` to `"false"`.

.Examples
```rascal-shell
import Boolean;
toString(true);
toString(false);
```

}
public str toString(bool b)
{
  return b ? "true" : "false";
}

