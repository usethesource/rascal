module experiments::Compiler::Tests::AllRascalTests

import IO;
import experiments::Compiler::Execute;

loc base1 = |project:///rascal-test/tests/functionality|;
list[str] functionalityTests = [
//"AccumulatingTests"		// Checking function testAppendHasLexicalScopingClosure
							// |rascal://lang::rascal::types::CheckTypes|(41697,4,<866,22>,<866,26>): Undeclared variable: cFun

//"AnnotationTests"			// r2mu: Compiling boolannotations1
							// |rascal://experiments::Compiler::Rascal2muRascal::RascalExpression|(11015,17,<263,74>,<263,91>): The called signature: translate(Symbol (...)),
							// does not match any of the declared (overloaded) signature patterns:

//"AssignmentTests"			// r2mu: Compiling testInteger5
							//|rascal://experiments::Compiler::Rascal2muRascal::RascalStatement|(16250,8,<362,99>,<362,107>): NoSuchKey("?=")

//"BackTrackingTests"			// |rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(4067,1,<134,20>,<134,21>): NoSuchKey(|project://rascal-test/src/tests/functionality/BackTrackingTests.rsc|(1176,1,<26,81>,<26,82>))

//"CallTests"					// |rascal://lang::rascal::types::CheckTypes|(277943,14,<5497,75>,<5497,89>): The called signature: isVarArgs(sort("Signature")),
							// does not match the declared signature:	bool isVarArgs(sort("Signature")); (concrete pattern); bool isVarArgs(sort("Signature")); (concrete pattern);  bool isVarArgs(sort("Signature")); (concrete pattern); bool isVarArgs(sort("Signature")); (concrete pattern);  

//"ComprehensionTests"		// error("Type DuplicateKey not declared",|project://rascal/src/org/rascalmpl/library/List.rsc|(29955,12,<1437,67>,<1437,79>))
							// error("Cannot match an expression of type: void() against a pattern of type tuple([int(),int()])",|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(19118,14,<346,61>,<346,75>))
							// error("Cannot match an expression of type: int() against a pattern of type cons(adt(\"TREE\",[]),\"i\",[label(\"N\",int())])",|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(7902,1,<161,36>,<161,37>))
							// error("Name Y is not in scope",|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(19113,1,<346,56>,<346,57>))
							// error("Type IndexOutOfBoundsError not declared",|project://rascal/src/org/rascalmpl/library/List.rsc|(26211,21,<1284,56>,<1284,77>))
							// error("Type EmptyList not declared",|project://rascal/src/org/rascalmpl/library/List.rsc|(6342,9,<286,57>,<286,66>))
							// error("Type EmptyList not declared",|project://rascal/src/org/rascalmpl/library/List.rsc|(15508,9,<723,36>,<723,45>))
							// error("Type IndexOutOfBounds not declared",|project://rascal/src/org/rascalmpl/library/List.rsc|(5806,16,<255,54>,<255,70>))
							// error("Remainder not defined on TREE i : (int N) and int",|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(7917,5,<161,51>,<161,56>))
							// error("Type DuplicateKey not declared",|project://rascal/src/org/rascalmpl/library/List.rsc|(28949,12,<1394,66>,<1394,78>))
							// error("Name X is not in scope",|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(19111,1,<346,54>,<346,55>))
							// error("Type EmptyList not declared",|project://rascal/src/org/rascalmpl/library/List.rsc|(5660,9,<251,41>,<251,50>))
							// error("Type EmptyList not declared",|project://rascal/src/org/rascalmpl/library/List.rsc|(11447,9,<541,35>,<541,44>))
							// error("Cannot match an expression of type: void() against a pattern of type tuple([int(),int()])",|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(19272,14,<350,61>,<350,75>))
							// error("Name Y is not in scope",|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(19267,1,<350,56>,<350,57>))
							// error("Type EmptyList not declared",|project://rascal/src/org/rascalmpl/library/List.rsc|(19660,9,<926,52>,<926,61>))
							// error("Remainder not defined on TREE i : (int N) and int",|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(7931,5,<161,65>,<161,70>))
							// error("Name X is not in scope",|project://rascal-test/src/tests/functionality/ComprehensionTests.rsc|(19265,1,<350,54>,<350,55>))
							// error("Type EmptyList not declared",|project://rascal/src/org/rascalmpl/library/List.rsc|(32316,9,<1575,35>,<1575,44>))
							// error("Type IndexOutOfBounds not declared",|project://rascal/src/org/rascalmpl/library/List.rsc|(9315,16,<424,66>,<424,82>))

//"DataDeclarationTests"	// Checking function parameterized3
							// |rascal://Type|(19722,49,<356,81>,<356,130>): "Length of symbol list and label list much match"

"DataTypeTests"				// Most test pass, still todo:
							// 1. some loc comparisons fail.
							// 2. 3 set match tests fails.
							// 3. Implement range outside enumerator.
							// 4. Ifdefined not yet implemented
							// 5. Commented out two problematic ||s.
							
//"DeclarationTests"		// error("Cannot re-declare name that is already declared in the current function or closure",|project://rascal-test/src/tests/functionality/DeclarationTests.rsc|(985,1,<31,18>,<31,19>))
						// error("Cannot re-declare name that is already declared in the current function or closure",|project://rascal-test/src/tests/functionality/DeclarationTests.rsc|(1071,1,<35,14>,<35,15>))
						// error("Cannot re-declare name that is already declared in the current function or closure",|project://rascal-test/src/tests/functionality/DeclarationTests.rsc|(1167,1,<39,24>,<39,25>))

//"PatternTests"			// Checking function matchADTwithKeywords4
						// |rascal://lang::rascal::types::CheckTypes|(138946,19,<2744,21>,<2744,40>): The called signature: checkExp(sort("Expression"), Configuration),
						// does not match the declared signature:	CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  

//"RangeTests"			// |rascal://experiments::Compiler::Rascal2muRascal::RascalExpression|(4295,40,<106,87>,<106,127>): "range outside enumerator not supported"

// "ReducerTests"		// OK

//"StatementTests"		// D d(int i) { if (i % 2 == 0) fail d; else return d();}
						// gives error("Target label not defined",|project://rascal-test/src/tests/functionality/StatementTests.rsc|(3931,7,<86,31>,<86,38>))
						// commented out
						
						// |rascal://experiments::Compiler::RVM::Run|(217,264,<12,0>,<14,153>): Java("RuntimeException","PANIC: undefined label FAIL_loop")
						
//"SubscriptTests"		// contains static errors
];



list[str] rascalTests = [
//"BacktrackingTests",
//"Booleans",
//"Equality",
//"Functions",
//"Integers",
//"IO",
//"ListRelations",
//"Lists",
//"Maps",
//"Matching",
//"Memoization",
//"Nodes",
//"Relations",
"MySets",
//"SolvedIssues",
//"Strings",  // Needs slice assignables in tc
"Tuples"
];

loc base = |rascal:///lang/rascal/tests/|;
int nsuccess = 0;
int nfail = 0;

void runTests(list[str] names, loc base){
 for(tst <- names){
      println("***** <tst> *****");
      if(<s, f> := execute(base + (tst + ".rsc"), [], recompile=true, testsuite=true)){
         nsuccess += s;
         nfail += f;
      } else {
         println("testsuite did not return a tuple");
      }
  }
}
  
value main(list[value] args){
  nsuccess = 0;
  nfail = 0;
  runTests(functionalityTests, |project://rascal-test/src/tests/functionality|);
  //runTests(rascalTests, |rascal:///lang/rascal/tests/|);
  println("Overall summary: <nsuccess + nfail> tests executed, <nsuccess> succeeded, <nfail> failed");
  return nfail == 0;
}