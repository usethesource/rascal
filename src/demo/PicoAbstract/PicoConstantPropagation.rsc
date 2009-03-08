module demo::PicoAbstract::PicoConstantPropagation

import  demo::PicoAbstract::PicoAbstractSyntax;
import  demo::PicoAbstract::PicoControlflow;
import  demo::PicoAbstract::PicoUseDef;
import  demo::PicoAbstract::PicoPrograms;
import Graph;
import IO;


bool is_constant(EXP E) {
   switch (E) {
     case natCon(int N): return true;

     case strCon(str S): return true;

     case EXP E: return false;
   }
}

PROGRAM constantPropagation(PROGRAM P) {
    rel[PicoId, int] Defs = defs(P);
    rel[ProgramPoint,ProgramPoint] CFG = cflow(P).graph;

    map[PicoId, EXP] replacements = 
      (Id2 : E | STATEMENT S <- P,
                 asgStat(PicoId Id, EXP E) := S,
                 is_constant(E),
                 PicoId Id2 <- reachX(CFG, {S@pos}, Defs[Id]),
                 Id2 == Id 
      );  
      
      println("replacements=<replacements>");
 
    return visit (P) {
     case id(PicoId Id): if(replacements[Id]? && EXP E := replacements[Id]){
                           insert E;
                        }
    };
}

PROGRAM smallCP =

program([decl("x", natural), decl("s", string)],
        [ asgStat("x", natCon(3))[@pos=1],
          asgStat("d", id("x"))[@pos=2] //,
          //whileStat(id("x"),
          //          [ asgStat("x", sub(id("x"), id("d")))[@pos=4],
           //           asgStat("s", conc(id("s"), strCon("#")))[@pos=5]
           //         ]
           //        )[@pos=3]
        ]//
       );
       
public bool test(){
  P = constantPropagation(smallCP);
  println("P=<P>");
  return true;
}