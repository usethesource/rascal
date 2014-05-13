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
import experiments::Compiler::Examples::Descent;
import experiments::Compiler::Examples::DescentLiteral;
import experiments::Compiler::Examples::DescentTuple;
import experiments::Compiler::Examples::DescentList;
import experiments::Compiler::Examples::TestSuite;
import experiments::Compiler::Examples::Template;
import experiments::Compiler::Examples::Overloading1;
import experiments::Compiler::Examples::Overloading2;
import experiments::Compiler::Examples::Overloading3;
import experiments::Compiler::Examples::OverloadingMatch;
import experiments::Compiler::Examples::OverloadingPlusBacktracking1;
import experiments::Compiler::Examples::OverloadingPlusBacktracking2;
import experiments::Compiler::Examples::OverloadingDynamicCall;
import experiments::Compiler::Examples::OverloadingPlusVarArgs;
import experiments::Compiler::Examples::OverloadingPlusVarArgsSpecialCase;
import experiments::Compiler::Examples::OverloadingPlusPolymorphism1;
import experiments::Compiler::Examples::OverloadingPlusPolymorphism2;
import experiments::Compiler::Examples::FunctionWithWhen;
import experiments::Compiler::Examples::ExceptionHandling1;
import experiments::Compiler::Examples::ExceptionHandling2;
import experiments::Compiler::Examples::ExceptionHandling3;
import experiments::Compiler::Examples::ExceptionHandling4;
import experiments::Compiler::Examples::ExceptionHandlingFinally1;
import experiments::Compiler::Examples::ExceptionHandlingFinally2;
import experiments::Compiler::Examples::ExceptionHandlingFinally3;
import experiments::Compiler::Examples::ExceptionHandlingFinally4;
import experiments::Compiler::Examples::ExceptionHandlingFinally5;
import experiments::Compiler::Examples::ExceptionHandlingFinally6;
import experiments::Compiler::Examples::ExceptionHandlingFinally7;
import experiments::Compiler::Examples::ExceptionHandlingFinally8;
import experiments::Compiler::Examples::ExceptionHandlingNotHandled;
import experiments::Compiler::Examples::ExceptionHandlingNotHandledSimple;
import experiments::Compiler::Examples::RascalRuntimeExceptions;
import experiments::Compiler::Examples::RascalRuntimeExceptionsPlusOverloading;
import experiments::Compiler::Examples::IsDefined;
import experiments::Compiler::Examples::UninitializedVariable;
import experiments::Compiler::Examples::UninitializedVariables;
import experiments::Compiler::Examples::IfDefinedOtherwise;
import experiments::Compiler::Examples::IfDefinedOtherwise2;
import experiments::Compiler::Examples::UseLibrary;
import experiments::Compiler::Examples::Visit1;
import experiments::Compiler::Examples::Visit1a;
import experiments::Compiler::Examples::Visit2;
import experiments::Compiler::Examples::Visit3;
import experiments::Compiler::Examples::Visit4;
import experiments::Compiler::Examples::Visit5;
import experiments::Compiler::Examples::Visit6;
import experiments::Compiler::Examples::Visit7;
import experiments::Compiler::Examples::Visit8;
import experiments::Compiler::Examples::Visit9;
import experiments::Compiler::Examples::Visit10;
import experiments::Compiler::Examples::Visit11;
import experiments::Compiler::Examples::Visit12;
import experiments::Compiler::Examples::VisitWithWhen;

import experiments::Compiler::Examples::FailWithLabel1;
import experiments::Compiler::Examples::FailWithLabel2;
import experiments::Compiler::Examples::FailWithinElse;
import experiments::Compiler::Examples::WhilePlusBacktracking;
import experiments::Compiler::Examples::IsTrio;
import experiments::Compiler::Examples::Or;
import experiments::Compiler::Examples::AnotherOr;
import experiments::Compiler::Examples::NestedOr;
import experiments::Compiler::Examples::WeirdAppend;

import experiments::Compiler::Examples::IMP3;

import experiments::Compiler::Examples::KWP1;
import experiments::Compiler::Examples::KWP2;
import experiments::Compiler::Examples::KWP3;
import experiments::Compiler::Examples::KWP4;
import experiments::Compiler::Examples::KWP5;
import experiments::Compiler::Examples::KWP6;

import experiments::Compiler::Examples::Template1;
import experiments::Compiler::Examples::Template2;

import experiments::Compiler::Examples::Closures;

import experiments::Compiler::Examples::AnotherFor;

import experiments::Compiler::Examples::Extending;

import experiments::Compiler::Examples::ExpectedResults;

loc base = |rascal:///experiments/Compiler/Examples/|;


value demo(str example, bool debug = false, bool listing=false, bool testsuite=false, bool recompile=true, bool profile=false) =
  execute(base + (example + ".rsc"), [], debug=debug, listing=listing, testsuite=testsuite, recompile=recompile, profile=profile);

test bool tst() = demo("AsType1",recompile=true) == experiments::Compiler::Examples::AsType1::main([]);

