@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module demo::AbstractPico::Uninit

import demo::AbstractPico::AbstractSyntax;
import demo::AbstractPico::Analysis;
import demo::AbstractPico::Controlflow;
import demo::AbstractPico::UseDef;
import demo::AbstractPico::Programs;
import IO;
import Graph;

/*
 * Compute uninitialized variables in a program
 */

public rel[PicoId, ProgramPoint] uninit(PROGRAM P) {
    BLOCK ControlFlow = cflow(P);
    rel[PicoId, ProgramPoint] Uses = uses(P);
    rel[PicoId, ProgramPoint] Defs = defs(P);
    
    rel[PicoId, ProgramPoint] result = {};
    for(<PicoId Id, ProgramPoint PP> <- Uses){
    
        // Compute all program points that can be reached from the root
        // without passing a definition for Id
        set[ProgramPoint] R = reachX(ControlFlow.graph, ControlFlow.entry, Defs[Id]);
        
        // Do a one step extension of R to cater for program points that are both a use
        // and a definition of Id
        R = R + ControlFlow.graph[R - (Defs[Id] - Uses[Id])];
        
        // If the current program point is in R, we have an uninitialized use of Id
        if(PP in R)
           result = result + <Id, PP>;   
    }
    return result;
}

test uninit(annotate(smallUninit)) == {<"x",10>, <"x", 1>, <"s", 5>};
test uninit(annotate(facUninit)) == {<"output", 7>, <"output", 3>};

