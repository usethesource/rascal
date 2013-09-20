module experiments::Compiler::Examples::Run

import Prelude;
import experiments::Compiler::Execute;

import experiments::Compiler::Examples::Bottles;
import experiments::Compiler::Examples::Capture;
import experiments::Compiler::Examples::D1D2;
import experiments::Compiler::Examples::Fac;
import experiments::Compiler::Examples::Fib;
import experiments::Compiler::Examples::Fail;
import experiments::Compiler::Examples::ListMatch;
import experiments::Compiler::Examples::Odd;
import experiments::Compiler::Examples::SendMoreMoney;
import experiments::Compiler::Examples::SetMatch;
import experiments::Compiler::Examples::TestSuite;
//import experiments::Compiler::Examples::Template;
import experiments::Compiler::Examples::Overloading1;
import experiments::Compiler::Examples::Overloading2;
import experiments::Compiler::Examples::Overloading3;
import experiments::Compiler::Examples::OverloadingMatch;
import experiments::Compiler::Examples::OverloadingPlusBacktracking;
import experiments::Compiler::Examples::ExceptionHandling1;
import experiments::Compiler::Examples::ExceptionHandling2;

loc base = |std:///experiments/Compiler/Examples/|;


value demo(str example bool debug = false, bool listing=false, bool testsuite=false, bool recompile=false) =
  execute(base + (example + ".rsc"), debug=debug, listing=listing, testsuite=testsuite, recompile=recompile);

test bool tst0() = demo("Bottles") == experiments::Compiler::Examples::Bottles::main([]);
test bool tst() = demo("Capture") == experiments::Compiler::Examples::Capture::main([]);
test bool tst() = demo("D1D2") == experiments::Compiler::Examples::D1D2::main([]);
test bool tst() = demo("Fac") == experiments::Compiler::Examples::Fac::main([]);
test bool tst() = demo("Fib") == experiments::Compiler::Examples::Fib::main([]);
test bool tst() = demo("ListMatch") == experiments::Compiler::Examples::ListMatch::main([]);
test bool tst() = demo("SetMatch") == experiments::Compiler::Examples::SetMatch::main([]);
test bool tst() = demo("Odd") == experiments::Compiler::Examples::Odd::main([]);
test bool tst() = demo("SendMoreMoney") == experiments::Compiler::Examples::SendMoreMoney::main([]);
//test bool tst() = demo("Template") == experiments::Compiler::Examples::Template::main([]);
test bool tst() = demo("Overloading1") == experiments::Compiler::Examples::Overloading1::main([]);
test bool tst() = demo("Overloading2") == experiments::Compiler::Examples::Overloading2::main([]) && demo("Overloading1") == demo("Overloading2");
test bool tst() = demo("Overloading3") == experiments::Compiler::Examples::Overloading3::main([]);
test bool tst() = demo("OverloadingMatch") == experiments::Compiler::Examples::OverloadingMatch::main([]);
test bool tst() = demo("OverloadingPlusBacktracking") == experiments::Compiler::Examples::OverloadingPlusBacktracking::main([]);
test bool tst() = demo("ExceptionHandling1") == experiments::Compiler::Examples::ExceptionHandling1::main([]);
test bool tst() = demo("ExceptionHandling2") == experiments::Compiler::Examples::ExceptionHandling2::main([]);
