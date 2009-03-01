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
    rel[PicoId, int] Uses = uses(P);
    rel[PicoId, int] Defs = defs(P);
    
    ProgramEntry = {0};
    CFG = ({0} * ControlFlow.entry) + ControlFlow.graph;

    return {PP | <PicoId Id, ProgramPoint PP> <- Uses,
                 PP in reachX(CFG, ProgramEntry, Defs[Id])
    };
}

public bool test(){

	assertTrue(uninit(smallUninit) == {2});

	assertTrue(uninit(facUninit) == {4});

	return report();
}