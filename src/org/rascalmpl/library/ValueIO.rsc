@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}

@synopsis{

Library functions for reading and writing values in textual and binary format.
}
module ValueIO

import Type;


@synopsis{

Read  a value from a binary file in PBF format.
}
public value readValueFile(loc file) {
  return readBinaryValueFile(#value, file);
}


@synopsis{

Get length of a file in number of bytes.
}
@javaClass{org.rascalmpl.library.Prelude}
public java int getFileLength(loc file);


@synopsis{

Read a typed value from a binary file.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T readBinaryValueFile(type[&T] result, loc file);

public value readBinaryValueFile(loc file) {
  return readBinaryValueFile(#value, file);
}


@synopsis{

Read a typed value from a text file.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T readTextValueFile(type[&T] result, loc file);

public value readTextValueFile(loc file) {
  return readTextValueFile(#value, file);
}


@synopsis{

If you have written a file containing reified types, then you can use this function
  to read them back.
}
public &T readTextValueFileWithEmbeddedTypes(type[&T] result, loc file) {
  return readTextValueFile(type(result.symbol, result.definitions + #Symbol.definitions + #Production.definitions), file);
}


@synopsis{

Parse a textual string representation of a value.
}
public value readTextValueString(str input) {
  return readTextValueString(#value, input);
}


@synopsis{

Parse a textual string representation of a value and validate it against the given type.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T readTextValueString(type[&T] result, str input);
	

@synopsis{

Write a value to a file using an efficient binary file format.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void writeBinaryValueFile(loc file, value val, bool compression = true);
	

@synopsis{

Write a value to a file using a textual file format.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void writeTextValueFile(loc file, value val);
