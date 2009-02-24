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

assert <"x",pp(asgStat("x",sub(id("x"),natCon(1)))@(pos:3))>
       ==
       <"x",pp(asgStat("x",sub(id("x"),natCon(1))))>;
       
assert <"s",pp(asgStat("s",conc(id("s"),strCon("#")))@(pos:4))>
       ==
       <"s",pp(asgStat("s",conc(id("s"),strCon("#"))))>;
       
assert <"x",pp(asgStat("x",natCon(3))@(pos:1))>
       ==
       <"x",pp(asgStat("x",natCon(3)))>;

assert
	{
		<"x",pp(asgStat("x",sub(id("x"),natCon(1))))>,
        <"s",pp(asgStat("s",conc(id("s"),strCon("#"))))>,
        <"x",pp(asgStat("x",natCon(3)))>
    }                
    ==
    {
        <"x",pp(asgStat("x",sub(id("x"),natCon(1))))>,
    	<"s",pp(asgStat("s",conc(id("s"),strCon("#"))))>,
        <"x",pp(asgStat("x",natCon(3)))>
    }
    ;  

assert
	{
		<"x",pp(asgStat("x",sub(id("x"),natCon(1)))@(pos:3))>,
        <"s",pp(asgStat("s",conc(id("s"),strCon("#")))@(pos:4))>,
        <"x",pp(asgStat("x",natCon(3))@(pos:1))>
    }                
    ==
    {
        <"x",pp(asgStat("x",sub(id("x"),natCon(1))))>,
    	<"s",pp(asgStat("s",conc(id("s"),strCon("#"))))>,
        <"x",pp(asgStat("x",natCon(3)))>
    }
    ;  
  
}                     
 
 public bool test2(){
  assertTrue(uses(small) == {<"x",pp(id("x")@(pos:2))>,
                             <"x",pp(id("x")@(pos:6))>,
                             <"s",pp(id("s")@(pos:7))>});  
  
  assertTrue(defs(small) == {<"x",pp(asgStat("x",sub(id("x"),natCon(1)))@(pos:3))>,
                             <"s",pp(asgStat("s",conc(id("s"),strCon("#")))@(pos:4))>,
                             <"x",pp(asgStat("x",natCon(3))@(pos:1))>});
                             
  assertTrue(uses(fac) == {<"output",pp(id("output")@(pos:11))>,
                           <"input",pp(id("input")@(pos:12))>,
                           <"repnr",pp(id("repnr")@(pos:13))>,
                           <"output",pp(id("output")@(pos:14))>,
                           <"input",pp(id("input")@(pos:17))>,
                           <"rep",pp(id("rep")@(pos:15))>,
                           <"repnr",pp(id("repnr")@(pos:16))>,
                           <"input",pp(id("input")@(pos:17))>});

  assertTrue(defs(fac) == {<"repnr",pp(asgStat("repnr",sub(id("repnr"),natCon(1)))@(pos:8))>,
                           <"output",pp(asgStat("output",natCon(1))@(pos:2))>,
                           <"input",pp(asgStat("input",natCon(13))@(pos:1))>,
                           <"output",pp(asgStat("output",add(id("output"),id("rep")))@(pos:7))>,
                           <"input",pp(asgStat("input",sub(id("input"),natCon(1)))@(pos:8))>,
                           <"repnr",pp(asgStat("repnr",id("input"))@(pos:5))>,
                           <"rep",pp(asgStat("rep",id("output"))@(pos:4))>});


  return report();
}