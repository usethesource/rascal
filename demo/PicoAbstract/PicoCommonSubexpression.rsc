module PicoCommonSubexpression

import PicoAbstractSyntax;
import PicoControlflow;
import PicoUseDef;

PROGRAM cse(PROGRAM P) {
    rel[PicoId, STATEMENT] Defs = defs(P);
    rel[CP,CP] Pred = cflow(P).graph;
    map[EXP, PicoId] replacements = 
       {E2 : Id | STATEMENT S <- P,
                  asgStat(PicoId Id, EXP E) :=  S,
                  Id notin E,
                  EXP E2 <- reachX(Pred, {S}, Defs[Id])
       };
  
    return visit (P) {
      case EXP E: if(PICO-ID Id := replacements[E]){
                       insert Id;
                    }
    };
}