module demo::PicoAbstract::PicoConstantPropagation

import  demo::PicoAbstract::PicoAbstractSyntax;
import  demo::PicoAbstract::PicoAnalysis;

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

STATEMENT replaceVar(PicoId Id, EXP E, STATEMENT S){
  return visit(S){
    case id(Id) => E
  };
}

PROGRAM constantPropagation(PROGRAM P) {
    rel[PicoId, ProgramPoint] Defs = defs(P);
    rel[PicoId, ProgramPoint] Uses = uses(P);
    rel[ProgramPoint,ProgramPoint] CFG = cflow(P).graph;

    println("CFG=<CFG>");
    println("Defs=<Defs>");
    rel[PicoId, set[ProgramPoint], EXP] replacements = 
      {<Id, CU, E> | STATEMENT S <- P,
                 asgStat(PicoId Id, EXP E) := S,
                 is_constant(E),
                 CU := reachX(CFG, {S@pos}, Defs[Id] - S@pos) & Uses[Id]
      };  
      
      println("replacements=<replacements>");
 /*
    return visit (P) {
     case STATEMENT S: if(S@pos in range(replacements))
    };
    */
    return P;
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