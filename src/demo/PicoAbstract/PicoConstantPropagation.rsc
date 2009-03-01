module demo::PicoAbstract::PicoConstantPropagation

import  demo::PicoAbstract::PicoAbstractSyntax;
import  demo::PicoAbstract::PicoControlflow;
import  demo::PicoAbstract::PicoUseDef;
import  demo::PicoAbstract::PicoPrograms;


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
      {Id2 <- E | STATEMENT S <- P,
                 asgStat(PicoId Id, EXP E) := S,
                 is_constant(E),
                 PicoId Id2 <- reachX(Pred, {S}, Defs[Id]),
                 Id2 == Id 
      };  
 
    return visit (P) {
     case id(PicoId Id): if(EXP E := replacements[Id]){
                           insert E;
                        }
    };
}

PROGRAM smallCP =

program([decl("x", natural), decl("s", string)],
        [ asgStat("x", natCon(3))[@pos=1],
          asgStat("d", natCon(1))[@pos=2],
          whileStat(id("x"),
                    [ asgStat("x", sub(id("x"), id("d")))[@pos=4],
                      asgStat("s", conc(id("s"), strCon("#")))[@pos=5]
                    ]
                   )[@pos=3]
        ]
       );