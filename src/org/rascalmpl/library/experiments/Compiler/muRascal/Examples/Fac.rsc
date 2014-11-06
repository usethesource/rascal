module experiments::Compiler::muRascal::Examples::Fac

//import Prelude;
//import util::Benchmark;
//
//import  experiments::Compiler::muRascal::AST;
//import experiments::Compiler::RVM::AST;
//import experiments::Compiler::RVM::Run;
//import experiments::Compiler::muRascal2RVM::mu2rvm;

/* OUTDATED muRascal constructors */

//
//int facarg = 20;
//int repeat = 1000;
//
//list[MuFunction] functions = [
//
//// fac(n) = n == 1 ? 1 : n * fac(n - 1)
//
//muFunction("fac", 1, 1, 1, 
//	[ muReturn(muIfelse(muCallPrim("equals_num_num", muLoc("n", 0), muCon(1)),
//						[ muCon(1) ],
//						[ muCallPrim("product_num_num", 
//						             muLoc("n", 0),
//						             muCall("fac", [ muCallPrim("subtraction_num_num", muLoc("n", 0), muCon(1)) ]))
//						]))
//	]),
//
//// main(args) { return fac(facarg); }
//						            
//muFunction("main", 1, 1, 2, 
//	[		
//		muReturn(muCall("fac", [muCon(facarg)]))
//	]
//)
//
//];
//
//int fac(int n) = (n == 1) ? 1 : n * fac(n-1);
//
//int fac_in_rascal(){
//	t1 = getMilliTime();
//	v = 0;
//	for(int i <- [0 .. repeat]){
//		v = fac(facarg);
//	}
//	t2 = getMilliTime();
//	println("rascal: fac(<facarg>) = <v> [<t2 - t1> msec]");
//	return t2 - t1;
//}
//
//test bool tstFac() = runFac() == 2432902008176640000;
//
//int runFac(){
//  muP = muModule("Fac", [], functions, [], []);
//  rvmP = mu2rvm(muP);
//  //iprintln(rvmP);
//  <v, t_rvm> = executeProgram(rvmP, false);
//  println("rvm:    fac(<facarg>) = <v> [<t_rvm> msec]");
//  t_rascal = fac_in_rascal();
//  println("ratio:  <t_rascal * 1.0 / t_rvm>");
//  return int n := v ? n : 0;
//}
