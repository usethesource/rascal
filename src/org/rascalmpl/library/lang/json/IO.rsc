@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@doc{
Synopsis: (de)serialization of JSON values. 
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
@reflect{Uses type store}
public java &T fromJSON(type[&T] typ, str src);

