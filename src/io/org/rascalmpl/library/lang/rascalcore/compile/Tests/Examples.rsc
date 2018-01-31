@ignoreCompiler{Work in progress}
module lang::rascalcore::compile::Tests::Examples

import Prelude;
import lang::rascalcore::compile::Execute;
import util::Reflective;

import lang::rascalcore::compile::Examples::Bottles;
//import lang::rascalcore::compile::Examples::Fac;
//import lang::rascalcore::compile::Examples::Fib;
//import lang::rascalcore::compile::Examples::SendMoreMoney;

//import lang::rascalcore::compile::Examples::UninitializedVariable;
//import lang::rascalcore::compile::Examples::UninitializedVariables;

//import lang::rascalcore::compile::Examples::RascalExtraction;
//import lang::rascalcore::compile::Examples::RascalMetrics;

loc base = |std:///experiments/Compiler/Examples/|;

value demo(str example bool debug = false, bool testsuite=false, bool recompile=true, bool profile=false, bool jvm=false) {
  compileAndLink("lang::rascalcore::compile::Examples::" + example, pathConfig(), jvm=jvm);
  return execute(base + (example + ".rsc"), pathConfig(), debug=debug, testsuite=testsuite, recompile=recompile, profile=profile);
}  

test bool tstBottles() = demo("Bottles") == lang::rascalcore::compile::Examples::Bottles::main();
//test bool tstFac() = demo("Fac") == lang::rascalcore::compile::Examples::Fac::main();
//test bool tstFib() = demo("Fib") == lang::rascalcore::compile::Examples::Fib::main();
//test bool tstMoney() = demo("SendMoreMoney") == lang::rascalcore::compile::Examples::SendMoreMoney::main();

// Interpreter does complain about unitialized variables
// RVM also checks for unitialized variables (but this may change when we start checking for this)

//test bool tst() = demo("UninitializedVariable") == lang::rascalcore::compile::Examples::UninitializedVariable::expectedResult;
//test bool tst() = demo("UninitializedVariables") == lang::rascalcore::compile::Examples::UninitializedVariables::expectedResult;

//test bool tstExtract() = demo("RascalExtraction") == lang::rascalcore::compile::Examples::RascalExtraction::main();
//test bool tstMetrics() = demo("RascalMetrics") == lang::rascalcore::compile::Examples::RascalMetrics::main();

