module  demo::PicoAbstract::PicoUseDef

import demo::PicoAbstract::PicoAbstractSyntax;
import demo::PicoAbstract::PicoAnalysis;
import demo::PicoAbstract::PicoPrograms;
import UnitTest;
import IO;

/*
 * Extract uses and definitions of variables in a program.
 */


private set[PicoId] getVarUses(EXP E){
    println("getVarUses: <E>");
    res = {Id | id(PicoId Id) <- E};
    println("getVarUses: <res>");
    return res;
}

/*
 * Compute variable uses:
 * - Associate all variable uses in an expression with the enclosing expression.
 * - Associate all variable uses in an assignment statement with that statement.
 */

public rel[PicoId, ProgramPoint] uses(PROGRAM P) {
  rel[PicoId, ProgramPoint] result = {};
  visit(P) {
   case ifStat(EXP Exp, _,  _):{
   			println("case ifStat: <Exp>");
        	result = result + getVarUses(Exp) * {Exp@pos};
        }                    
   case whileStat(EXP Exp, _):{
   			println("case whileStat: <Exp>");
        	result = result + getVarUses(Exp) * {Exp@pos};
         }
         
   case subject: asgStat(PicoId _, EXP Exp):{
   		println("case asgStat: <Exp>");
        result = result + getVarUses(Exp) * {subject@pos};
        } 
   };
   return result;                  
  }
  
  
/*
 * Compute variable definitions:
 * - Each assignment generates a definition
 */
 
public rel[PicoId, ProgramPoint] defs(PROGRAM P) { 
  println("defs");
  return {<Id, S@pos> | STATEMENT S <- P, asgStat(PicoId Id, EXP Exp) := S};
}        
 
public bool test(){

  assertEqual(uses(annotate(small)), {<"x",3>,<"s",1>,<"x",6>});  
  
  assertEqual(defs(annotate(small)), {<"s",1>,<"x",3>,<"x",9>});
  
  assertEqual(uses(annotate(fac)), {<"output",5>,<"rep",5>,<"input",11>,<"output",13>,<"input",16>,<"input",1>,<"repnr",3>,<"repnr",18>}); 

  assertEqual(defs(annotate(fac)), {<"output",23>,<"output",5>,<"input",25>,<"repnr",11>,<"input",1>,<"rep",13>,<"repnr",3>});

  return report("PicoUseDef");
}