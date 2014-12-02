module experiments::Compiler::Tests::AllRascalTests

import IO;
import Type;
import List;
import DateTime;
import experiments::Compiler::Execute;

// Percentage of succeeded tests, see spreadsheet TestOverview.ods


list[str] basicTests = [
"Booleans",					// OK
"Equality",					// OK
"Functions",				// OK
"Integers",                 // OK
"IO",						// OK

"ListRelations",			// OK
"Lists",                    // OK
"Locations",			    // OK
"Maps",						// OK
"Matching",					// OK
"Memoization",
"Nodes",					// OK
"Relations"	,				// OK
"Sets",						// OK
"SolvedIssues",				// OK
"Strings" , 				// OK
"Tuples"					// OK					
];


list[str] functionalityTests = [

"AccumulatingTests",		// OK
"AliasTests",				// OK
"AnnotationTests",			// OK
"AssignmentTests",			// OK
"BacktrackingTests",		// OK
"CallTests",				// OK
"ComprehensionTests",		// OK, 3 tests fail that correspond to empty enumerations: interpreter gives false, compiler gives true.
"ConcretePatternTests1",	// OK
"ConcretePatternTests2",	// OK
"ConcreteSyntaxTests1",     // OK
"ConcreteSyntaxTests2",     // OK

"DataDeclarationTests",		// OK
"DataTypeTests",			// OK
"DeclarationTests",			// OK
"FunctionCompositionTests",	// OK
"InterpolationTests",
"PatternTests",				// OK
"PatternTestsDescendant",
"PatternTestsList3",
"ProjectionTests", 			// OK
"RangeTests",				// OK, 4 tests fail but this is due to false 1. == 1.0 comparisons.
"ReducerTests",				// OK
"RegExpTests",				// OK
"ScopeTests",				// OK
"SetMatchTests1",           // OK
"SetMatchTests2",           // OK
"StatementTests",			// OK
"SubscriptTests",			// OK
"TryCatchTests",			// OK    				
"VisitTests"				// OK
];


list[str] libraryTests = [

// OK

"lang/csv/CSVIOTests",      // OK
"lang/json/JSONIOTests",    // OK
"BooleanTests",			    // OK
"GraphTests",			    // OK
"IntegerTests",			    // OK
"ListRelationTests",
"ListTests" ,			    // OK
"MapTests",				    // OK
"MathTests"	,			    // OK
"NodeTests",                // OK
"NumberTests",			    // OK
"RelationTests",		    // OK
"SetTests",				    // OK
"StringTests",			    // OK
"ValueIOTests"
];


list[str] files_with_tests =
[
"demo/basic/Ackermann",                             // OK
"demo/basic/Bubble",                                // 1 fails
"demo/basic/Factorial",                             // OK
"demo/common/Calls",                                // OK
"demo/common/ColoredTrees",                         // OK
"demo/common/CountConstructors",                    // OK
"demo/common/Cycles",                               // OK
"demo/common/Derivative",                           // OK
"demo/common/Lift",                                 // OK
"demo/common/StringTemplate",                       // 1 fails
"demo/common/Trans",                                // OK
"demo/common/WordReplacement",                      // OK
"demo/common/WordCount/CountInLine1",               // OK
"demo/common/WordCount/CountInLine2",               // OK
"demo/common/WordCount/CountInLine3",               // OK
"demo/common/WordCount/WordCount",                  // OK
"demo/Dominators",                                  // OK
"demo/lang/Exp/Abstract/Eval",                      // OK
"demo/lang/Exp/Combined/Automatic/Eval",            // 1 fails
"demo/lang/Exp/Combined/Manual/Eval",               // static errors
"demo/lang/Exp/Concrete/NoLayout/Eval",             // 4 fail, parse error, incomplete grammar info
"demo/lang/Exp/Concrete/WithLayout/Eval",           // 4 fail parse error, incomplete grammar info
"demo/lang/Func/Test",                              // 2 fail, "Cannot find a constructor for Prog"
"demo/lang/Lisra/Test",                             // 2 fail
"demo/McCabe",                                      // OK
"demo/ReachingDefs",                                // OK
"demo/Slicing",                                     // OK
"demo/Uninit",                                      // OK
"lang/rascal/format/Escape",                        // OK
"lang/rascal/format/Grammar",                       // 2 fail
"lang/rascal/grammar/definition/Characters",        // 1 fails
"lang/rascal/grammar/Lookahead",                    // 2 fail
"lang/rascal/syntax/tests/ConcreteSyntax",          // static errors
"lang/rascal/syntax/tests/ExpressionGrammars",      // OK
"lang/rascal/syntax/tests/ImplodeTests",            // 2 fail
"lang/rascal/syntax/tests/KnownIssues",             // 1 fail, parse error
"lang/rascal/syntax/tests/ParsingRegressionTests",  // 2 fail
"lang/rascal/syntax/tests/PreBootstrap",            // 2 fail
"lang/rascal/syntax/tests/SolvedIssues",            // 10 fail, parse errors
"lang/yaml/Model",                                  // 1 fails, illegal argument
"util/PriorityQueue",                               // OK
"util/UUID"                                         // OK
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
   
  all_results += runTests(files_with_tests, |rascal:///|);
   
  //all_results += runTests(functionalityTests, |rascal:///lang/rascal/tests/functionality|);
  //all_results += runTests(basicTests, |rascal:///lang/rascal/tests/basic|);
  //all_results += runTests(libraryTests, |rascal:///lang/rascal/tests/library|);
  
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
