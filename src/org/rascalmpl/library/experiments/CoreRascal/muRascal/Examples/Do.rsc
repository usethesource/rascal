module experiments::CoreRascal::muRascal::Examples::Do

import Prelude;
import experiments::CoreRascal::muRascal::AST;
import experiments::CoreRascal::muRascalVM::AST;
import experiments::CoreRascal::muRascalVM::Run;
import experiments::CoreRascal::muRascal::mu2rvm;

list[MuFunction] functions = [

// int square(int n) = n * n;

muFunction("square", 1, 1, 1, 
	[ muReturn(muCallPrim("product_num_num", muLoc("n", 0), muLoc("n", 0)))
	]),
	
// int cube(int n) = n * n * n;

muFunction("cube", 1, 1, 1, 
	[ muReturn(muCallPrim("product_num_num", 
						  muLoc("n", 0), 
			              muCallPrim("product_num_num", muLoc("n", 0), muLoc("n", 0))))
	]),
	
// int do(f, n) = f(n);

muFunction("do", 1, 2, 2, 
	[ muReturn(muCall( muVar("f", 1, 0), [ muVar("n", 1, 1) ] ))
	]),

// main(args) { return(fib(20)); }
						            
muFunction("main", 1, 1, 1, 
	[		
		muReturn(muCall("do", [muFun("cube"), muCon(20)]))
	]
)

];

test bool tstDo() = runDo() == 8000;

int runDo(){
  muP = muModule("Do", [], functions, [], []);
  rvmP = mu2rvm(muP);
  iprintln(rvmP);
  <v, t> = executeProgram(rvmP, true, 1);
  println("Result = <v>, [<t> msec]");
  return int n := v ? n : 0;
}
