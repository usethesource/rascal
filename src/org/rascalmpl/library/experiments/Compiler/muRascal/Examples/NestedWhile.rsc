module experiments::Compiler::muRascal::Examples::NestedWhile

//import Prelude;
//import experiments::Compiler::muRascal::AST;
//import experiments::Compiler::RVM::AST;
//import experiments::Compiler::RVM::Run;
//import experiments::Compiler::muRascal2RVM::mu2rvm;

/* OUTDATED muRascal constructors */

//list[MuFunction] functions = [
//
//muFunction("main", 1, 1, 3, 
//	[	muCallPrim("println", muCon("At start of main")),				
//		muAssignLoc("i", 1, muCon(0)),
//		muWhile(muCallPrim("less_num_num", muLoc("i", 1), muCon(3)),
//			[ muCallPrim("println", muCon("Enter outer while body")),
//			  muAssignLoc("j", 2, muCon(0)),
//			  muWhile(muCallPrim("less_num_num", muLoc("j", 2), muCon(4)),
//			  [
//			  	muCallPrim("println", muCallPrim("addition_str_str",
//			  							muCallPrim("addition_str_str", muCon("i = "), muLoc("i", 1)), 
//			  						 	muCallPrim("addition_str_str", muCon(", j = "), muLoc("j", 2)))),
//			    muAssign("j", 1, 2, muCallPrim("addition_num_num", muLoc("j", 2), muCon(1)))
//			  ]),
//			  muAssign("i", 1, 1, muCallPrim("addition_num_num", muLoc("i", 1), muCon(1)))
//			]),
//		muReturn(muCon(true))
//	])
//
//];
//
//bool runNestedWhile(){
//  muP = muModule("NesteWhile", [], functions, [], []);
//  rvmP = mu2rvm(muP);
//  iprintln(rvmP);
//  <v, t> = executeProgram(rvmP, false);
//  println("Result = <v>, [<t> msec]");
//  return bool b := v ? b : false;
//}
