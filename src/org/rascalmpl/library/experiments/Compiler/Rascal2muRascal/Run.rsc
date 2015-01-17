module experiments::Compiler::Rascal2muRascal::Run

import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::muRascal::Implode;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::RVM::Run;
import experiments::Compiler::RVM::AST;
import experiments::Compiler::muRascal2RVM::mu2rvm; 

loc Example1 = |rascal:///experiments/Compiler/Examples/Capture.rsc|;
loc Example2 = |rascal:///experiments/Compiler/Examples/D1D2.rsc|;
loc Example3 = |rascal:///experiments/Compiler/Examples/ListMatch.rsc|;
loc Example4 = |rascal:///experiments/Compiler/Examples/Odd.rsc|;
loc Example5 = |rascal:///experiments/Compiler/Examples/Fac.rsc|;
loc Example6 = |rascal:///experiments/Compiler/Examples/Fib.rsc|;

loc muExample1 = |rascal:///experiments/Compiler/muRascal2RVM/TypeConExample.mu|;
loc muExample2 = |rascal:///experiments/Compiler/muRascal2RVM/Coroutines.mu|;
loc muExample3 = |rascal:///experiments/Compiler/muRascal2RVM/CallByReference.mu|;
loc muExample4 = |rascal:///experiments/Compiler/muRascal2RVM/Capture.mu|;

//void run(){
//  muP = r2mu(Example5);
//  rvmP = mu2rvm(muP);
//  <v, t> = executeProgram(rvmP, [], false, false);
//  println("Result = <v>, [<t> msec]");
//  return;
//}
//
//void runMu2rvm(){
//  muP = parse(muExample4);
//  // Add 'testsuite'
//  code = [ muCallPrim("testreport_open", []), muCallPrim("testreport_close", []), muReturn() ];
//  main_testsuite = getUID(muP.name,[],"testsuite",1);
//  // println("main_testsuite = <main_testsuite>");
//  // Generate a very generic function type
//  ftype = Symbol::func(Symbol::\value(),[Symbol::\list(Symbol::\value())]);
//  muP.functions = muP.functions + muFunction(main_testsuite, ftype, "" /*in the root*/, 1, 1, |rascal:///|, [], (), code);
//  rvmP = mu2rvm(muP);
//  <v, t> = executeProgram(rvmP, [], true, false);
//  println("Result = <v>, [<t> msec]");
//}

