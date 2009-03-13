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

PROGRAM constantPropagation(PROGRAM P) {
    rel[PicoId, ProgramPoint] Defs = defs(P);
    rel[ProgramPoint,ProgramPoint] CFG = cflow(P).graph;

    println("CFG=<CFG>\nDefs=<Defs>");
    
    rel[ProgramPoint, PicoId, EXP] replacements = {};
    
    for(STATEMENT S <- P, asgStat(PicoId Id, EXP E) := S, is_constant(E)){
        ConstantUses := reachX(CFG, {S@pos}, Defs[Id] - S@pos);
        
        for(ProgramPoint C <- ConstantUses){
            replacements = replacements + {<C, Id, E>};
        }
    }
    
    return performReplacements(P, replacements);
 }
 
 PROGRAM performReplacements(PROGRAM P, rel[ProgramPoint, PicoId, EXP] replacements){
      
    println("replacements=<replacements>");
 
    return visit (P) {
     case STATEMENT S:
     	  { R = replacements[S@pos];
            if(R != {}){
               insert visit(S){
                      case id(PicoId Name):
                           if({EXP NewExp} := R[Name])
                              insert NewExp;
               };
            }
          }
                   
     case EXP E:
          { if(id(PicoId Name) := E){
               R = replacements[E@pos];
               if(R != {} && {EXP NewExp} := R[Name])
                  insert NewExp;
            }
          }
    };
}

PROGRAM smallCP =

program([decl("x", natural), decl("s", string), decl("d", natural), decl("dd", natural)],
        [ asgStat("x", natCon(3)),
          asgStat("d", id("x")) ,
          whileStat(id("x"),
                    [ asgStat("x", sub(id("x"), id("d"))),
                      asgStat("s", conc(id("s"), strCon("#"))),
                      asgStat("dd", add(id("d"), id("x")))
                    ]
                   )
        ]
       );
       
public bool test(){
  P = constantPropagation(annotate(smallCP));
  println("P=<P>");
  return true;
}