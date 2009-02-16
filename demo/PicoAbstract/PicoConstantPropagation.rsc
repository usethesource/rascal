module PicoConstantPropagation

import  PicoAbstractSyntax;
import  PicoControlflow;
import  PicoUseDef;

bool is_constant(EXP E) {
   switch (E) {
     case natCon(int N): return true;

     case strCon(str S): return true;

     case EXP E: return false;
   }
}

PROGRAM cp(PROGRAM P) {
    rel[PicoId, STATEMENT] Defs = defs(P);
    rel[CP,CP] Pred = cflow(P).graph;

    map[PicoId, EXP] replacements = 
      {Id2 : E | STATEMENT S : P,
                 asgStat(PicoId Id, EXP E) := S,
                 is_constant(E),
                 PicoId Id2 : reachX(Pred, {S},Defs[Id]),
                 Id2 == Id 
      };  
 
    return visit (P) {
     case id(PicoId Id): if(EXP E := replacements[Id]){
                           insert E;
                        }
    };
}