@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module ValueIO



@doc{Read  a value from a binary file in PBF format}
public value readValueFile(loc file) {
  return readBinaryValueFile(#value, file);
}

@doc{Get length in bytes of a file.}
@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses URI Resolver Registry}
public java int getFileLength(loc file);

@doc{Read a typed value from a binary file.}
@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses URI Resolver Registry}
public java &T readBinaryValueFile(type[&T] result, loc file);

public value readBinaryValueFile(loc file) {
  return readBinaryValueFile(#value, file);
}

@doc{Read a typed value from a text file.}
@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses URI Resolver Registry}
public java &T readTextValueFile(type[&T] result, loc file);

public value readTextValueFile(loc file) {
  return readTextValueFile(#value, file);
}

@doc{Parse a textual string representation of a value}
public value readTextValueString(str input) {
  return readTextValueString(#value, input);
}

@doc{Parse a textual string representation of a value and validate it against the given type}
@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses TypeStore from environment}
public java &T readTextValueString(type[&T] result, str input);
	
@doc{Write a value to a file using an efficient binary file format}
@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses URI Resolver Registry}
public java void writeBinaryValueFile(loc file, value val, bool compression = true);
	
@doc{Write a value to a file using a textual file format}
@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses URI Resolver Registry}
public java void writeTextValueFile(loc file, value val);
