module PicoUseDef

import PicoAbstractSyntax;
import PicoPrograms;
import UnitTest;
import IO;

public rel[PicoId, EXP] uses(PROGRAM P) {
  return {<Id, E> | EXP E <- P, id(PicoId Id) := E};
}

public rel[PicoId, STATEMENT] defs(PROGRAM P) { 
  return {<Id, S> | STATEMENT S <- P, asgStat(PicoId Id, EXP Exp) := S};
}

public bool test(){
  assertTrue(uses(small) == {<"s",id("s")>,<"x",id("x")>});
  assertTrue(defs(small) == {<"x",asgStat("x",natCon(3))>,
                             <"s",asgStat("s",conc(id("s"),strCon("#")))>,
                             <"x",asgStat("x",sub(id("x"),natCon(1)))>});
                             

  assertTrue(uses(fac) == {<"repnr",id("repnr")>,<"rep",id("rep")>,<"input",id("input")>,<"output",id("output")>});
  assertTrue(defs(fac) == {<"rep",asgStat("rep",id("output"))>,
                           <"output",asgStat("output",natCon(1))>,
                           <"input",asgStat("input",natCon(13))>,
                           <"repnr",asgStat("repnr",sub(id("repnr"),natCon(1)))>,
                           <"output",asgStat("output",add(id("output"),id("rep")))>,
                           <"input",asgStat("input",sub(id("input"),natCon(1)))>,
                           <"repnr",asgStat("repnr",id("input"))>});
  
  return report();
}