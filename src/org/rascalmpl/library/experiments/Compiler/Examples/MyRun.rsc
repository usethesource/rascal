module experiments::Compiler::Examples::MyRun

import Prelude;
import experiments::Compiler::Execute;

loc base = |rascal:///experiments/Compiler/Examples/|;

value demo(str example, bool debug = false, bool listing=false, bool testsuite=false, bool recompile=true, bool profile=false) =
  execute(base + (example + ".rsc"), [], debug=debug, listing=listing, testsuite=testsuite, recompile=recompile, profile=profile);

 
 
value main(list[value] args) {
	demo("Mean") ; 	
	demo("Variance") ; 	
	demo("Fib") ; 	
	demo("Marriage") ; 	
	demo("SendMoreMoney") ; 	
	demo("Overloading") ; 	
	demo("BWhile") ; 	
	demo("BSudoku") ; 	
	return true ;
}