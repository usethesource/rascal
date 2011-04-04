@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module \Pico-constant-propagation

import  pico/syntax/Pico;
import  \Pico-controlflow;
import  \Pico-use-def;

Boolean is_constant(EXP E) {
   switch (E) {
     case <NatCon N>: return true;

     case <StrCon S>: return true;

     case <EXP E>: return false;
   }
}

PROGRAM cp(PROGRAM P) {
    rel[PICO-ID, STATEMENT] Defs = defs(P);
    rel[CP,CP] Pred = cflow(P).graph;

    map[PICO-ID, EXP] replacements = 
      {<Id2 -> E> | STATEMENT S : P,
                  ` <PICO-ID Id> := <EXP E> ` ~~ S,
                  is_constant(E),
                  PICO-ID Id2 : reachX({S},Defs[Id],Pred),
                  Id2 == Id 
      };  
 
    return visit (P) {
     case <PICO-ID Id>: if(<EXP E> ~~ replacements[Id]){
                           insert E;
                        }
    };
}
