@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@doc{
.Synopsis
(de)serialization of JSON values. 
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@contributor{Tijs van der Storm - storm@cwi.nl (CWI)}
@contributor{Davy Landman - landman@cwi.nl (CWI)}

module lang::json::IO

@javaClass{org.rascalmpl.library.lang.json.IO}
public java str toJSON(value v);

@javaClass{org.rascalmpl.library.lang.json.IO}
public java str toJSON(value v, bool compact);

@javaClass{org.rascalmpl.library.lang.json.IO}
@reflect{Uses type store}
public java &T fromJSON(type[&T] typ, str src);


@javaClass{org.rascalmpl.library.lang.json.IO}
@doc{reads JSON values from a stream
In general:
 - Objects translate to maps
 - Arrays translate to lists
 - Booleans translate to bools
 
 If the expected type is different than the general `value`, then mapping and validation is influenced as follows:
 - node will try and read an object {names and args} as a keyword parameter map to a node ""(kwparams)
 - adt/cons will objects using the property name of the parent object as constructor name, and property names as keyword field names
 - datetime can be mapped from ints and dateformat strings (see setCalenderFormat())  
 - rationals will read array tuples [nom, denom] or {nominator: non, deminator: denom} objects
 - sets will read arrays
 - lists will read arrays
 - tuples read arrays
 - maps read objects
 - relations read arrays of arrays
 - locations will read strings which contain :/ as URI strings (encoded) and strings which do not contain :/ as absolute file names,
   also locations will read objects { scheme : str, authority: str?, path: str?, fragment: str?, query: str?, offset: int, length: int, begin: [bl, bc], end: [el, ec]}
 - reals will read strings or doubles
 - ints will read strings or doubles
 - nums will read like ints      
}
public java &T readJSON(type[&T] typ, loc src);
