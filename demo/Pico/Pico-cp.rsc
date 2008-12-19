module Pico-constant-propagation

import  pico/syntax/Pico;
import  Pico-controlflow;
import  Pico-use-def;

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
                  [| <PICO-ID Id> := <EXP E> |] ~~ S,
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