// New bug in type checker:
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
test bool tst() = demo("Descent") == experiments::Compiler::Examples::Descent::main([]);
test bool tst() = demo("DescentLiteral") == experiments::Compiler::Examples::DescentLiteral::main([]);
test bool tst() = demo("DescentTuple") == experiments::Compiler::Examples::DescentTuple::main([]);
test bool tst() = demo("DescentList") == experiments::Compiler::Examples::DescentList::main([]);
test bool tst() = demo("Odd") == experiments::Compiler::Examples::Odd::main([]);
test bool tst() = demo("SendMoreMoney") == experiments::Compiler::Examples::SendMoreMoney::main([]);

test bool tst() = demo("Template") == experiments::Compiler::Examples::Template::main([]);
test bool tst() = demo("Overloading1") == experiments::Compiler::Examples::Overloading1::main([]);
test bool tst() = demo("Overloading2") == experiments::Compiler::Examples::Overloading2::main([]) && demo("Overloading1") == demo("Overloading2");
test bool tst() = demo("Overloading3") == experiments::Compiler::Examples::Overloading3::main([]);
test bool tst() = demo("OverloadingMatch") == experiments::Compiler::Examples::OverloadingMatch::main([]);
test bool tst() = demo("OverloadingPlusBacktracking1") == experiments::Compiler::Examples::OverloadingPlusBacktracking1::main([]);
test bool tst() = demo("OverloadingPlusBacktracking2") == experiments::Compiler::Examples::OverloadingPlusBacktracking2::main([]);
test bool tst() = demo("OverloadingDynamicCall") == experiments::Compiler::Examples::OverloadingDynamicCall::main([]);
test bool tst() = demo("OverloadingPlusVarArgs") == experiments::Compiler::Examples::OverloadingPlusVarArgs::main([]);
test bool tst() = demo("OverloadingPlusVarArgsSpecialCase") == experiments::Compiler::Examples::OverloadingPlusVarArgsSpecialCase::main([]);
test bool tst() = demo("OverloadingPlusPolymorphism1") == experiments::Compiler::Examples::OverloadingPlusPolymorphism1::main([]);
test bool tst() = demo("OverloadingPlusPolymorphism2") == experiments::Compiler::Examples::OverloadingPlusPolymorphism2::main([]);
test bool tst() = demo("FunctionWithWhen") == experiments::Compiler::Examples::FunctionWithWhen::main([]);
test bool tst() = demo("ExceptionHandling1") == experiments::Compiler::Examples::ExceptionHandling1::main([]);
test bool tst() = demo("ExceptionHandling2") == experiments::Compiler::Examples::ExceptionHandling2::main([]);
test bool tst() = demo("ExceptionHandling3") == experiments::Compiler::Examples::ExceptionHandling3::main([]);
test bool tst() = demo("ExceptionHandling4") == experiments::Compiler::Examples::ExceptionHandling4::main([]);
test bool tst() = demo("ExceptionHandlingFinally1") == experiments::Compiler::Examples::ExceptionHandlingFinally1::main([]);
test bool tst() = demo("ExceptionHandlingFinally2") == experiments::Compiler::Examples::ExceptionHandlingFinally2::main([]);
test bool tst() = demo("ExceptionHandlingFinally3") == experiments::Compiler::Examples::ExceptionHandlingFinally3::main([]);
test bool tst() = demo("ExceptionHandlingFinally4") == experiments::Compiler::Examples::ExceptionHandlingFinally4::main([]);
test bool tst() = demo("ExceptionHandlingFinally5") == experiments::Compiler::Examples::ExceptionHandlingFinally5::main([]);
test bool tst() = demo("ExceptionHandlingFinally6") == experiments::Compiler::Examples::ExceptionHandlingFinally6::main([]);
test bool tst() = demo("ExceptionHandlingFinally7") == experiments::Compiler::Examples::ExceptionHandlingFinally7::main([]);
test bool tst() = demo("ExceptionHandlingFinally8") == experiments::Compiler::Examples::ExceptionHandlingFinally8::main([]);
test bool tst() = demo("ExceptionHandlingNotHandled") == experiments::Compiler::Examples::ExceptionHandlingNotHandled::expectedResult;
test bool tst() = demo("ExceptionHandlingNotHandledSimple") == experiments::Compiler::Examples::ExceptionHandlingNotHandledSimple::expectedResult;
test bool tst() = demo("RascalRuntimeExceptions") == experiments::Compiler::Examples::RascalRuntimeExceptions::main([]);
test bool tst() = demo("RascalRuntimeExceptionsPlusOverloading") == experiments::Compiler::Examples::RascalRuntimeExceptionsPlusOverloading::main([]);
test bool tst() = demo("IsDefined") == experiments::Compiler::Examples::IsDefined::main([]);

// RVM now assumes that all variables have been initialized
//test bool tst() = demo("UninitializedVariable") == experiments::Compiler::Examples::UninitializedVariable::expectedResult;
//test bool tst() = demo("UninitializedVariables") == experiments::Compiler::Examples::UninitializedVariables::expectedResult;

