module experiments::CoreRascal::muRascal::Examples::NestedWhile

import Prelude;
import  experiments::CoreRascal::muRascal::AST;
import experiments::CoreRascal::muRascalVM::AST;
import experiments::CoreRascal::muRascalVM::Run;
import experiments::CoreRascal::muRascal::mu2rvm;

list[MuFunction] functions = [

muFunction("main", 1, 1, 3, 
	[	muNote("At start of main"),				
		muAssign("i", 1, 1, muCon(0)),
		muWhile(muCallPrim("less_num_num", muVar("i", 1, 1), muCon(3)),
			[ muNote("Enter outer while body"),
			  muAssign("j", 1, 2, muCon(0)),
			  muWhile(muCallPrim("less_num_num", muVar("j", 1, 2), muCon(4)),
			  [ //muNote("Enter inner while body"),
			  	muCallPrim("println", muCallPrim("addition_str_str",
			  							muCallPrim("addition_str_str", muCon("i = "), muVar("i", 1, 1)), 
			  						 	muCallPrim("addition_str_str", muCon(", j = "), muVar("j", 1, 2)))),
			    muAssign("j", 1, 2, muCallPrim("addition_num_num", muVar("j", 1, 2), muCon(1)))
			  ]),
			  muAssign("i", 1, 1, muCallPrim("addition_num_num", muVar("i", 1, 1), muCon(1)))
			]),
		muReturn(muCon(true))
	])

];

bool runNestedWhile(){
  muP = muModule("NesteWhile", [], functions, [], []);
  rvmP = mu2rvm(muP);
  iprintln(rvmP);
  <v, t> = executeProgram(rvmP, false, 1);
  println("Result = <v>, [<t> msec]");
  return bool b := v ? b : false;
}
