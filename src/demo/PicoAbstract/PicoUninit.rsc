module demo::PicoAbstract::PicoUninit

import demo::PicoAbstract::PicoAbstractSyntax;
import demo::PicoAbstract::PicoAnalysis;
import demo::PicoAbstract::PicoControlflow;
import demo::PicoAbstract::PicoUseDef;
import demo::PicoAbstract::PicoPrograms;
import UnitTest;
import IO;
import Graph;

/*
 * Compute uninitialized variables in a program
 */

rel[PicoId, ProgramPoint] uninit(PROGRAM P) {
    BLOCK ControlFlow = cflow(P);
    rel[PicoId, ProgramPoint] Uses = uses(P);
    rel[PicoId, ProgramPoint] Defs = defs(P);
    
    rel[PicoId, ProgramPoint] result = {};
    for(<PicoId Id, ProgramPoint PP> <- Uses){
    
        // Compute all program points that can be reached from the root
        // without passing a definition for Id
        set[ProgramPoint] R := reachX(ControlFlow.graph, ControlFlow.entry, Defs[Id]);
        
        // Do a one step extension of R to cater for program points that are both a use
        // and a definition of Id
        R = R + ControlFlow.graph[R - (Defs[Id] - Uses[Id])];
        
        // If the current program point is in R, we have an uninitialized use of Id
        if(PP in R)
           result = result + <Id, PP>;   
    }
    return result;
}

public bool test(){
   
	assertEqual(uninit(annotate(smallUninit)), {<"x",3>, <"x", 6>, <"s", 1>});
	
	assertEqual(uninit(annotate(facUninit)), {<"output", 5>, <"output", 13>});

	return report("PicoUninit");
}