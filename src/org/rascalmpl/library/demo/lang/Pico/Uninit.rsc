module demo::lang::Pico::Uninit

import Prelude;
import demo::lang::Pico::Abstract;
import demo::lang::Pico::Load;

import demo::lang::Pico::UseDef;
import demo::lang::Pico::ControlFlow;

set[PicoId] uninit(PROGRAM P) {
   U = uses(P);
   D = defs(P);
   CFG = cflowProgram(P);
   root = CFG.entry;
   pred = CFG.graph;
   return {Id | <PicoId Id, EXP E> <- U, exp(E) in reachX(pred, root, {stat(S) | S <- D[Id]}) };
}

public set[PicoId] uninit(str txt) = uninit(load(txt)); 