// Related to the above, but not yet investiagted:
test bool tst() = demo("IfDefinedOtherwise") == experiments::Compiler::Examples::IfDefinedOtherwise::expectedResult;

test bool tst() = demo("IfDefinedOtherwise2") == experiments::Compiler::Examples::IfDefinedOtherwise2::main([]);

test bool tst() = demo("UseLibrary") == experiments::Compiler::Examples::UseLibrary::main([]);

test bool tst1()  = demo("Visit1")  == experiments::Compiler::Examples::Visit1::main([]);
test bool tst1a() = demo("Visit1a") == experiments::Compiler::Examples::Visit1::main([]);
test bool tst2()  = demo("Visit2")  == experiments::Compiler::Examples::Visit2::main([]);
test bool tst3()  = demo("Visit3")  == experiments::Compiler::Examples::Visit3::main([]);
test bool tst4()  = demo("Visit4")  == experiments::Compiler::Examples::Visit4::main([]);
test bool tst5()  = demo("Visit5")  == experiments::Compiler::Examples::Visit5::main([]);
test bool tst6()  = demo("Visit6")  == experiments::Compiler::Examples::Visit6::main([]);
test bool tst7()  = demo("Visit7")  == experiments::Compiler::Examples::Visit7::main([]);
test bool tst8()  = demo("Visit8")  == experiments::Compiler::Examples::Visit8::main([]);
test bool tst9()  = demo("Visit9")  == experiments::Compiler::Examples::Visit9::main([]);
test bool tst10() = demo("Visit10") == experiments::Compiler::Examples::Visit10::expectedResult;
test bool tst11() = demo("Visit11") == experiments::Compiler::Examples::Visit11::main([]);
test bool tst12() = demo("Visit12") == experiments::Compiler::Examples::Visit12::main([]);
test bool tst13() = demo("VisitWithWhen") == experiments::Compiler::Examples::VisitWithWhen::main([]);

// Overloading resolution & imports
test bool tst() = demo("IMP3") == experiments::Compiler::Examples::IMP3::main([]);

// Fail with labels
test bool tst() = demo("FailWithLabel1") == experiments::Compiler::Examples::FailWithLabel1::main([]);
test bool tst() = demo("FailWithLabel2") == experiments::Compiler::Examples::FailWithLabel2::main([]);
test bool tst() = demo("FailWithinElse") == experiments::Compiler::Examples::FailWithinElse::main([]);
test bool tst() = demo("WhilePlusBacktracking") == experiments::Compiler::Examples::WhilePlusBacktracking::main([]);
test bool tst() = demo("IsTrio") == experiments::Compiler::Examples::IsTrio::main([]);
test bool tst() = demo("Or") == experiments::Compiler::Examples::Or::main([]);
test bool tst() = demo("AnotherOr") == experiments::Compiler::Examples::AnotherOr::main([]);
test bool tst() = demo("NestedOr") == experiments::Compiler::Examples::NestedOr::main([]);
test bool tst() = demo("WeirdAppend") == experiments::Compiler::Examples::WeirdAppend::main([]);

// Keyword parameters
test bool tst() = demo("KWP1") == experiments::Compiler::Examples::KWP1::main([]);
test bool tst() = demo("KWP2") == experiments::Compiler::Examples::KWP2::main([]);
test bool tst() = demo("KWP3") == experiments::Compiler::Examples::KWP3::main([]);
test bool tst() = demo("KWP4") == experiments::Compiler::Examples::KWP4::expectedResult;
test bool tst() = demo("KWP5") == experiments::Compiler::Examples::KWP5::expectedResult;
test bool tst() = demo("KWP6") == experiments::Compiler::Examples::KWP6::main([]);
test bool tst() = demo("KWP7") == experiments::Compiler::Examples::ExpectedResults::expectedResultKWP7;
test bool tst() = demo("KWP8") == experiments::Compiler::Examples::ExpectedResults::expectedResultKWP8;

// Nested functions
test bool tst() = demo("NestedFunctions1") == experiments::Compiler::Examples::NestedFunctions1::main([]);
test bool tst() = demo("NestedFunctions2") == experiments::Compiler::Examples::NestedFunctions2::main([]);
test bool tst() = demo("NestedFunctions3") == experiments::Compiler::Examples::NestedFunctions3::main([]);
test bool tst() = demo("NestedFunctions4") == experiments::Compiler::Examples::NestedFunctions4::main([]);

// Bug in the compiler, issue #543
test bool tst() = demo("Template1") == experiments::Compiler::Examples::Template1::main([]);
test bool tst() = demo("Template2") == experiments::Compiler::Examples::Template2::main([]);

test bool tst() = demo("Closures") == experiments::Compiler::Examples::Closures::main([]);

test bool tst() = demo("AnotherFor") == experiments::Compiler::Examples::AnotherFor::main([]);

test bool tst() = demo("Extending") == experiments::Compiler::Examples::Extending::main([]);
