module experiments::Compiler::Rascal2muRascal::Run

import Prelude;
import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::RVM::Run;
import experiments::Compiler::muRascal2RVM::mu2rvm;

loc Example1 = |std:///experiments/Compiler/Rascal2muRascal/Examples/Example1.rsc|;
loc Example2 = |std:///experiments/Compiler/Rascal2muRascal/Examples/Example2.rsc|;

void run(){
  muP = r2mu(Example2);
  iprintln(muP);
  rvmP = mu2rvm(muP);
  iprintln(rvmP);
  
  <v, t> = executeProgram(rvmP, true, 1);
  println("Result = <v>, [<t> msec]");
}

