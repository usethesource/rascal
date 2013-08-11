module experiments::CoreRascal::muRascal::Examples::CountDown

import Prelude;
import  experiments::CoreRascal::muRascal::AST;
import experiments::CoreRascal::muRascalVM::AST;
import experiments::CoreRascal::muRascalVM::Run;
import experiments::CoreRascal::muRascal::mu2rvm;

list[MuFunction] functions = [

muFunction("countDown", 0, 1, 1, 
	[					
		muWhile(muCallPrim("greater_num_num", muVar("n", 0, 0), muCon(0)),
			[ muYield(muVar("n", 0, 0)),
			  muAssign("n", 0, 0, muCallPrim("subtraction_num_num", muVar("n", 0, 0), muCon(1)))
			]),
	    muReturn(muCon(0))		
	]
),

muFunction("main", 0, 1, 2, 
	[		
		muAssign("c", 0, 1, muCreate("countDown")),
		muInit(muVar("c", 0, 1), muCon(10)),
		muWhile(muHasNext(muVar("c", 0, 1)),
			  [ muCallPrim("println", muNext(muVar("c", 0, 1))) ]),
		muReturn(muCon(0))
	]
)

];

int runCountDown(){
  muP = muModule("CountDown", [], functions, [], []);
  rvmP = mu2rvm(muP);
  iprintln(rvmP);
  <v, t> = executeProgram(rvmP, false, 1);
  println("Result = <v>, [<t> msec]");
  return int n := v ? n : 0;
}
