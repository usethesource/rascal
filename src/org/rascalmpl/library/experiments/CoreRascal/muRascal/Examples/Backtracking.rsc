module experiments::CoreRascal::muRascal::Examples::Backtracking

import Prelude;
import  experiments::CoreRascal::muRascal::AST;
import experiments::CoreRascal::muRascalVM::AST;
import experiments::CoreRascal::muRascalVM::Run;
import experiments::CoreRascal::muRascal::mu2rvm;

list[MuFunction] functions = [
/*
muFunction("TRUE", 1, 0, 0, 
	[
		muReturn(muCon(true))
	]
),
       
muFunction("FALSE", 1, 0, 0, 
	[
		muReturn(muCon(false))
	]
),

          
muFunction("AND", 1, 2, 2, 
	[					
		muAssign("lhs", 1, 0, muInit(muCreate(muVar("lhs", 1, 0)))),
		muWhile(muHasNext(muVar("lhs", 1, 0)),
			[ muIfelse(muNext(muVar("lhs", 1, 0)),
			     [ muAssign("rhs", 1, 1, muInit(muCreate(muVar("rhs", 1, 1)))),
			       muWhile(muHasNext(muVar("rhs", 1, 1)),
						   [ muYield(muNext(muVar("rhs", 1, 1))) ])
				],
				[])
			]),
		muReturn(muCon(false))
	]
),
          
muFunction("ONE", 1, 1, 1, 
	[
		muAssign("lhs", 1, 0, muInit(muCreate(muVar("arg", 1, 0)))),
		muReturn(muNext(muVar("arg", 1, 0)))
	]
),
/*					
muFunction("ALL", 0, 1, 1, 
	[					
		muAssign("arg", 0, 0, muInit(muCreate(muVar("arg", 0, 0)))),
		muWhile(muHasNext(muVar("lhs", 0, 0)),
			[ muYield(muNext(muVar("arg", 0, 0)))
			]),
	    muReturn(muCon(false))		
	]
),

muFunction("main1", 1, 1, 2, 
	[		
		muAssign("c", 1, 1, muCreate("AND")),
		muInit(muVar("c", 1, 1), [muFun("TRUE"), muFun("TRUE")]),
		muReturn(muNext(muVar("c", 1, 1)))
	]
),
*/
muFunction("main", 1, 1, 3, 
	[	muNote("At start of main"),				
		//muAssign("i", 1, 1, muCon(0)),
		//muAssign("j", 1, 2, muCon(0)),
		//muWhile(muCallPrim("less_num_num", muVar("i", 1, 1), muCon(1)),
		//	[ muNote("Enter outer while body"),
		//	  muWhile(muCallPrim("less_num_num", muVar("j", 1, 2), muCon(1)),
		//	  [ muNote("Enter inner while body"),
		//	    muAssign("j", 1, 2, muCallPrim("addition_num_num", muVar("j", 1, 2), muCon(1)))
		//	  ]),
		//	  muAssign("i", 1, 1, muCallPrim("subtraction_num_num", muVar("i", 1, 1), muCon(1)))
		//	]),
		muReturn(muCon(false))
	]
)

];

bool runBacktracking(){
  muP = muModule("Backtracking", functions, [], []);
  rvmP = mu2rvm(muP);
  iprintln(rvmP);
  <v, t> = executeProgram(rvmP, true, 1);
  println("Result = <v>, [<t> msec]");
  return bool b := v ? b : false;
}
