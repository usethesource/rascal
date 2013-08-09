module experiments::CoreRascal::Translation::Run

import Prelude;
import experiments::CoreRascal::Translation::RascalModule;
import experiments::CoreRascal::muRascalVM::Run;
import experiments::CoreRascal::muRascal::mu2rvm;

loc Example1 = |std:///experiments/CoreRascal/Translation/Examples/Example1.rsc|;


void run(){
  p = mu2rvm(r2mu(Example1));
  iprintln(p);
  <v, t> = executeProgram(p, false, 1);
  println("Result = <v>, [<t> msec]");
}

