module experiments::Compiler::muRascal::Examples::Fib

import Prelude;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::RVM::AST;
import experiments::Compiler::RVM::Run;
import experiments::Compiler::muRascal2RVM::mu2rvm;

/* OUTDATED muRascal constructors */

//import util::Benchmark;
//
//int fibarg = 20;
//
//list[MuFunction] functions = [
//
//// int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));
//
//// F0	F1	F2	F3	F4	F5	F6	F7	F8	F9	F10	F11	F12	F13	F14	F15	F16	F17	 F18  F19	F20
//// 0	1	1	2	3	5	8	13	21	34	55	89	144	233	377	610	987	1597 2584 4181	6765
//
//
//
//muFunction("fib", 1, 1, 1, 
//	[ muReturn(muIfelse(muCallPrim("equals_num_num", [muLoc("n", 0), muCon(0)]),
//						[ muCon(0) ],
//						[ muIfelse(muCallPrim("equals_num_num", [muLoc("n", 0), muCon(1)]),
//								[ muCon(1) ],
//								[ muCallPrim("addition_num_num", 
//						             [ muCall(muFun("fib"), [ muCallPrim("subtraction_num_num",[muLoc("n", 0), muCon(1)]) ]),
//						               muCall(muFun("fib"), [ muCallPrim("subtraction_num_num",[muLoc("n", 0), muCon(2)]) ])
//						             ])
//						        ])
//						]))
//	]),
//
//// main(args) { return(fib(fibarg)); }
//						            
//muFunction("main", 1, 1, 2, 
//	[		
//		muReturn(muCall(muFun("fib"), [muCon(fibarg)]))
//	]
//)
//
//];
//
//int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));
//
//int fib_in_rascal(){
//	t1 = getMilliTime();
//	v = fib(fibarg);
//	t2 = getMilliTime();
//	println("rascal: fib(<fibarg>) = <v> [<t2 - t1> msec]");
//	return t2 - t1;
//}
//
//test bool tstFib() = runFib() == fib(fibarg);
//
//int runFib(){
//  muP = muModule("Fib", [], functions, [], []);
//  rvmP = mu2rvm(muP);
//  //iprintln(rvmP);
//  <v, t_rvm> = executeProgram(rvmP, false);
//  println("rvm:    fib(<fibarg>) = <v> [<t_rvm> msec]");
//  t_rascal = fib_in_rascal();
//  println("ratio:  <t_rascal * 1.0 / t_rvm>");
//  return int n := v ? n : 0;
//}
