module Pico-common-subexpression

import pico/syntax/Pico;
import Pico-controlflow;
import Pico-use-def;

PROGRAM cse(PROGRAM P) {
    rel[PICO-ID, STATEMENT] Defs = defs(P);
    rel[CP,CP] Pred = cflow(P).graph;
    map[EXP, PICO-ID] replacements = 
       {<E2 -> Id> | STATEMENT S : P,
                   [| <PICO-ID Id> := <EXP E> |] ~~ S,
                   Id notin E,
                   EXP E2 : reachX({S}, Defs[Id], Pred)
       };
  
    return visit (P) {
      case <EXP E>: if([| <PICO-ID Id> |] ~~ replacements(E)){
                       replace-by Id;
                    }
    };
}