module PicoUseDef

import PicoAbstractSyntax;
import PicoAnalysis;
import PicoPrograms;
import UnitTest;
import IO;

public rel[PicoId, ProgramPoint] uses(PROGRAM P) {
  return {<Id, E@pos> | EXP E <- P, id(PicoId Id) := E};
}

public rel[PicoId, ProgramPoint] defs(PROGRAM P) { 
  return {<Id, S@pos> | STATEMENT S <- P, asgStat(PicoId Id, EXP Exp) := S};
}        
 
public bool test(){
  assertTrue(uses(small) == {<"x",2>,
                             <"x",6>,
                             <"s",7>});  
  
  assertTrue(defs(small) == {<"x",3>,
                             <"s",4>,
                             <"x",1>});
                             
  assertTrue(uses(fac) == {<"output",11>,
                           <"input",12>,
                           <"repnr",13>,
                           <"output",14>,
                           <"input",17>,
                           <"rep",15>,
                           <"repnr",16>,
                           <"input",17>});

  assertTrue(defs(fac) == {<"repnr",8>,
                           <"output",2>,
                           <"input",1>,
                           <"output",7>,
                           <"input",8>,
                           <"repnr",5>,
                           <"rep",4>});

  return report();
}