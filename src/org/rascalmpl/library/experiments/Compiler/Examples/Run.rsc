module experiments::Compiler::Examples::Run

import Prelude;
import experiments::Compiler::Execute;

import experiments::Compiler::Examples::Bottles;
import experiments::Compiler::Examples::Fac;
import experiments::Compiler::Examples::Fib;
import experiments::Compiler::Examples::SendMoreMoney;

import experiments::Compiler::Examples::UninitializedVariable;
import experiments::Compiler::Examples::UninitializedVariables;

import experiments::Compiler::Examples::RascalExtraction;

loc base = |std:///experiments/Compiler/Examples/|;

value demo(str example bool debug = false, bool listing=false, bool testsuite=false, bool recompile=true, bool profile=false) =
  execute(base + (example + ".rsc"), [], debug=debug, listing=listing, testsuite=testsuite, recompile=recompile, profile=profile);


// Bug in the interpreter, see issue #542
test bool tst() = demo("Bottles") == experiments::Compiler::Examples::Bottles::main([]);
test bool tst() = demo("Fac") == experiments::Compiler::Examples::Fac::main([]);
test bool tst() = demo("Fib") == experiments::Compiler::Examples::Fib::main([]);

test bool tst() = demo("SendMoreMoney") == experiments::Compiler::Examples::SendMoreMoney::main([]);

// Interpreter now complains about unitialized variables
// RVM now assumes that all variables have been initialized
//test bool tst() = demo("UninitializedVariable") == experiments::Compiler::Examples::UninitializedVariable::expectedResult;
//test bool tst() = demo("UninitializedVariables") == experiments::Compiler::Examples::UninitializedVariables::expectedResult

test bool tst() = demo("RascalExtraction") == experiments::Compiler::Examples::RascalExtraction::main([]);
