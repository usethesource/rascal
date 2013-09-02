module experiments::Compiler::Rascal2muRascal::Run

import Prelude;
import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::muRascal::Implode;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::RVM::Run;
import experiments::Compiler::RVM::AST;
import experiments::Compiler::muRascal2RVM::mu2rvm;

loc Example1 = |std:///experiments/Compiler/Examples/Capture.rsc|;
loc Example2 = |std:///experiments/Compiler/Examples/D1D2.rsc|;
loc muExample3 = |std:///experiments/Compiler/muRascal2RVM/TypeConExample.mu|;
loc muExample4 = |std:///experiments/Compiler/muRascal2RVM/Coroutines.mu|;
loc muExample5 = |std:///experiments/Compiler/muRascal2RVM/CallByReference.mu|;
loc Example6 = |std:///experiments/Compiler/Examples/ListMatch.rsc|;
loc muExample7 = |std:///experiments/Compiler/muRascal2RVM/Capture.mu|;
loc Example8 = |std:///experiments/Compiler/Examples/Odd.rsc|;
loc Example9 = |std:///experiments/Compiler/Examples/Fac.rsc|;
loc Example10 = |std:///experiments/Compiler/Examples/Fib.rsc|;

void run(){
  muP = r2mu(Example9);
  rvmP = mu2rvm(muP, listing = true);
  <v, t> = executeProgram(rvmP, true, 1, false);
  println("Result = <v>, [<t> msec]");
  return;
}

void runMu2rvm(){
  muP = parse(muExample5);
  iprintln(muP);
  rvmP = mu2rvm(muP);
  //iprintln(rvmP);
  <v, t> = executeProgram(rvmP, true, 1, false);
  println("Result = <v>, [<t> msec]");
}

