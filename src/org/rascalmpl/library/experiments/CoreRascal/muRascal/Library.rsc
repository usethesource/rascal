module experiments::CoreRascal::muRascal::Library

import Prelude;
import  experiments::CoreRascal::muRascal::AST;
import experiments::CoreRascal::muRascalVM::AST;
import experiments::CoreRascal::muRascalVM::Run;
import experiments::CoreRascal::muRascal::mu2rvm;

list[MuFunction] library = [

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

muFunction("AND_U_U", 1, 2, 2, 
	[				
		muReturn(muCallPrim("and_bool_bool", muLoc("lhs", 0), muLoc("rhs", 1)))
	]
),
            
muFunction("AND_M_U", 1, 2, 2, 
	[					
		muAssignLoc("lhs", 0, muInit(muCreate(muLoc("lhs", 0)))),
		muWhile(muHasNext(muLoc("lhs", 0)),
			[ muIfelse(muNext(muLoc("lhs", 0)),
			     [ muIfelse(muLoc("rhs", 1),
			                [ muYield(muNext(muLoc("rhs", 1))) ],
			                [ ])
			     ],
			     [])              
			]),
		muReturn(muCon(false))
	]
),

muFunction("AND_U_M", 1, 2, 2, 
	[					 
		muIfelse(muLoc("lhs", 0),
			[ 
			  muAssignLoc("rhs",  1, muInit(muCreate(muLoc("rhs", 1)))),
			  muWhile(muHasNext(muLoc("rhs", 1)),
					  [ muYield(muNext(muLoc("rhs", 1))) ])
		    ],
			[]),
		muReturn(muCon(false))
	]
),

muFunction("AND_M_M", 1, 2, 2, 
	[					
		muAssignLoc("lhs", 0, muInit(muCreate(muLoc("lhs", 0)))),
		muWhile(muHasNext(muLoc("lhs", 0)),
			[ muIfelse(muNext(muLoc("lhs", 0)),
			     [ muAssign("rhs", 1, 1, muInit(muCreate(muLoc("lrs", 1)))),
			       muWhile(muHasNext(muLoc("lrs", 1)),
						   [ muYield(muNext(muLoc("lrs", 1))) ])
				],
				[])
			]),
		muReturn(muCon(false))
	]
),

muFunction("AND_U_U", 1, 2, 2, 
	[				
		muReturn(muCallPrim("and_bool_bool", muLoc("lhs", 0), muLoc("lrs", 1)))
	]
),
            
muFunction("ONE", 1, 1, 1, 
	[
		muAssignLoc("lhs", 0, muInit(muCreate(muLoc("arg", 0)))),
		muReturn(muNext(muLoc("arg", 0)))
	]
),
					
muFunction("ALL", 1, 1, 1, 
	[					
		muAssignLoc("arg", 0, muInit(muCreate(muLoc("arg", 0)))),
		muWhile(muHasNext(muLoc("lhs", 0)),
			[ muYield(muNext(muLoc("lhs", 0)))
			]),
	    muReturn(muCon(false))		
	]
),

muFunction("MATCH", 1, 2, 1, 
	[					
		muInit(muVar("pat", 1, 0), [muVar("subject", 1, 1)]),
		muWhile(muHasNext(muVar("pat", 1, 0)),
			[ muYield(muNext(muVar("pat", 1, 0)))
			]),
	    muReturn(muCon(false))		
	]
),

muFunction("MATCH_INT", 1, 2, 1, 
	[					
	    muReturn(muCallPrim("equals_num_num", muVar("pat", 1, 0), muVar("subject", 1, 1)))	
	]
),

muFunction("MATCH_LIST", 1, 2, 1, 
	[					
		muInit(muVar("pat", 1, 0), [muVar("subject", 1, 1)]),
		// list matching
	    muReturn(muCon(true))		
	]
)

];

void run(){
  muP = muModule("Library", [], library, [], []);
  rvmP = mu2rvm(muP);
  iprintln(rvmP);
  <v, t> = executeProgram(rvmP, true, 1);
  println("Result = <v>, [<t> msec]");
}
