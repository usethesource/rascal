module PicoUseDef

import PicoAbstractSyntax;
import PicoAnalysis;
import PicoPrograms;
import UnitTest;
import IO;

public rel[PicoId, ProgramPoint] uses(PROGRAM P) {
  return {<Id, pp(E)> | EXP E <- P, id(PicoId Id) := E};
}

public rel[PicoId, ProgramPoint] defs(PROGRAM P) { 
  return {<Id, pp(S)> | STATEMENT S <- P, asgStat(PicoId Id, EXP Exp) := S};
}

void myAssertTrue(bool res){
 	resetLabelGen();
 	assertTrue(res);
}

public bool test(){
 
  resetLabelGen();
  assertTrue(uses(small) == {<"x",pp(id("x"),2)>,
                             <"x",pp(id("x"),1)>,
                             <"s",pp(id("s"),3)>});  
  
  resetLabelGen();
  assertTrue(defs(small) == {<"x",pp(asgStat("x",sub(id("x"),natCon(1))),2)>,
                             <"s",pp(asgStat("s",conc(id("s"),strCon("#"))),3)>,
                             <"x",pp(asgStat("x",natCon(3)),1)>});
                             
  resetLabelGen();
  assertTrue(uses(fac) == {<"output",pp(id("output"),2)>,
                           <"input",pp(id("input"),1)>,
                           <"repnr",pp(id("repnr"),7)>,
                           <"output",pp(id("output"),5)>,<"input",pp(id("input"),8)>,
                           <"rep",pp(id("rep"),6)>,<"repnr",pp(id("repnr"),4)>,
                           <"input",pp(id("input"),3)>});

  resetLabelGen();
  assertTrue(defs(fac) == {<"repnr",pp(asgStat("repnr",sub(id("repnr"),natCon(1))),6)>,
                           <"output",pp(asgStat("output",natCon(1)),2)>,
                           <"input",pp(asgStat("input",natCon(13)),1)>,
                           <"output",pp(asgStat("output",add(id("output"),id("rep"))),5)>,
                           <"input",pp(asgStat("input",sub(id("input"),natCon(1))),7)>,
                           <"repnr",pp(asgStat("repnr",id("input")),4)>,
                           <"rep",pp(asgStat("rep",id("output")),3)>});


  return report();
}