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
@description{
The pairs ((asJSON)):((parseJSON)) and ((writeJSON)):((readJSON)) are both bi-directional
transformations between serializable Rascal values (all except function instances) and JSON strings.
The ((asJSON)) and ((parseJSON)) work on `str` representations, while ((writeJSON)) and ((readJSON)) 
stream to/from files directly.

The basic principle of the bi-directional mapping is that constructors of algebraic data-types
map one-to-one to JSON object notation, and vice versa. The other builtin Rascal data-structures
are judiciously mapped to objects and arrays, and strings, etc. The goal is that their representation
is natural on the receiving end (e.g. TypeScript, Javascript and Python code), without sacrificing
on the naturalness of the Rascal representation.
}
@pitfalls{
* ((asJSON)) and ((writeJSON)) are not isomorphisms. They are homomorphisms that choose
JSON arrays or JSON objects for multiple different kinds of Rascal values. For example 
maps and nodes and ADT's are all mapped to JSON object notation (homonyms). 
}
@benefits{
* Using the `expected`` type arguments of ((parseJSON)) and ((readJSON)) the homonyms created by ((asJSON)) and ((writeJSON)) can be converted back to their
original Rascal structures. If the expected type contains only _concrete types_, and no _abstract types_ then
then pairs ((asJSON))/((parseJSON)) and ((writeJSON))/((readJSON)) are isomorphic.
   * The _abstract types_ are `value`, `node`, `num` or any composite type that contains it.
   * The _concrete types_ are all types which are not _abstract types_.
* If you provide `value` or `node` as an expected type, you will always get a useful representation
on the Rascal side. It is not guaranteed to be the same representation as before.
}
module lang::json::IO

import util::Maybe;
import Exception;

@synopsis{JSON parse errors have more information than general parse errors}
@description{
* `location` is the place where the parsing got stuck (going from left to right).
* `cause` is a factual diagnosis of what was expected at that position, versus what was found.
* `path` is a path query string into the JSON value from the root down to the leaf where the error was detected.
}
data RuntimeException(str cause="", str path="");

private str DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd\'T\'HH:mm:ssZ";

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
   a location object is read from the respective properties: { scheme : str, authority: str?, path: str?, fragment: str?, query: str?, offset: int, length: int, begin: [bl, bc], end: [el, ec]}
* Go to ((JSONParser)) to find out how to use the optional `parsers` parameter.
* if the parser finds a `null` JSON value, it will lookup in the `nulls` map based on the currently expected type which value to return, or throw an exception otherwise.
First the expected type is used as a literal lookup, and then each value is tested if the current type is a subtype of it. 
}
java &T readJSON(
  type[&T] expected, 
  loc src, 
  str dateTimeFormat = DEFAULT_DATETIME_FORMAT, 
  bool lenient=false, 
  bool trackOrigins=false,
  JSONParser[value] parser = (type[value] _, str _) { throw ""; },
  map [type[value] forType, value nullValue] nulls = defaultJSONNULLValues,
  bool explicitConstructorNames = false,
  bool explicitDataTypes = false
);

public map[type[value] forType, value nullValue] defaultJSONNULLValues = (
  #Maybe[value]     : nothing(), 
  #node             : "null"(), 
  #int              : -1, 
  #real             : -1.0, 
  #rat              : -1r1, 
  #value            : "null"(), 
  #str              : "", 
  #list[value]      : [], 
  #set[value]       : {}, 
  #map[value,value] : (),
  #loc              : |unknown:///|
);

@javaClass{org.rascalmpl.library.lang.json.IO}
@synopsis{parses JSON values from a string.
In general the translation behaves as the same as for ((readJSON)).}
java &T parseJSON(
  type[&T] expected, 
  str src, 
  str dateTimeFormat = DEFAULT_DATETIME_FORMAT, 
  bool lenient=false, 
  bool trackOrigins=false, 
  JSONParser[value] parser = (type[value] _, str _) { throw ""; },
  map[type[value] forType, value nullValue] nulls = defaultJSONNULLValues,
  bool explicitConstructorNames = false,
  bool explicitDataTypes = false
);

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
* Check out ((JSONFormatter)) on how to use the `formatters` parameter
* The `dateTimeFormat` parameter dictates how `datetime` values will be printed.
* The `unpackedLocations` parameter will produce an object with many fields for every property of a `loc` value, but
if set to false a `loc` will be printed as a string.
}
@pitfalls{
* It is understood that Rascal's number types have arbitrary precision, but this is not supported by the JSON writer.
As such when an `int` is printed that does not fit into a JVM `long`, there will be truncation to the lower 64 bits.
For `real` numbers that are larger than JVM's double you get "negative infinity" or "positive infinity" as a result.
}
java void writeJSON(loc target, value val, 
  bool unpackedLocations=false, 
  str dateTimeFormat=DEFAULT_DATETIME_FORMAT, 
  bool dateTimeAsInt=false, 
  int indent=0, 
  bool dropOrigins=true, 
  JSONFormatter[value] formatter = str (value _) { fail; }, 
  bool explicitConstructorNames=false, 
  bool explicitDataTypes=false
);

@javaClass{org.rascalmpl.library.lang.json.IO}
@synopsis{Does what ((writeJSON)) does but serializes to a string instead of a location target.}
@synopsis{Serializes a value as a JSON string and stores it as a string}
@description{
This function uses `writeJSON` and stores the result in a string.
}
java str asJSON(value val, bool unpackedLocations=false, str dateTimeFormat=DEFAULT_DATETIME_FORMAT, bool dateTimeAsInt=false, int indent = 0, bool dropOrigins=true, JSONFormatter[value] formatter = str (value _) { fail; }, bool explicitConstructorNames=false, bool explicitDataTypes=false);

@synopsis{((writeJSON)) and ((asJSON)) uses `Formatter` functions to flatten structured data to strings, on-demand}
@description{
A JSONFormatter can be passed to the ((writeJSON)) and ((asJSON)) functions. When/if the type matches an algebraic data-type
to be serialized, then it is applied and the resulting string is serialized to the JSON stream instead of the structured data.

The goal of JSONFormat and its dual JSONParser is to bridge the gap between string-based JSON encodings and typical
Rascal algebraic combinators.
}
alias JSONFormatter[&T] = str (&T);

@synopsis{((readJSON)) and ((parseJSON)) use JSONParser functions to turn unstructured data into structured data.}
@description{
A parser JSONParser can be passed to ((readJSON)) and ((parseJSON)). When the reader expects an algebraic data-type
or a syntax type, but the input at that moment is a JSON string, then the parser is called on that string (after string.trim()).

The resulting data constructor is put into the resulting value instead of a normal string.

The goal of JSONParser and its dual JSONFormatter is to bridge the gap between string-based JSON encodings and typical
Rascal algebraic combinators.
}
@benefits{
* Use parsers to create more structure than JSON provides.
}
@pitfalls{
* The `type[&T]` argument is called dynamically by the JSON reader; it does not contain the
grammar. It does encode the expected type of the parse result.
* The expected types can only be `data` types, not syntax types. 
}
alias JSONParser[&T] = &T (type[&T], str);