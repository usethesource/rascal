module experiments::Compiler::Tests::AllRascalTests

import IO;
import Type;
import List;
import DateTime;
import experiments::Compiler::Execute;

loc base1 = |project:///rascal-test/tests/functionality|;

// Percentage of succeeded tests, see spreadsheet TestOverview.ods

list[str] functionalityTests = [

"AccumulatingTests",		// [15] 2 tests fail: append that crosses function boundary: make tmp scope dependent?
"AliasTests",				// OK
"AnnotationTests",			// OK
"AssignmentTests",			// OK
"BackTrackingTests",		// OK
"CallTests",				// [58] keyword parameters Issue #456
"ComprehensionTests",		// OK
							 //3 tests fail that correspond to empty enumerations: interpreter gives false, compiler gives true.
"ConcretePatternTests1",	//OK
"ConcretePatternTests2",	//OK

"DataDeclarationTests",		// OK
"DataTypeTests",			// OK
"DeclarationTests",			// OK, these are conscious changes in the scoping rules
							 //error("Cannot re-declare name that is already declared in the current function or closure",|project://rascal-test/src/tests/functionality/DeclarationTests.rsc|(985,1,<31,18>,<31,19>))
							 //error("Cannot re-declare name that is already declared in the current function or closure",|project://rascal-test/src/tests/functionality/DeclarationTests.rsc|(1071,1,<35,14>,<35,15>))
							 //error("Cannot re-declare name that is already declared in the current function or closure",|project://rascal-test/src/tests/functionality/DeclarationTests.rsc|(1167,1,<39,24>,<39,25>))
"FunctionCompositionTests",	// Issue #468	
"PatternTests",				// [420] Issue #458
"PatternTestsList3",
"PatternTestsDescendant",
"ProjectionTests", 			// OK
"RangeTests",				// OK, 4 tests fail but this is due to false 1. == 1.0 comparisons.
"ReducerTests",				// OK
"RegExpTests",				// OK
 							 //Commented out 6: Treatment of redeclared local variables
"ScopeTests",				// OK
							 //Commented out several tests: no shadowing allowed
"StatementTests",			// Fail in overloaded constructor gives problem ==> Issue posted
"SubscriptTests",			// OK
"TryCatchTests",			// OK    				
"VisitTests"				// 13 fail [98]
];


list[str] rascalTests = [
"BacktrackingTests",		// OK
"Booleans",					// OK
							// Commented out fromInt test
"Equality",					// OK
							// Added parentheses for ? operator
"Functions",				// OK
"Integers",					// OK
"IO",						// OK
"Lists",					// OK
"ListRelations",			// TC tests commented out
							// Issue #462
"Maps",						// OK
"Matching",					// TC, #450
"Nodes",					// OK
"Relations"	,				// 1 test fails, nested any
"Sets",						// 4 tests fails
							// Issue #459
							// Issue #460
"SolvedIssues",				// OK
"Strings" , 				// OK
"StringTests",				// OK
"Tuples"					// OK					
];

