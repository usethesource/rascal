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
@synopsis{Maps any Rascal value to a JSON string}
@deprecated{use ((writeJSON))}
public java str toJSON(value v);

@javaClass{org.rascalmpl.library.lang.json.IO}
@synopsis{Maps any Rascal value to a JSON string, optionally in compact form.}
@deprecated{use ((asJSON))}
public java str toJSON(value v, bool compact);

@javaClass{org.rascalmpl.library.lang.json.IO}
@deprecated{use ((readJSON))}
@synopsis{Parses a JSON string and maps it to the requested type of Rascal value.}
public java &T fromJSON(type[&T] typ, str src);

@javaClass{org.rascalmpl.library.lang.json.IO}
@synopsis{reads JSON values from a stream}
@description{
In general the translation behaves as follows:
 * Objects translate to map[str,value] by default, unless a node is expected (properties are then translated to keyword fields)
 * Arrays translate to lists by default, or to a set if that is expected or a tuple if that is expected. Arrays may also be interpreted as constructors or nodes (see below)
 * Booleans translate to bools
 * If the expected type provided is a datetime then an int instant is mapped and if a string is found then the dateTimeFormat parameter will be used to configure the parsing of a date-time string
 * If the expected type provided is an ADT then this reader will try to "parse" each object as a constructor for that ADT. It helps if there is only one constructor for that ADT. Positional parameters will be mapped by name as well as keyword parameters.
 * If the expected type provided is a node then it will construct a node named "object" and map the fields to keyword fields.
 * If num, int, real or rat are expected both strings and number values are mapped
 * If loc is expected than strings which look like URI are parsed (containing :/) or a file:/// URI is build, or if an object is found each separate field of
   a location object is read from the respective properties: { scheme : str, authority: str?, path: str?, fragment: str?, query: str?, offset: int, length: int, begin: [bl, bc], end: [el, ec]}}
java &T readJSON(type[&T] expected, loc src, str dateTimeFormat = "yyyy-MM-dd\'T\'HH:mm:ssZZZZZ", bool lenient=false, bool trackOrigins=false, bool explicitConstructorNames=false, bool explicitDataTypes=false);

@javaClass{org.rascalmpl.library.lang.json.IO}
@synopsis{parses JSON values from a string
In general the translation behaves as the same as for ((readJSON)).}
java &T parseJSON(type[&T] expected, str src, str dateTimeFormat = "yyyy-MM-dd\'T\'HH:mm:ssZZZZZ", bool lenient=false, bool trackOrigins=false, bool explicitConstructorNames=false, bool explicitDataTypes=false);

@javaClass{org.rascalmpl.library.lang.json.IO}
@synopsis{Serializes a value as a JSON string and stream it}
@description{
This function tries to map Rascal values to JSON values in a natural way.
In particular it tries to create a value that has the same number of recursive levels,
such that one constructor maps to one object. The serialization is typically _lossy_ since
JSON values by default do not explicitly encode the class or constructor while Rascal data types do.

If you need the names of constructors or data-types in your result, then use the parameters:
* `explicitConstructorNames=true` will store the name of every constructor in a field `_constructor`
* `explicitDataTypes=true` will store the name of the ADT in a field called `_type`

The `dateTimeFormat` parameter dictates how `datetime` values will be printed.

The `unpackedLocations` parameter will produce an object with many fields for every property of a `loc` value, but
if set to false a `loc` will be printed as a string.
}
@pitfalls{
* It is understood that Rascal's number types have arbitrary precision, but this is not supported by the JSON writer.
As such when an `int` is printed that does not fit into a JVM `long`, there will be truncation to the lower 64 bits.
For `real` numbers that are larger than JVM's double you get "negative infinity" or "positive infinity" as a result.
}
java void writeJSON(loc target, value val, bool unpackedLocations=false, str dateTimeFormat="yyyy-MM-dd\'T\'HH:mm:ssZZZZZ", bool dateTimeAsInt=false, int indent=0, bool dropOrigins=true, bool explicitConstructorNames=false, bool explicitDataTypes=false);

@javaClass{org.rascalmpl.library.lang.json.IO}
@synopsis{Serializes a value as a JSON string and stores it as a string}
@description{
This function uses `writeJSON` and stores the result in a string.
}
java str asJSON(value val, bool unpackedLocations=false, str dateTimeFormat="yyyy-MM-dd\'T\'HH:mm:ssZZZZZ", bool dateTimeAsInt=false, int indent = 0, bool dropOrigins=true, bool explicitConstructorNames=false, bool explicitDataTypes=false);
