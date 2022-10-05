module demo::lang::Pico::Uninit

import demo::lang::Pico::Abstract;
import demo::lang::Pico::Load;

import demo::lang::Pico::UseDef;
import demo::lang::Pico::ControlFlow;

import analysis::graphs::Graph;

set[CFNode] defNodes(PicoId Id, set[Occurrence] Defs) =
   {statement(occ.stat.src, occ.stat) | Occurrence occ <- Defs, occ.name == Id};

set[Occurrence] uninitProgram(PROGRAM P) {
   // highlight-start
   D = defs(P); 
   CFG = cflowProgram(P); 
   return { occ | occ <- uses(P), 
                  any(CFNode N <- reachX(CFG.graph, CFG.entry, defNodes(occ.name, D)),
                      N has location && occ.src <= N.location) 
          }; 
   // highlight-end
}

set[Occurrence] uninitProgram(str txt) = uninitProgram(load(txt)); 
