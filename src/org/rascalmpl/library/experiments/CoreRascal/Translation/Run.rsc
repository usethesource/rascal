module experiments::CoreRascal::Translation::Run

import Prelude;
import experiments::CoreRascal::Translation::RascalModule;
import experiments::CoreRascal::muRascalVM::Run;
import experiments::CoreRascal::muRascal::mu2rvm;

loc Example1 = |std:///experiments/CoreRascal/Translation/Examples/Example1.rsc|;
loc Example2 = |std:///experiments/CoreRascal/Translation/Examples/Example2.rsc|;

void run(){
  muP = r2mu(Example2);
  iprintln(muP);
  rvmP = mu2rvm(muP);
  iprintln(rvmP);
  
  <v, t> = executeProgram(rvmP, true, 1);
  println("Result = <v>, [<t> msec]");
}

