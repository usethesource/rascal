module experiments::Compiler::Tests::AllRascalTests

import IO;
import Type;
import List;
import DateTime;
import experiments::Compiler::Execute;

// Percentage of succeeded tests, see spreadsheet TestOverview.ods


list[str] basicTests = [
"Booleans",					// OK, Commented out fromInt test
"Equality",					// OK
							// Added parentheses for ? operator
"Functions",				// OK
"Integers",                 // OK
"IO",						// TC cannot handle ... arguments

"ListRelations",			// TC tests commented out
							// Issue #462	

"Lists",                    // OK
"Locations",			
"Maps",						// OK
"Matching",					// TC, #450
"Memoization",
"Nodes",					// OK
"Relations"	,				// 1 test fails, nested any
"Sets",						// TC complains about tst_group2
                            // 4 tests fails
							// Issue #459
							// Issue #460
"SolvedIssues",				// OK
"Strings" , 				// OK
"Tuples"					// OK					
];


list[str] functionalityTests = [

"AccumulatingTests",		// [15] 2 tests fail: append that crosses function boundary: make tmp scope dependent?
"AliasTests",				// OK
"AnnotationTests",			// OK
"AssignmentTests",			// OK
"BacktrackingTests",		// OK
"CallTests",				// OK
"ComprehensionTests",		// OK, 3 tests fail that correspond to empty enumerations: interpreter gives false, compiler gives true.
"ConcretePatternTests1",	//OK
"ConcretePatternTests2",	//OK
"ConcreteSyntaxTests1",
"ConcreteSyntaxTests2",

"DataDeclarationTests",		// OK
"DataTypeTests",			// OK
"DeclarationTests",			// OK
"FunctionCompositionTests",	// OK
"InterpolationTests",
"PatternTests",				// [OK
"PatternTestsDescendant",
"PatternTestsList3",
"ProjectionTests", 			// OK
"RangeTests",				// OK, 4 tests fail but this is due to false 1. == 1.0 comparisons.
"ReducerTests",				// OK
"RegExpTests",				// OK, Commented out 6: Treatment of redeclared local variables
"ScopeTests",				// OK, Commented out several tests: no shadowing allowed
"SetMatchTests1",
"SetMatchTests2",           // TC cannot handle overloaded constructor
"StatementTests",			// Fail in overloaded constructor gives problem ==> Issue posted
"SubscriptTests",			// OK
"TryCatchTests",			// OK    				
"VisitTests"				// OK
];


list[str] libraryTests = [

// OK

"lang/csv/CSVIOTests",
"lang/json/JSONIOTests",

"BooleanTests",			// OK
"GraphTests",			// OK
"IntegerTests",			// OK
"ListRelationsTests",
"ListTests" ,			// OK
"MapTests",				// OK
"MathTests"	,			// OK
"NodeTests",
"NumberTests",			// OK
"RelationTests",		// OK
"SetTests",				// OK
"StringTests",			// OK
"ValueIOTests"
];


lrel[loc,str] crashes = [];
lrel[loc,str] partial_results = [];

lrel[loc,int,str] runTests(list[str] names, loc base){
 all_test_results = [];
 for(tst <- names){
      prog = base + (tst + ".rsc");
      try {
	      if(lrel[loc,int,str] test_results := execute(prog, [], recompile=false, testsuite=true, listing=false, debug=false)){
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
  all_results += runTests(functionalityTests, |rascal:///lang/rascal/tests/functionality|);
  all_results += runTests(basicTests, |rascal:///lang/rascal/tests/basic|);
  all_results += runTests(libraryTests, |rascal:///lang/rascal/tests/library|);
  
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