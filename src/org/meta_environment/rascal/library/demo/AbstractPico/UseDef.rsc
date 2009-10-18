module  demo::AbstractPico::UseDef

import demo::AbstractPico::AbstractSyntax;
import demo::AbstractPico::Analysis;
import demo::AbstractPico::Programs;
import IO;

/*
 * Extract uses and definitions of variables in a program.
 */


private set[PicoId] getVarUses(EXP E){
    println("getVarUses: <E>");
    res = {Id | /id(PicoId Id) <- E};
    println("getVarUses returns: <res>");
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
         
   case asg: asgStat(PicoId _, EXP Exp):{
   		println("case asgStat: <Exp>");
        result = result + getVarUses(Exp) * {asg@pos};
        } 
   };
   return result;                  
  }
  
  
/*
 * Compute variable definitions:
 * - Each assignment generates a definition
 */
 
public rel[PicoId, ProgramPoint] defs(PROGRAM P) { 
  return {<Id, S@pos> | /STATEMENT S <- P, asgStat(PicoId Id, EXP Exp) := S};
}        
 
test uses(annotate(small)) == {<"x",3>,<"x",12>,<"s",7>};  
  
test defs(annotate(small)) ==  {<"s",7>,<"x",1>,<"x",3>};
  
test uses(annotate(fac)) == {<"repnr",37>,<"rep",9>,<"output",5>,<"repnr",13>,<"input",27>,<"input",32>,<"output",9>,<"input",7>}; 

test defs(annotate(fac)) ==  {<"repnr",7>,<"output",9>,<"input",1>,<"repnr",13>,<"input",27>,<"rep",5>,<"output",3>};
