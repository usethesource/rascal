@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@synopsis{All basic utility functions in one handy module to import}
@description{
Unlike the module name suggests the Prelude module is _not_ automatically imported when Rascal is started.
All it is, is a handy combination of extended modules that will provide the utility functions most
Rascal programmers need.

Prelude combines the following modules:

*  ((Library:module:Boolean))
*  ((Library:module:DateTime))
*  ((Library:module:Exception))
*  ((Library:module:IO))
*  ((Library:module:List))
*  ((Library:module:Map))
*  ((Library:module:Node))
*  ((Library:module:ParseTree))
*  ((Library:module:Relation))
*  ((Library:module:Set))
*  ((Library:module:String))
*  ((Library:module:ValueIO))
}
@examples{
```rascal-shell
import Prelude;
println("Hello World"); // from IO
size([1,2,3])           // from List
size({1,2,1})           // from Set
```
}
@benefits{
* Prelude makes all the feature of the extended modules transitively available to an importing module.
}
@pitfalls{
* Prelude combines many many function names and so the namespace of modules that import it is a bit crowded.
}
module Prelude

extend Boolean;
extend DateTime;
extend Exception;
extend Grammar;
extend IO;
extend List;
extend ListRelation;
extend Map;
extend Node;
extend ParseTree;
extend Relation;
extend Set;
extend String; 
extend Type;
extend ValueIO;  
