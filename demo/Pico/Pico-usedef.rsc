module Pico-use-def

import pico/syntax/Pico;

rel[PICO-ID, EXP] uses(PROGRAM P) {
  return {<Id, E> | EXP E : P, [| <PICO-ID Id> |] ~~ E};
}

rel[PICO-ID, STATEMENT] defs(PROGRAM P) { 
  return {<Id, S> | STATEMENT S : P, 
                    [| <PICO-ID Id> := <EXP Exp> |] ~~ S};
}