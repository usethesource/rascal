module PicoUseDef

import PicoAbstractSyntax;

rel[PicoId, EXP] uses(PROGRAM P) {
  return {<Id, E> | id(PicoId Id) : P};
}

rel[PicoId, STATEMENT] defs(PROGRAM P) { 
  return {<Id, S> | STATEMENT S : P, asgStat(PicoId Id, EXP Exp) := S};
}