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


module lang::json::IO

@doc{write an JSon Term to a text file}
@javaClass{org.rascalmpl.library.lang.json.IO}
@reflect{Uses URI Resolver Registry}
public java void writeTextJSonFile(loc location, value v);


@doc{read an ATerm from a text file}
@javaClass{org.rascalmpl.library.lang.json.IO}
@reflect{Uses URI Resolver Registry}
public java &T readTextJSonFile(type[&T] begin, loc location);

public value readTextJSonFile(loc location) {
  return readTextJSonFile(#value, location);
}
