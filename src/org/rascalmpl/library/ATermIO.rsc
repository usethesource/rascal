@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module ATermIO

@doc{read an ATerm from a text file}
@javaClass{org.rascalmpl.library.ATermIO}
@reflect{Uses URI Resolver Registry}
public &T java readTextATermFile(type[&T] start, loc location);

public value readTextATermFile(loc location) {
  return readTextATermFile(#value, location);
}

@doc{write an ATerm to a text file}
@javaClass{org.rascalmpl.library.ATermIO}
@reflect{Uses URI Resolver Registry}
public void java writeTextATermFile(loc location, value v);
