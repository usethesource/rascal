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
        result = result + getVarUses(Exp) * {Exp@pos};
                            
   case whileStat(EXP Exp, _):
         result = result + getVarUses(Exp) * {Exp@pos};
         
   case asgStat(_, EXP Exp):{
         result = result + getVarUses(Exp) * {Exp@pos};
        }
   };
   return result;                  
  }

public rel[PicoId, ProgramPoint] defs(PROGRAM P) { 
  return {<Id, S@pos> | STATEMENT S <- P, asgStat(PicoId Id, EXP Exp) := S};
}        
 
public bool test(){

  assertTrue(uses(annotate(small)) == {<"s",5>,<"x",9>,<"x",10>});  
  
  assertTrue(defs(annotate(small)) == {<"s",5>,<"x",9>,<"x",12>});
                                
  assertTrue(uses(annotate(fac)) == {<"output",23>,
                                     <"repnr",14>,
                                     <"rep",18>,
                                     <"input",24>,
                                     <"output",18>,
                                     <"input",21>,
                                     <"input",7>,
                                     <"repnr",19>});

  assertTrue(defs(annotate(fac)) == {<"input",28>,
                                     <"repnr",14>,
                                     <"output",18>,
                                     <"rep",23>,
                                     <"repnr",21>,
                                     <"input",7>,
                                     <"output",26>});

  return report("PicoUseDef");
}