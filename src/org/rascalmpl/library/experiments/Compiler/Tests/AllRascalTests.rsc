module experiments::Compiler::Tests::AllRascalTests

import IO;
import Type;
import List;
import DateTime;
import experiments::Compiler::Execute;

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
"SetMatchTests1",
"SetMatchTests2",           // TC
"StatementTests",			// Fail in overloaded constructor gives problem ==> Issue posted
"SubscriptTests",			// OK
"TryCatchTests",			// OK    				
"VisitTests"				// 13 fail [98]
];


list[str] basicTests = [
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
//"ListRelationsTests",
"ListTests" ,			// OK
"MapTests",				// OK
"MathTests"	,			// OK
"NumberTests",			// OK
"RelationTests",		// OK
"SetTests",				// OK
"StringTests"			// OK
];

/*
TESTS RUN AT $2014-02-08T00:22:48.163+01:00$

FAILED TESTS:
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
|project://rascal-test/src/tests/ListRelations.rsc|(2949,149,<94,0>,<96,83>): FALSE  with arguments: [<-219637694,-221094360>,<-548755640,-1212156571>,<1324502974,773893189>] 
|project://rascal-test/src/tests/Matching.rsc|(283,77,<20,0>,<23,1>): FALSE 
|project://rascal-test/src/tests/Nodes.rsc|(2908,328,<136,0>,<145,1>): FALSE  with arguments: "������"($1348-04-05T10:17:16.706+01:00$,{"\"\"���","\"\\\"\\\"���\"���",""},({465151638,-1811667661}:|tmp:///wI|,{}:|tmp:///|)) 
|project://rascal-test/src/tests/Nodes.rsc|(2536,182,<122,0>,<127,1>): FALSE  with arguments: "h9Bc"("7c"($0228-05-07T03:12:04.393+01:00$,""),$2015-06-02T05:44:42.155+01:00$,{-1620187633,-1831381091,-275570814},()) 
|project://rascal-test/src/tests/Relations.rsc|(2201,151,<70,0>,<72,85>): FALSE  with arguments: {<1012834307,905272390>,<-1511508561,-1930750599>} 
|project://rascal-test/src/tests/Relations.rsc|(871,185,<27,0>,<30,47>): FALSE  with arguments: {<{},0.24525990529456354,""()>} 

IGNORED TESTS:
|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(7797,75,<162,4>,<163,48>): IGNORED
|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(7717,75,<160,4>,<161,48>): IGNORED
|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(7637,75,<158,4>,<159,48>): IGNORED
|project://rascal-test/src/tests/functionality/RangeTests.rsc|(2492,80,<51,4>,<51,84>): IGNORED
|project://rascal-test/src/tests/functionality/RangeTests.rsc|(2414,72,<50,4>,<50,76>): IGNORED
|project://rascal-test/src/tests/functionality/RangeTests.rsc|(2328,80,<49,4>,<49,84>): IGNORED
|project://rascal-test/src/tests/functionality/RangeTests.rsc|(2247,75,<48,4>,<48,79>): IGNORED

SUMMARY: 2835 tests executed; 21 failed; 7 ignored

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