module demo::PicoAbstract::PicoUninit

import demo::PicoAbstract::PicoAbstractSyntax;
import demo::PicoAbstract::PicoAnalysis;
import demo::PicoAbstract::PicoControlflow;
import demo::PicoAbstract::PicoUseDef;
import demo::PicoAbstract::PicoPrograms;
import UnitTest;
import IO;
import Graph;

set[int] uninit(PROGRAM P) {
    BLOCK ControlFlow = cflow(P);
    rel[PicoId, ProgramPoint] Uses = uses(P);
    rel[PicoId, ProgramPoint] Defs = defs(P);
    
    ProgramEntry = {0};
    CFG = ({0} * ControlFlow.entry) + ControlFlow.graph;

    return {PP | <PicoId Id, ProgramPoint PP> <- Uses,
                 PP in reachX(CFG, ProgramEntry, Defs[Id])
    };
}

public bool test(){
    println("Start ...");
    U = uninit(annotate(smallUninit)) ;
    println(U);
	assertTrue(uninit(annotate(smallUninit)) == {2});
	
	  U = uninit(annotate(facUninit)) ;
    println(U);

	assertTrue(uninit(annotate(facUninit)) == {4});

	return report("PicoUninit");
}