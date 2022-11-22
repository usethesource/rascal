@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Jurgen.Vinju@cwi.nl - CWI}

module lang::aut::IO

@synopsis{Read relations from an AUT file.}
@description{

An AUT file contains tuples of ternary relation as lines with the following format:
* `(<int>,<str>,<int>)`
*  each field is separated by a comma 

readAUT takes an AUT file and generates a value of type `rel[int, str,int]`.
}
@javaClass{org.rascalmpl.library.lang.aut.IO}
public java rel[int, str, int] readAUT(str nameAUTFile);

@doc{write an AUT file}
@javaClass{org.rascalmpl.library.lang.aut.IO}
public java void writeAUT(str nameAUTFile, rel[int, str, int] r);

