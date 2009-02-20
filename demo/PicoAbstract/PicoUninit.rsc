module PicoUninit

import PicoAbstractSyntax;
import PicoAnalysis;
import PicoControlflow;
import PicoUseDef;
import PicoPrograms;
import UnitTest;
import IO;
import Graph;

set[ProgramPoint] uninit(PROGRAM P) {
    rel[PicoId, ProgramPoint] Uses = uses(P);
    rel[PicoId, ProgramPoint] Defs = defs(P);
    BLOCK ControlFlow = cflow(P);
    
    println("Uses=<Uses>\nDefs=<Defs>\nControlFlow=<ControlFlow>");
    
    R= ControlFlow.entry;
    G=ControlFlow.entry;
    
    println("R=<R>\nG=<G>");
    dx = Defs["x"]; ux = Uses["x"];
    ds = Defs["s"]; uy = Uses["s"];
    
    println("dx=<dx>\nux=<ux>");
    println("ds=<ds>\nus=<us>");

    return {PP | <PicoId Id, ProgramPoint PP> <- Uses,
                 PP in reachX(ControlFlow.graph, ControlFlow.entry, Defs[Id])
    };
}

public bool test(){

ui = uninit(smallUninit);
println("ui=<ui>");
return true;
}