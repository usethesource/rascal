module demo::lang::Pico::Uninit

import Prelude;
import demo::lang::Pico::Abstract;
import demo::lang::Pico::Load;

import demo::lang::Pico::UseDef;
import demo::lang::Pico::ControlFlow;

public set[Occurrence[PicoId]] uninitProgram(PROGRAM P) {
   D = defs(P);
   CFG = cflowProgram(P);
   return { occ | occ <- uses(P), 
                  any(CFNode N <- reachX(CFG.graph, CFG.entry, rangeR(D, {occ.item})), N has location && occ.location <= N.location)
          };
}

public set[Occurrence[PicoId]] uninitProgram(str txt) = uninitProgram(load(txt)); 