list[str] libraryTests = [

// OK

"BooleanTests",			// OK
"GraphTests",			// OK
"IntegerTests",			// OK
"ListTests" ,			// OK
"MapTests",				// OK
"MathTests"	,			// OK
"NumberTests",			// OK
"RelationTests",		// OK
"SetTests",				// OK
"StringTests"			// OK
];
/*
FAILED TESTS:
|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(23580,114,<422,4>,<422,118>): FALSE 
|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(23137,86,<414,4>,<414,90>): FALSE 
|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(22909,116,<411,4>,<411,120>): FALSE 
|project://rascal-test/src/tests/functionality/StatementTests.rsc|(3898,39,<86,4>,<86,43>): FALSE 
|project://rascal-test/src/tests/functionality/VisitTests.rsc|(12253,115,<268,2>,<268,117>): FALSE 
|project://rascal-test/src/tests/functionality/VisitTests.rsc|(12138,112,<267,2>,<267,114>): FALSE 
|project://rascal-test/src/tests/functionality/VisitTests.rsc|(11658,119,<259,2>,<259,121>): FALSE 
|project://rascal-test/src/tests/functionality/VisitTests.rsc|(11539,116,<258,2>,<258,118>): FALSE 
|project://rascal-test/src/tests/functionality/VisitTests.rsc|(11083,89,<250,2>,<250,91>): FALSE 
|project://rascal-test/src/tests/functionality/VisitTests.rsc|(11000,80,<249,2>,<249,82>): FALSE 
|project://rascal-test/src/tests/functionality/VisitTests.rsc|(10630,79,<241,2>,<241,81>): FALSE 
|project://rascal-test/src/tests/functionality/VisitTests.rsc|(10553,74,<240,2>,<240,76>): FALSE 
|project://rascal-test/src/tests/functionality/VisitTests.rsc|(10199,86,<233,2>,<233,88>): FALSE 
|project://rascal-test/src/tests/functionality/VisitTests.rsc|(10116,80,<232,2>,<232,82>): FALSE 
|project://rascal-test/src/tests/BacktrackingTests.rsc|(6715,940,<184,0>,<204,1>): FALSE UninitializedVariable(11)
|project://rascal-test/src/tests/BacktrackingTests.rsc|(5709,1004,<162,0>,<182,1>): FALSE UninitializedVariable(11)
|project://rascal-test/src/tests/BacktrackingTests.rsc|(4881,826,<140,0>,<160,1>): FALSE UninitializedVariable(4)
|project://rascal-test/src/tests/BacktrackingTests.rsc|(4013,866,<118,0>,<138,1>): FALSE UninitializedVariable(10)
|project://rascal-test/src/tests/BacktrackingTests.rsc|(3177,834,<96,0>,<116,1>): FALSE 
|project://rascal-test/src/tests/Matching.rsc|(162,77,<14,0>,<17,1>): FALSE 
|project://rascal-test/src/tests/Nodes.rsc|(2908,328,<136,0>,<145,1>): FALSE PANIC: (instruction execution): instruction: CALLPRIM node_slice, 4; message: null with arguments: "pOUT"(()) 
|project://rascal-test/src/tests/Nodes.rsc|(322,110,<19,0>,<25,1>): FALSE PANIC: (instruction execution): instruction: CALLMUPRIM get_name_and_children_and_keyword_params_as_map, 1; message: null
|project://rascal-test/src/tests/Relations.rsc|(2817,148,<90,0>,<92,83>): FALSE  with arguments: {<537121109,-449751468>,<-1041396077,-1371317604>} 

IGNORED TESTS:
|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(7797,75,<162,4>,<163,48>): IGNORED
|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(7717,75,<160,4>,<161,48>): IGNORED
|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(7637,75,<158,4>,<159,48>): IGNORED
|project://rascal-test/src/tests/functionality/RangeTests.rsc|(2492,80,<51,4>,<51,84>): IGNORED
|project://rascal-test/src/tests/functionality/RangeTests.rsc|(2414,72,<50,4>,<50,76>): IGNORED
|project://rascal-test/src/tests/functionality/RangeTests.rsc|(2328,80,<49,4>,<49,84>): IGNORED
|project://rascal-test/src/tests/functionality/RangeTests.rsc|(2247,75,<48,4>,<48,79>): IGNORED

SUMMARY: 2868 tests executed; 23 failed; 7 ignored

CRASHED TESTS:
|project://rascal-test/src/tests/functionality/FunctionCompositionTests.rsc|: Java("RuntimeException","In function tests::functionality::FunctionCompositionTests/nonDeterministicChoiceAndNormalComposition2()#0 : No enum constant org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive.func_add_func")
*/

lrel[loc,str] crashes = [];
lrel[loc,str] partial_results = [];

lrel[loc,int,str] runTests(list[str] names, loc base){
 all_test_results = [];
 for(tst <- names){
      prog = base + (tst + ".rsc");
      try {
	      if(lrel[loc,int,str] test_results := execute(prog, [], recompile=true, testsuite=true, listing=false, debug=false)){
	         s = makeTestSummary(test_results);
	         println("TESTING <prog>: <s>");
	         partial_results += <prog, s>;
	         all_test_results += test_results;
	      } else {
	         println("testsuite did not return a list of test results");
	      }
      } catch e: {
        crashes += <prog, "<e>">;
      }
  }
 
  return all_test_results;
}
  
value main(list[value] args){
  timestamp = now();
  crashes = [];
  partial_results = [];
  all_results = [];
  all_results += runTests(functionalityTests, |project://rascal-test/src/tests/functionality|);
  all_results += runTests(rascalTests, |project://rascal-test/src/tests|);
  all_results += runTests(libraryTests, |project://rascal-test/src/tests/library|);
  
  println("TESTS RUN AT <timestamp>");
  println("\nRESULTS PER FILE:");
  for(<prog, s> <- partial_results)
      println("<prog>: <s>");
  
  println("\nFailed/IGNORED TESTS:");
  printTestReport(all_results);
  
  if(size(crashes) > 0){
     println("\nCRASHED TESTS:");
     for(<prog, e> <- crashes)
         println("<prog>: <e>");
  }
  
  return size(all_results[_,0]) == 0;
}