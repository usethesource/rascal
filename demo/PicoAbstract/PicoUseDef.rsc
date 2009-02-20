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

public bool test(){

  assertTrue(uses(small) == {<"x",pp(id("x"))>,<"s",pp(id("s"))>});
  assertTrue(defs(small) == {<"s",pp(asgStat("s",conc(id("s"),strCon("#"))))>,
                             <"x",pp(asgStat("x",sub(id("x"),natCon(1))))>,
                             <"x",pp(asgStat("x",natCon(3)))>});               

  assertTrue(uses(fac) == {<"rep",pp(id("rep"))>,
                           <"input",pp(id("input"))>,
                           <"output",pp(id("output"))>,
                           <"repnr",pp(id("repnr"))>});
  
  assertTrue(defs(fac) == {<"repnr",pp(asgStat("repnr",sub(id("repnr"),natCon(1))))>,
                           <"output",pp(asgStat("output",add(id("output"),id("rep"))))>,
                           <"input",pp(asgStat("input",sub(id("input"),natCon(1))))>,
                           <"output",pp(asgStat("output",natCon(1)))>,
                           <"repnr",pp(asgStat("repnr",id("input")))>,
                           <"rep",pp(asgStat("rep",id("output")))>,
                           <"input",pp(asgStat("input",natCon(13)))>});
  return report();
}