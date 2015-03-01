module experiments::Compiler::Examples::Run

import Prelude;
import experiments::Compiler::Execute;

import experiments::Compiler::Examples::AsType1;
import experiments::Compiler::Examples::AsType2;
import experiments::Compiler::Examples::Bottles;
import experiments::Compiler::Examples::Capture; 
import experiments::Compiler::Examples::NestedFunctions1;
import experiments::Compiler::Examples::NestedFunctions2;
import experiments::Compiler::Examples::NestedFunctions3;
import experiments::Compiler::Examples::NestedFunctions4;
import experiments::Compiler::Examples::E1E2;
import experiments::Compiler::Examples::Fac;
import experiments::Compiler::Examples::Fib;
import experiments::Compiler::Examples::Fail;
import experiments::Compiler::Examples::ListMatch;
import experiments::Compiler::Examples::Odd;
import experiments::Compiler::Examples::SendMoreMoney;
import experiments::Compiler::Examples::SetMatch;
import experiments::Compiler::Examples::SetMatchMix;

import experiments::Compiler::Examples::TestSuite;
import experiments::Compiler::Examples::Template;

import experiments::Compiler::Examples::FunctionWithWhen;

import experiments::Compiler::Examples::RascalRuntimeExceptions;
import experiments::Compiler::Examples::RascalRuntimeExceptionsPlusOverloading;
import experiments::Compiler::Examples::IsDefined;
import experiments::Compiler::Examples::UninitializedVariable;
import experiments::Compiler::Examples::UninitializedVariables;
import experiments::Compiler::Examples::IfDefinedOtherwise;
import experiments::Compiler::Examples::IfDefinedOtherwise2;
import experiments::Compiler::Examples::UseLibrary;

import experiments::Compiler::Examples::WhilePlusBacktracking;
import experiments::Compiler::Examples::IsTrio;
import experiments::Compiler::Examples::Or;
import experiments::Compiler::Examples::AnotherOr;
import experiments::Compiler::Examples::NestedOr;

import experiments::Compiler::Examples::IMP3;

import experiments::Compiler::Examples::KWP1;
import experiments::Compiler::Examples::KWP2;
import experiments::Compiler::Examples::KWP3;
import experiments::Compiler::Examples::KWP4;
import experiments::Compiler::Examples::KWP5;
import experiments::Compiler::Examples::KWP6;
//
import experiments::Compiler::Examples::KWP9;
import experiments::Compiler::Examples::KWP10;

import experiments::Compiler::Examples::Template1;
import experiments::Compiler::Examples::Template2;

import experiments::Compiler::Examples::Closures;

import experiments::Compiler::Examples::Extending;

//import experiments::Compiler::Examples::ExpectedResults;

import experiments::Compiler::Examples::FunctionWithVarargsAndKeyword;
import experiments::Compiler::Examples::ModuleVarInitWithRange;
import experiments::Compiler::Examples::RascalExtraction;

loc base = |std:///experiments/Compiler/Examples/|;

value demo(str example bool debug = false, bool listing=false, bool testsuite=false, bool recompile=true, bool profile=false) =
  execute(base + (example + ".rsc"), [], debug=debug, listing=listing, testsuite=testsuite, recompile=recompile, profile=profile);

test bool tst() = demo("AsType1",recompile=true) == experiments::Compiler::Examples::AsType1::main([]);

test bool tst() = demo("AsType2",recompile=true) == experiments::Compiler::Examples::AsType2::main([]);

// Bug in the interpreter, see issue #542
test bool tst() = demo("Bottles") == experiments::Compiler::Examples::Bottles::main([]);
test bool tst() = demo("Capture") == experiments::Compiler::Examples::Capture::main([]);
test bool tst() = demo("E1E2") == experiments::Compiler::Examples::E1E2::main([]);
test bool tst() = demo("Fac") == experiments::Compiler::Examples::Fac::main([]);
test bool tst() = demo("Fib") == experiments::Compiler::Examples::Fib::main([]);
test bool tst() = demo("ListMatch") == experiments::Compiler::Examples::ListMatch::main([]);
test bool tst() = demo("SetMatch") == experiments::Compiler::Examples::SetMatch::main([]);
test bool tst() = demo("SetMatchMix") == experiments::Compiler::Examples::SetMatchMix::main([]);

test bool tst() = demo("Odd") == experiments::Compiler::Examples::Odd::main([]);
test bool tst() = demo("SendMoreMoney") == experiments::Compiler::Examples::SendMoreMoney::main([]);

