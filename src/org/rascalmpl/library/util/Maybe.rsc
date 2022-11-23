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
```rascal
Maybe[int] indexOf(list[int] l, int toFind) {
   for (i <- index(l), l[i] == toFind) {
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
