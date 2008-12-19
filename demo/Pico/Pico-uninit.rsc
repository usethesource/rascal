module Pico-uninit

import pico/syntax/Pico;

import Pico-controlflow;
import Pico-use-def;

set[PICO-ID] uninit(PROGRAM P) {
    rel[EXP,PICO-ID] Uses = uses(P);
    rel[PICO-ID, STATEMENT] Defs = defs(P);
    CFSEGMENT CFLOW = cflow(P);
    set[CP] Root = CFLOW.entry;
    rel[CP,CP] Pred = CFLOW.graph; 

    return {Id | <EXP E, PICO-ID Id> : Uses,
                 E in reachX(Root, Defs[Id], Pred)
    };
}
