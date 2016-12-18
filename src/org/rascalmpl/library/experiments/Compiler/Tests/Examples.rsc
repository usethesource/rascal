module experiments::Compiler::Tests::Examples

import Prelude;
import experiments::Compiler::Execute;
import util::Reflective;

import experiments::Compiler::Examples::Bottles;
//import experiments::Compiler::Examples::Fac;
//import experiments::Compiler::Examples::Fib;
//import experiments::Compiler::Examples::SendMoreMoney;

//import experiments::Compiler::Examples::UninitializedVariable;
//import experiments::Compiler::Examples::UninitializedVariables;

//import experiments::Compiler::Examples::RascalExtraction;
//import experiments::Compiler::Examples::RascalMetrics;

loc base = |std:///experiments/Compiler/Examples/|;

value demo(str example bool debug = false, bool testsuite=false, bool recompile=true, bool profile=false, bool jvm=false) {
  compileAndLink("experiments::Compiler::Examples::" + example, pathConfig(), jvm=jvm);
  return execute(base + (example + ".rsc"), pathConfig(), debug=debug, testsuite=testsuite, recompile=recompile, profile=profile);
}  

test bool tstBottles() = demo("Bottles") == experiments::Compiler::Examples::Bottles::main();
//test bool tstFac() = demo("Fac") == experiments::Compiler::Examples::Fac::main();
//test bool tstFib() = demo("Fib") == experiments::Compiler::Examples::Fib::main();
//test bool tstMoney() = demo("SendMoreMoney") == experiments::Compiler::Examples::SendMoreMoney::main();

// Interpreter does complain about unitialized variables
// RVM also checks for unitialized variables (but this may change when we start checking for this)

//test bool tst() = demo("UninitializedVariable") == experiments::Compiler::Examples::UninitializedVariable::expectedResult;
//test bool tst() = demo("UninitializedVariables") == experiments::Compiler::Examples::UninitializedVariables::expectedResult;

//test bool tstExtract() = demo("RascalExtraction") == experiments::Compiler::Examples::RascalExtraction::main();
//test bool tstMetrics() = demo("RascalMetrics") == experiments::Compiler::Examples::RascalMetrics::main();

