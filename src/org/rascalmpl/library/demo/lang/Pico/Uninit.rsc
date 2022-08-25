// tag::module[]
module demo::lang::Pico::Uninit

import demo::lang::Pico::Abstract;
import demo::lang::Pico::Load;

import demo::lang::Pico::UseDef;
import demo::lang::Pico::ControlFlow;

import analysis::graphs::Graph;

public set[CFNode] defNodes(PicoId Id, set[Occurrence] Defs) =
   {statement(occ.stat@location, occ.stat) | Occurrence occ <- Defs, occ.name == Id};

public set[Occurrence] uninitProgram(PROGRAM P) {
   D = defs(P); // <1>
   CFG = cflowProgram(P); // <2>
   return { occ | occ <- uses(P), // <3>
                  any(CFNode N <- reachX(CFG.graph, CFG.entry, defNodes(occ.name, D)),
                      N has location && occ.location <= N.location) 
          }; // <4>
}

public set[Occurrence] uninitProgram(str txt) = uninitProgram(load(txt)); // <5>
// end::module[]
