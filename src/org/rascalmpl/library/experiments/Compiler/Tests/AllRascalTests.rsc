module experiments::Compiler::Tests::AllRascalTests

import IO;
import Type;
import List;
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
  crashes = [];
  partial_results = [];
  all_results = [];
  all_results += runTests(functionalityTests, |project://rascal-test/src/tests/functionality|);
  all_results += runTests(rascalTests, |project://rascal-test/src/tests|);
  all_results += runTests(libraryTests, |project://rascal-test/src/tests/library|);
  
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