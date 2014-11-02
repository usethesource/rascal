module demo::lang::Pico::CommonSubExpr

import Prelude;
import analysis::graphs::Graph;
import demo::lang::Pico::Abstract;
import demo::lang::Pico::Load;

import demo::lang::Pico::UseDef;
import demo::lang::Pico::ControlFlow;

PROGRAM cse(PROGRAM P){
  D = defs(P);
  pred = cflowProgram(P).graph;
  replacements = (Exp2 : Id |  / Stat:asgStat(PicoId Id, EXP Exp) <- P,
                               !/id(Id) := Exp,
                               EXP Exp2 <- reachX(pred, {Stat}, D[Id]) );
                               
  return visit (P) {
           case EXP Exp => id(replacements[Exp])  when replacements[Exp]?
  }              
}

public PROGRAM cse(str txt) = cse(load(txt)); 
