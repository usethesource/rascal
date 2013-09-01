module experiments::Compiler::Examples::Run

import Prelude;
import experiments::Compiler::Compile;

import experiments::Compiler::Examples::Capture;
import experiments::Compiler::Examples::D1D2;
import experiments::Compiler::Examples::Fac;
import experiments::Compiler::Examples::Fib;
import experiments::Compiler::Examples::ListMatch;
import experiments::Compiler::Examples::Odd;
//import experiments::Compiler::Examples::SendMoreMoney;

loc base = |std:///experiments/Compiler/Examples/|;

value run(str example bool debug = false, bool listing=false) =
  execute(base + (example + ".rsc"), debug=debug, listing=listing);

test bool tst() = run("Capture") == experiments::Compiler::Examples::Capture::main([]);
test bool tst() = run("D1D2") == experiments::Compiler::Examples::D1D2::main([]);
test bool tst() = run("Fac") == experiments::Compiler::Examples::Fac::main([]);
test bool tst() = run("Fib") == experiments::Compiler::Examples::Fib::main([]);
test bool tst() = run("ListMatch") == experiments::Compiler::Examples::ListMatch::main([]);
test bool tst() = run("Odd") == experiments::Compiler::Examples::Odd::main([]);
//test bool tst() = run("SendMoreMoney") == experiments::Compiler::Examples::SendMoreMoney::main([]);