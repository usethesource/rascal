module demo::lang::Pico::Uninit

import Prelude;
import demo::lang::Pico::Abstract;
import demo::lang::Pico::Load;

import demo::lang::Pico::UseDef;
import demo::lang::Pico::ControlFlow;

public alias Occurrence = tuple[str id, loc where];

public set[Occurrence] uninit(PROGRAM P) {
   U = uses(P);
   D = defs(P);
   CFG = cflowProgram(P);
   root = CFG.entry;
   pred = CFG.graph;
   //println("U = <U>,\n D = <D>,\n CFG = <CFG>,\n root = <root>,\n pred = <pred>");
   return {<Id, E@location> | <PicoId Id, EXP E> <- U, exp(E) in reachX(pred, root, {stat(S) | S <- D[Id]}) };
}

public set[Occurrence] uninitProgram(str txt) = uninit(load(txt)); 
