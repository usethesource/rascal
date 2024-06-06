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


module lang::aterm::IO

@synopsis{read an ATerm from a text file}
@javaClass{org.rascalmpl.library.lang.aterm.IO}
public java &T readTextATermFile(type[&T] begin, loc location);

public value readTextATermFile(loc location) {
  return readTextATermFile(#value, location);
}


@synopsis{Read an ATerm from a named file.}

@javaClass{org.rascalmpl.library.lang.aterm.IO}
public java value readATermFromFile(str fileName);

@synopsis{write an ATerm to a text file}
@javaClass{org.rascalmpl.library.lang.aterm.IO}
public java void writeTextATermFile(loc location, value v);
