module demo::lang::Pico::UseDef

import Prelude;
import demo::lang::Pico::Abstract;


public rel[PicoId, EXP] uses(PROGRAM P) {
  rel[PicoId, EXP] r = {};
  top-down-break visit(P.stats){
      case EXP Exp: r += {<Id, Exp> | /PicoId Id <- Exp};
  }
  return r;
}
  
public rel[PicoId, STATEMENT] defs(PROGRAM P) =
  { < Id, Stat > | /Stat: asgStat(PicoId Id, EXP Exp) <- P.stats};