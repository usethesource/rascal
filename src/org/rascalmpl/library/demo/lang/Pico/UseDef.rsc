module demo::lang::Pico::UseDef

import Prelude;
import demo::lang::Pico::Abstract;

public set[Occurrence[PicoId]] uses(PROGRAM P) {       /*1*/
  set[Occurrence[PicoId]] r = {};                      /*2*/
  visit(P.stats){                                      /*3*/
      case u:id(PicoId Id): r += {< u@location, Id >}; /*4*/
  }
  return r;                                            /*5*/
}
  
public set[Occurrence[PicoId]] defs(PROGRAM P) =                     /*6*/
  { < Exp@location, Id > | /asgStat(PicoId Id, EXP Exp) <- P.stats}; /*7*/