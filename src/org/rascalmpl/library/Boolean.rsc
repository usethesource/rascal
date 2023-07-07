@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}


@synopsis{

Library functions for Booleans.

}
@description{

The following library functions are defined for Booleans:
(((TOC)))
}
module Boolean

import Exception;


@synopsis{

Return an arbitrary Boolean value.

}
@examples{

```rascal-shell
import Boolean;
arbBool();
arbBool();
arbBool();
```

}
@benefits{

`arbInt` is a convenient generator for arbitrary binary choices.
}

@javaClass{org.rascalmpl.library.Prelude}
public java bool arbBool();


@synopsis{

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


@synopsis{

Convert a Boolean value to integer.

}
@description{

Maps `true` to `1` and `false` to `0`.

}
@examples{

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


@synopsis{

Convert Boolean value to real.

}
@description{

Maps `true` to `1.0` and `false` to `0.0`.

}
@examples{

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


@synopsis{

Convert Boolean value to string.

}
@description{

Maps `true` to `"true"` and `false` to `"false"`.

}
@examples{

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

