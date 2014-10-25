module experiments::Compiler::muRascal::Examples::CountDown

import Prelude;
import  experiments::Compiler::muRascal::AST;
import experiments::Compiler::RVM::AST;
import experiments::Compiler::RVM::Run;
import experiments::Compiler::muRascal2RVM::mu2rvm;

/* OUTDATED muRascal constructors */

//list[MuFunction] functions = [
//
//muFunction("countDown", 0, 1, 1, 
//	[					
//		muWhile(muCallPrim("greater_num_num", muLoc("n", 0), muCon(0)),
//			[ muYield(muLoc("n", 0)),
//			  muAssignLoc("n", 0, muCallPrim("subtraction_num_num", muLoc("n", 0), muCon(1)))
//			]),
//	    muReturn(muCon(0))		
//	]
//),
//
//muFunction("main", 0, 1, 2, 
//	[		
//		muAssign("c", 0, 1, muCreate("countDown")),
//		muInit(muVar("c", 0, 1), [ muCon(10) ]),
//		muWhile(muHasNext(muVar("c", 0, 1)),
//			  [ muCallPrim("println", muNext(muVar("c", 0, 1))) ]),
//		muReturn(muCon(0))
//	]
//)
//
//];
//
//int runCountDown(){
//  muP = muModule("CountDown", [], functions, [], []);
//  rvmP = mu2rvm(muP);
//  iprintln(rvmP);
//  <v, t> = executeProgram(rvmP, false);
//  println("Result = <v>, [<t> msec]");
//  return int n := v ? n : 0;
//}
