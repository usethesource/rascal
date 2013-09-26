module experiments::Compiler::Examples::Run

import Prelude;
import experiments::Compiler::Execute;

import experiments::Compiler::Examples::Bottles;
import experiments::Compiler::Examples::Capture;
import experiments::Compiler::Examples::E1E2;
import experiments::Compiler::Examples::Fac;
import experiments::Compiler::Examples::Fib;
import experiments::Compiler::Examples::Fail;
import experiments::Compiler::Examples::ListMatch;
import experiments::Compiler::Examples::Odd;
import experiments::Compiler::Examples::SendMoreMoney;
import experiments::Compiler::Examples::SetMatch;
import experiments::Compiler::Examples::TestSuite;
import experiments::Compiler::Examples::Template;
import experiments::Compiler::Examples::Overloading1;
import experiments::Compiler::Examples::Overloading2;
import experiments::Compiler::Examples::Overloading3;
import experiments::Compiler::Examples::OverloadingMatch;
import experiments::Compiler::Examples::OverloadingPlusBacktracking;
import experiments::Compiler::Examples::ExceptionHandling1;
import experiments::Compiler::Examples::ExceptionHandling2;
import experiments::Compiler::Examples::ExceptionHandling3;
import experiments::Compiler::Examples::ExceptionHandlingFinally1;
import experiments::Compiler::Examples::ExceptionHandlingFinally2;
import experiments::Compiler::Examples::ExceptionHandlingFinally3;
import experiments::Compiler::Examples::ExceptionHandlingFinally4;
import experiments::Compiler::Examples::ExceptionHandlingFinally5;
import experiments::Compiler::Examples::ExceptionHandlingFinally6;
import experiments::Compiler::Examples::ExceptionHandlingFinally7;
import experiments::Compiler::Examples::ExceptionHandlingFinally8;

loc base = |std:///experiments/Compiler/Examples/|;


value demo(str example bool debug = false, bool listing=false, bool testsuite=false, bool recompile=false) =
  execute(base + (example + ".rsc"), [], debug=debug, listing=listing, testsuite=testsuite, recompile=recompile);

test bool tst() = demo("Bottles") == experiments::Compiler::Examples::Bottles::main([]);
test bool tst() = demo("Capture") == experiments::Compiler::Examples::Capture::main([]);
test bool tst() = demo("E1E2") == experiments::Compiler::Examples::E1E2::main([]);
test bool tst() = demo("Fac") == experiments::Compiler::Examples::Fac::main([]);
test bool tst() = demo("Fib") == experiments::Compiler::Examples::Fib::main([]);
test bool tst() = demo("ListMatch") == experiments::Compiler::Examples::ListMatch::main([]);
test bool tst() = demo("SetMatch") == experiments::Compiler::Examples::SetMatch::main([]);
test bool tst() = demo("Odd") == experiments::Compiler::Examples::Odd::main([]);
test bool tst() = demo("SendMoreMoney") == experiments::Compiler::Examples::SendMoreMoney::main([]);

// String templates generate a too large indent for nested templates.
/*fails*/ // test bool tst() = demo("Template") == experiments::Compiler::Examples::Template::main([]);
test bool tst() = demo("Overloading1") == experiments::Compiler::Examples::Overloading1::main([]);
test bool tst() = demo("Overloading2") == experiments::Compiler::Examples::Overloading2::main([]) && demo("Overloading1") == demo("Overloading2");
test bool tst() = demo("Overloading3") == experiments::Compiler::Examples::Overloading3::main([]);
test bool tst() = demo("OverloadingMatch") == experiments::Compiler::Examples::OverloadingMatch::main([]);
test bool tst() = demo("OverloadingPlusBacktracking") == experiments::Compiler::Examples::OverloadingPlusBacktracking::main([]);
test bool tst() = demo("ExceptionHandling1") == experiments::Compiler::Examples::ExceptionHandling1::main([]);
test bool tst() = demo("ExceptionHandling2") == experiments::Compiler::Examples::ExceptionHandling2::main([]);
test bool tst() = demo("ExceptionHandling3") == experiments::Compiler::Examples::ExceptionHandling3::main([]);
test bool tst() = demo("ExceptionHandlingFinally1") == experiments::Compiler::Examples::ExceptionHandlingFinally1::main([]);
test bool tst() = demo("ExceptionHandlingFinally2") == experiments::Compiler::Examples::ExceptionHandlingFinally2::main([]);
test bool tst() = demo("ExceptionHandlingFinally3") == experiments::Compiler::Examples::ExceptionHandlingFinally3::main([]);
test bool tst() = demo("ExceptionHandlingFinally4") == experiments::Compiler::Examples::ExceptionHandlingFinally4::main([]);
test bool tst() = demo("ExceptionHandlingFinally5") == experiments::Compiler::Examples::ExceptionHandlingFinally5::main([]);
test bool tst() = demo("ExceptionHandlingFinally6") == experiments::Compiler::Examples::ExceptionHandlingFinally6::main([]);
test bool tst() = demo("ExceptionHandlingFinally7") == experiments::Compiler::Examples::ExceptionHandlingFinally7::main([]);
test bool tst() = demo("ExceptionHandlingFinally8") == experiments::Compiler::Examples::ExceptionHandlingFinally8::main([]);
