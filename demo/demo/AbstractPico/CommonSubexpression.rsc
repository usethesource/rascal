module demo::AbstractPico::CommonSubexpression

import demo::AbstractPico::AbstractSyntax;
import demo::AbstractPico::Controlflow;
import demo::AbstractPico::UseDef;

PROGRAM cse(PROGRAM P) {
    rel[PicoId, STATEMENT] Defs = defs(P);
    rel[CP,CP] Pred = cflow(P).graph;
    map[EXP, PicoId] replacements = 
       (E2 : Id | STATEMENT S <- P,
                  asgStat(PicoId Id, EXP E) :=  S,
                  Id notin E,
                  EXP E2 <- reachX(Pred, {S}, Defs[Id])
       );
  
    return visit (P) {
      case EXP E: if(PicoId Id := replacements[E]){
                       insert Id;
                    }
    };
}

public bool test(){
	return true;
}