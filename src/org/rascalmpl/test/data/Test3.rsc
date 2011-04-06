@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module Test3
import zoo::pico::syntax::Main;

str f(str s) { return s; }

public str t(){
    m = f("abc");        // <--- gaat altijd goed
    n = 10;
    m = f("def<n>");     // <--- gaat fout bij import pico syntax
    return m;
}
