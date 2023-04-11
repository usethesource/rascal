@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@synopsis{(De)serialization of JSON values.}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@contributor{Tijs van der Storm - storm@cwi.nl (CWI)}
@contributor{Davy Landman - landman@cwi.nl (CWI)}

module lang::json::IO

@javaClass{org.rascalmpl.library.lang.json.IO}
@deprecated{use writeJSON}
public java str toJSON(value v);

@javaClass{org.rascalmpl.library.lang.json.IO}
@deprecated{use asJSON}
public java str toJSON(value v, bool compact);

@javaClass{org.rascalmpl.library.lang.json.IO}
@deprecated{use readJSON}
public java &T fromJSON(type[&T] typ, str src);

@javaClass{org.rascalmpl.library.lang.json.IO}
@doc{reads JSON values from a stream
In general the translation behaves as follows:
 * Objects translate to map[str,value] by default, unless a node is expected (properties are then translated to keyword fields)
 * Arrays translate to lists by default, or to a set if that is expected or a tuple if that is expected. Arrays may also be interpreted as constructors or nodes (see below)
 * Booleans translate to bools
 * If the expected type provided is a datetime then an int instant is mapped and if a string is found then the dateTimeFormat parameter will be used to configure the parsing of a date-time string
 * If the expected type provided is an ADT then this reader will try to "parse" each object as a constructor for that ADT. It helps if there is only one constructor for that ADT. Positional parameters will be mapped by name as well as keyword parameters.
 * If the expected type provided is a node then it will construct a node named "object" and map the fields to keyword fields.
 * If num, int, real or rat are expected both strings and number values are mapped
 * If loc is expected than strings which look like URI are parsed (containing :/) or a file:/// URI is build, or if an object is found each separate field of
   a location object is read from the respective properties: { scheme : str, authority: str?, path: str?, fragment: str?, query: str?, offset: int, length: int, begin: [bl, bc], end: [el, ec]}
}
java &T readJSON(type[&T] expected, loc src, str dateTimeFormat = "yyyy-MM-dd\'T\'HH:mm:ssZZZZZ", bool lenient=false, bool trackOrigins=false);

@javaClass{org.rascalmpl.library.lang.json.IO}
@doc{parses JSON values from a string
In general the translation behaves as the same as for ((readJSON)).
}
java &T parseJSON(type[&T] expected, str src, str dateTimeFormat = "yyyy-MM-dd\'T\'HH:mm:ssZZZZZ", bool lenient=false, bool trackOrigins=false);

@javaClass{org.rascalmpl.library.lang.json.IO}
java void writeJSON(loc target, value val, bool unpackedLocations=false, str dateTimeFormat="yyyy-MM-dd\'T\'HH:mm:ssZZZZZ", bool dateTimeAsInt=false, int indent=0);

@javaClass{org.rascalmpl.library.lang.json.IO}
java str asJSON(value val, bool unpackedLocations=false, str dateTimeFormat="yyyy-MM-dd\'T\'HH:mm:ssZZZZZ", bool dateTimeAsInt=false, int indent = 0);
