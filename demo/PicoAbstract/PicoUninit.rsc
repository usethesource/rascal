module PicoUninit

import PicoAbstractSyntax;

import PicoControlflow;
import PicoUseDef;
import PicoPrograms;
import UnitTest;
import IO;
import Relation;

set[PicoId] uninit(PROGRAM P) {
    rel[PicoId, EXP] Uses = uses(P);
    rel[PicoId, STATEMENT] Defs = defs(P);
    rel[PicoId, CP] CDefs = { <Id, cp(S)> | 
    CFSEGMENT CFLOW = cflow(P);
    set[CP] Root = CFLOW.entry;
    rel[CP,CP] Pred = CFLOW.graph; 

    return {Id | <PicoId Id, EXP E> <- Uses,
                 E in reachX(Pred, Root, Defs[Id])
    };
}

public bool test(){

ui = uninit(facUninit);
println("ui=<ui>");
return true;
}