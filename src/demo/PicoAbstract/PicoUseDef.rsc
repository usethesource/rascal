module demo::PicoAbstract::PicoUseDef

import demo::PicoAbstract::PicoAbstractSyntax;
import demo::PicoAbstract::PicoAnalysis;
import demo::PicoAbstract::PicoPrograms;
import UnitTest;
import IO;


private set[PicoId] getVarUses(EXP E){
    return {Id | EXP E1 <- E, id(PicoId Id) := E1};
}

public rel[PicoId, ProgramPoint] uses(PROGRAM P) {
  rel[PicoId, ProgramPoint] result = {};
  visit(P) {
   case ifStat(EXP Exp, _,  _):
        result = result + getVarUses(Exp) * {subject@pos};
                            
   case whileStat(EXP Exp, _):
         result = result + getVarUses(Exp) * {subject@pos};
         
   case asgStat(_, EXP Exp):{
         result = result + getVarUses(Exp) * {subject@pos};
        }
   };
   return result;                  
  }

public rel[PicoId, ProgramPoint] defs(PROGRAM P) { 
  return {<Id, S@pos> | STATEMENT S <- P, asgStat(PicoId Id, EXP Exp) := S};
}        
 
public bool test(){

  println("small=<small>");
  asmall = annotate(small);
  println("asmall=<asmall>");
  U = uses(annotate(small));
  println("<U>");
  assertTrue(uses(small) == {<"x",2>,
                             <"x",3>,
                             <"s",4>});  
 
  assertTrue(defs(small) == {<"x",3>,
                             <"s",4>,
                             <"x",1>});
                           
  assertTrue(uses(fac) == {<"output",4>,
                           <"input",3>,
                           <"repnr",6>,
                           <"output",7>,
                           <"input",5>,
                           <"rep",7>,
                           <"repnr",8>,
                           <"input",9>});

  assertTrue(defs(fac) == {<"repnr",8>,
                           <"output",2>,
                           <"input",1>,
                           <"output",7>,
                           <"input",9>,
                           <"repnr",5>,
                           <"rep",4>});

  return report("PicoUseDef");
}