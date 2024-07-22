@license{
  Copyright (c) 2009-2022 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Atze van der Ploeg - ploeg@cwi.nl - CWI}
@synopsis{Encapsulate any optional value using `Maybe[&T]`}
module util::Maybe

@synopsis{Generic data type to encapsulate any value, optionally.}
@examples{
```rascal-shell
import util::Maybe;
// nothing() can always be assigned to a variable of type `Maybe[Anytype]`
Maybe[int] myIntOption = nothing();
// another example of the same feature:
Maybe[str] myStrOption = nothing();
// if you do have a value, the type of the parameter of `just` must align:
myStrOption = just("a string");
```

If you don't align the type of the parameter with the parameter type of `Maybe`, static errors ensue:
```rascal-shell-continue-error
myStrOption = just(42);
```

Here's a function that sometimes returns a value and otherwise returns `nothing()`:
```rascal
Maybe[int] indexOf(list[int] haystack, int needle) {
   for (i <- index(haystack), haystack[i] == needle) {
      return just(i);
   }
   
   return nothing();
}
```
}
data Maybe[&A] 
   = nothing() 
   | just(&A val)
   ;
