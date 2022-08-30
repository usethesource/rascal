@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Atze van der Ploeg - ploeg@cwi.nl - CWI}
@doc{
.Synopsis
Represent an optional value.
}
module util::Maybe

@doc{
.Synopsis
Data type to represent an optional value.

.Types

.Usage

.Description

.Examples
```rascal
Maybe[int] linearSearch(list[int] l, int toFind) {
   for(i <- index(l)){
      if(l[i] == toFind) {
         return just(i);
      }
   }
   return nothing();
}
```
}
data Maybe[&A] = nothing() | just(&A val);
