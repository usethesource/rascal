module experiments::Compiler::Rascal2muRascal::Run

import Prelude;
import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::muRascal::Implode;
import experiments::Compiler::RVM::Run;
import experiments::Compiler::muRascal2RVM::mu2rvm;

loc Example1 = |std:///experiments/Compiler/Rascal2muRascal/Examples/Example1.rsc|;
loc Example2 = |std:///experiments/Compiler/Rascal2muRascal/Examples/Example2.rsc|;
loc muExample3 = |std:///experiments/Compiler/muRascal2RVM/TypeConExample.mu|;

void run(){
  muP = r2mu(Example1);
  iprintln(muP);
  rvmP = mu2rvm(muP);
 // iprintln(rvmP);
  
 // <v, t> = executeProgram(rvmP, true, 1);
 // println("Result = <v>, [<t> msec]");
}

void runMu2rvm(){
  muP = parse(muExample3);
  iprintln(muP);
  rvmP = mu2rvm(muP);
  iprintln(rvmP);
  
  <v, t> = executeProgram(rvmP, true, 1);
  println("Result = <v>, [<t> msec]");
}

