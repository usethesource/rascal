@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::AbstractPico::CommonSubexpression

import demo::AbstractPico::AbstractSyntax;
import demo::AbstractPico::Controlflow;
import demo::AbstractPico::UseDef;

PROGRAM cse(PROGRAM P) {
    rel[PicoId, STATEMENT] Defs = defs(P);
    rel[CP,CP] Pred = cflow(P).graph;
    map[EXP, PicoId] replacements = 
       (E2 : Id | STATEMENT S <- P,
                  asgStat(PicoId Id, EXP E) :=  S,
                  Id notin E,
                  EXP E2 <- reachX(Pred, {S}, Defs[Id])
       );
  
    return visit (P) {
      case EXP E: if(PicoId Id := replacements[E]){
                       insert Id;
                    }
    };
}