test bool tst() = demo("Template") == experiments::Compiler::Examples::Template::main([]);

test bool tst() = demo("FunctionWithWhen") == experiments::Compiler::Examples::FunctionWithWhen::main([]);

test bool tst() = demo("RascalRuntimeExceptions") == experiments::Compiler::Examples::RascalRuntimeExceptions::main([]);
test bool tst() = demo("RascalRuntimeExceptionsPlusOverloading") == experiments::Compiler::Examples::RascalRuntimeExceptionsPlusOverloading::main([]);

test bool tst() = demo("IsDefined") == experiments::Compiler::Examples::IsDefined::main([]);

// Interpreter now complains about unitialized variables
// RVM now assumes that all variables have been initialized
//test bool tst() = demo("UninitializedVariable") == experiments::Compiler::Examples::UninitializedVariable::expectedResult;
//test bool tst() = demo("UninitializedVariables") == experiments::Compiler::Examples::UninitializedVariables::expectedResult;

// Related to the above
//test bool tst() = demo("IfDefinedOtherwise") == experiments::Compiler::Examples::IfDefinedOtherwise::expectedResult;

test bool tst() = demo("IfDefinedOtherwise2") == experiments::Compiler::Examples::IfDefinedOtherwise2::main([]);

test bool tst() = demo("UseLibrary") == experiments::Compiler::Examples::UseLibrary::main([]);

// Overloading resolution & imports
test bool tst() = demo("IMP3") == experiments::Compiler::Examples::IMP3::main([]);

// Fail with labels

test bool tst() = demo("WhilePlusBacktracking") == experiments::Compiler::Examples::WhilePlusBacktracking::main([]);
test bool tst() = demo("IsTrio") == experiments::Compiler::Examples::IsTrio::main([]);
test bool tst() = demo("Or") == experiments::Compiler::Examples::Or::main([]);
test bool tst() = demo("AnotherOr") == experiments::Compiler::Examples::AnotherOr::main([]);
test bool tst() = demo("NestedOr") == experiments::Compiler::Examples::NestedOr::main([]);

// Keyword parameters
test bool tst() = demo("KWP1") == experiments::Compiler::Examples::KWP1::main([]);
test bool tst() = demo("KWP2") == experiments::Compiler::Examples::KWP2::main([]);
test bool tst() = demo("KWP3") == experiments::Compiler::Examples::KWP3::main([]);
test bool tst() = demo("KWP4") == true; // experiments::Compiler::Examples::KWP4::expectedResult;
test bool tst() = demo("KWP5") == true; // == experiments::Compiler::Examples::KWP5::expectedResult;
test bool tst() = demo("KWP6") == experiments::Compiler::Examples::KWP6::main([]);
test bool tst() = demo("KWP7") == true; // == experiments::Compiler::Examples::ExpectedResults::expectedResultKWP7;
test bool tst() = demo("KWP8") == true; // == experiments::Compiler::Examples::ExpectedResults::expectedResultKWP8;
test bool tst1331() = demo("KWP9") == true; // == experiments::Compiler::Examples::KWP9::main([]);
test bool tst13331() = demo("KWP10") == true; //experiments::Compiler::Examples::KWP10::main([]);

// Nested functions
test bool tst() = demo("NestedFunctions1") == experiments::Compiler::Examples::NestedFunctions1::main([]);
test bool tst() = demo("NestedFunctions2") == experiments::Compiler::Examples::NestedFunctions2::main([]);
test bool tst() = demo("NestedFunctions3") == experiments::Compiler::Examples::NestedFunctions3::main([]);
test bool tst() = demo("NestedFunctions4") == experiments::Compiler::Examples::NestedFunctions4::main([]);

test bool tst() = demo("FunctionWithVarargsAndKeyword") == experiments::Compiler::Examples::FunctionWithVarargsAndKeyword::main([]);
test bool tst() = demo("ModuleVarInitWithRange") == experiments::Compiler::Examples::ModuleVarInitWithRange::main([]);

test bool tst() = demo("Template1") == experiments::Compiler::Examples::Template1::main([]);
test bool tst() = demo("Template2") == experiments::Compiler::Examples::Template2::main([]);

test bool tst() = demo("Closures") == experiments::Compiler::Examples::Closures::main([]);

test bool tst() = demo("Extending") == experiments::Compiler::Examples::Extending::main([]);

test bool tst() = demo("RascalExtraction") == experiments::Compiler::Examples::RascalExtraction::main([]);
