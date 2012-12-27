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
module HalloTest

import languages::pico::syntax::Pico;
import IO;

public Tree simple = 
begin
  declare
    x: natural,
    s: string;

  x := 10;
  while x do
    x := x - 1;
    s := s || "#"
  od
end;



public bool test() {
  if ([| begin declare <decls>; <stats> end |] := simple) {
    println("decls: <decls>");
    println("stats: <stats>");
    return true;
  }
  return false;
}
