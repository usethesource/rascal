@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module \Pico-common-subexpression

import pico/syntax/Pico;
import \Pico-controlflow;
import \Pico-use-def;

PROGRAM cse(PROGRAM P) {
    rel[PICO-ID, STATEMENT] Defs = defs(P);
    rel[CP,CP] Pred = cflow(P).graph;
    map[EXP, PICO-ID] replacements = 
       {<E2 -> Id> | STATEMENT S : P,
                   ` <PICO-ID Id> := <EXP E> ` ~~ S,
                   Id notin E,
                   EXP E2 : reachX({S}, Defs[Id], Pred)
       };
  
    return visit (P) {
      case <EXP E>: if(` <PICO-ID Id> ` ~~ replacements(E)){
                       replace-by Id;
                    }
    };
}
