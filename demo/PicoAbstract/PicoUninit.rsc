module PicoUninit

import PicoAbstractSyntax;

import PicoControlflow;
import PicoUseDef;

set[PICO-ID] uninit(PROGRAM P) {
    rel[EXP,PICO-ID] Uses = uses(P);
    rel[PicoId, STATEMENT] Defs = defs(P);
    CFSEGMENT CFLOW = cflow(P);
    set[CP] Root = CFLOW.entry;
    rel[CP,CP] Pred = CFLOW.graph; 

    return {Id | <EXP E, PicoId Id> : Uses,
                 E in reachX(Root, Defs[Id], Pred)
    };
}