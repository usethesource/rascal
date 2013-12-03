module experiments::Compiler::Tests::AllRascalTests

import IO;
import experiments::Compiler::Execute;

loc base1 = |project:///rascal-test/tests/functionality|;
list[str] functionalityTests = [

//"AnnotationTests"			// OK
//"AssignmentTests"			// OK
//"ReducerTests"			// OK
//"DeclarationTests"		// OK, these are conscious changes in the scoping rules
							// error("Cannot re-declare name that is already declared in the current function or closure",|project://rascal-test/src/tests/functionality/DeclarationTests.rsc|(985,1,<31,18>,<31,19>))
							// error("Cannot re-declare name that is already declared in the current function or closure",|project://rascal-test/src/tests/functionality/DeclarationTests.rsc|(1071,1,<35,14>,<35,15>))
							// error("Cannot re-declare name that is already declared in the current function or closure",|project://rascal-test/src/tests/functionality/DeclarationTests.rsc|(1167,1,<39,24>,<39,25>))
//"RangeTests"				// OK
							// 4 tests fail but this a deliberate improvement over the interpreter result.

//"AccumulatingTests"		// 2 tests fail: append that crosses function boundary: make tmp scope dependent?

"BackTrackingTests"		// 13 tests fail 

//"CallTests"				// keyword parameters

//"ComprehensionTests"		// 15 tests fail

//"DataDeclarationTests"	//error("Initializer type Maybe[&T \<: value] not assignable to variable of type Maybe[void]",|project://rascal-test/src/tests/functionality/DataDeclarationTests.rsc|(5906,10,<104,53>,<104,63>))
							//error("Initializer type Exp1[&T \<: value] not assignable to variable of type Exp1[int]",|project://rascal-test/src/tests/functionality/DataDeclarationTests.rsc|(5772,11,<100,58>,<100,69>))
							//error("Initializer type &T \<: value not assignable to variable of type str",|project://rascal-test/src/tests/functionality/DataDeclarationTests.rsc|(5535,12,<95,68>,<95,80>))
							//error("Initializer type &T \<: value not assignable to variable of type str",|project://rascal-test/src/tests/functionality/DataDeclarationTests.rsc|(5061,12,<89,68>,<89,80>))
							// Issue posted
							
//"DataTypeTests"			// 8 tests fail: escapes in string templates
							

//"PatternTests"			// Uses keyword parameters
							// Checking function matchADTwithKeywords4
							// |rascal://lang::rascal::types::CheckTypes|(140533,19,<2772,21>,<2772,40>): The called signature: checkExp(sort("Expression"), Configuration),
							// does not match the declared signature:	CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  


//"StatementTests"			// Fail in overloaded constructor gives problem ==> Issue posted
						
//"SubscriptTests"			// set-based subscripts of relations ==> Issue posted.
];


list[str] rascalTests = [

//"Integers"				// OK
//"Tuples"					// OK
//"SolvedIssues"			// OK

//"BacktrackingTests"		// error("Name s is not in scope",|project://rascal-test/src/tests/BacktrackingTests.rsc|(8573,1,<223,10>,<223,11>))
							//error("Name L is not in scope",|project://rascal-test/src/tests/BacktrackingTests.rsc|(8246,1,<218,13>,<218,14>))
							//error("Name s is not in scope",|project://rascal-test/src/tests/BacktrackingTests.rsc|(8315,1,<219,9>,<219,10>))
							//error("Name l8 is not in scope",|project://rascal-test/src/tests/BacktrackingTests.rsc|(9056,2,<233,6>,<233,8>))
							//error("Name r is not in scope",|project://rascal-test/src/tests/BacktrackingTests.rsc|(8317,1,<219,11>,<219,12>))
							//error("Name l13 is not in scope",|project://rascal-test/src/tests/BacktrackingTests.rsc|(9259,3,<238,6>,<238,9>))
							//error("Name r is not in scope",|project://rascal-test/src/tests/BacktrackingTests.rsc|(8575,1,<223,12>,<223,13>))
							//error("Name L is not in scope",|project://rascal-test/src/tests/BacktrackingTests.rsc|(8319,1,<219,13>,<219,14>))
							//error("Name l9 is not in scope",|project://rascal-test/src/tests/BacktrackingTests.rsc|(9071,2,<234,6>,<234,8>))
							//error("Name L is not in scope",|project://rascal-test/src/tests/BacktrackingTests.rsc|(8577,1,<223,14>,<223,15>))
							//error("Cannot assign pattern of type list[str] to non-inferred variable of type list[int]",|project://rascal-test/src/tests/BacktrackingTests.rsc|(8324,28,<219,18>,<219,46>))
							//error("Name s is not in scope",|project://rascal-test/src/tests/BacktrackingTests.rsc|(8242,1,<218,9>,<218,10>))
							//error("Cannot assign pattern of type list[value] to non-inferred variable of type list[str]",|project://rascal-test/src/tests/BacktrackingTests.rsc|(8582,32,<223,19>,<223,51>))
							//error("Name r is not in scope",|project://rascal-test/src/tests/BacktrackingTests.rsc|(8244,1,<218,11>,<218,12>))
							//error("Cannot assign pattern of type list[int] to non-inferred variable of type list[str]",|project://rascal-test/src/tests/BacktrackingTests.rsc|(8251,28,<218,18>,<218,46>))
							// Issue posted
							
//"Booleans"				// 3 tests fail
						// Commented out fromInt test

//"Equality"				// 2 tests fail
						// Added parentheses for ?operator
						
//"Functions"			// Checking function callKwp
						// |rascal://lang::rascal::types::CheckTypes|(206380,13,<4071,16>,<4071,29>): The called signature: checkExp(sort("Expression"), Configuration),
						// does not match the declared signature:	CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  

//"IO"					// adding grammar for ValueIO
						// |rascal://experiments::Compiler::RVM::Run|(217,715,<12,0>,<23,28>): Java("RuntimeException","PANIC: (instruction execution): null")
						//	at org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM.executeProgram(|file:///RVM.java|(0,0,<1381,0>,<1381,0>))

//"ListRelations"		// error("Name b is not in scope",|project://rascal-test/src/tests/ListRelations.rsc|(1014,1,<29,57>,<29,58>))
						//error("Multiple functions found which could be applied",|project://rascal-test/src/tests/ListRelations.rsc|(2163,9,<68,20>,<68,29>))
						//error("Name a is not in scope",|project://rascal-test/src/tests/ListRelations.rsc|(2385,1,<72,81>,<72,82>))
						//error("Name z is not in scope",|project://rascal-test/src/tests/ListRelations.rsc|(250,1,<11,52>,<11,53>))
						//error("Name a is not in scope",|project://rascal-test/src/tests/ListRelations.rsc|(1011,1,<29,54>,<29,55>))
						//error("Multiple functions found which could be applied",|project://rascal-test/src/tests/ListRelations.rsc|(971,9,<29,14>,<29,23>))
						//error("Multiple functions found which could be applied",|project://rascal-test/src/tests/ListRelations.rsc|(531,9,<19,10>,<19,19>))
						//error("Multiple functions found which could be applied",|project://rascal-test/src/tests/ListRelations.rsc|(2329,9,<72,25>,<72,34>))
						//error("lrel[&A \<: value, &B \<: value, &B \<: value, &C \<: value, &D \<: value] and lrel[&A \<: value, &B \<: value] incomparable",|project://rascal-test/src/tests/ListRelations.rsc|(708,13,<23,17>,<23,30>))
						//error("Function of type fun list[&T1 \<: value](lrel[&T0 \<: value, &T1 \<: value]) cannot be called with argument types (list[&A \<: value])",|project://rascal-test/src/tests/ListRelations.rsc|(226,8,<11,28>,<11,36>))
						//error("lrel[&A \<: value, &B \<: value, &B \<: value, &C \<: value, &D \<: value] and lrel[&B \<: value, &C \<: value, &D \<: value] incomparable",|project://rascal-test/src/tests/ListRelations.rsc|(674,13,<22,17>,<22,30>))
						//error("Name a is not in scope",|project://rascal-test/src/tests/ListRelations.rsc|(237,1,<11,39>,<11,40>))
						//error("Function of type fun lrel[&T1 \<: value, &T2 \<: value](lrel[&T0 \<: value, &T1 \<: value, &T2 \<: value]) cannot be called with argument types (list[&A \<: value])",|project://rascal-test/src/tests/ListRelations.rsc|(226,8,<11,28>,<11,36>))
						//error("Function of type fun lrel[&T1 \<: value, &T2 \<: value, &T3 \<: value](lrel[&T0 \<: value, &T1 \<: value, &T2 \<: value, &T3 \<: value]) cannot be called with argument types (list[&A \<: value])",|project://rascal-test/src/tests/ListRelations.rsc|(226,8,<11,28>,<11,36>))
						//error("Function of type fun lrel[&T1 \<: value, &T2 \<: value, &T3 \<: value, &T4 \<: value](lrel[&T0 \<: value, &T1 \<: value, &T2 \<: value, &T3 \<: value, &T4 \<: value]) cannot be called with argument types (list[&A \<: value])",|project://rascal-test/src/tests/ListRelations.rsc|(226,8,<11,28>,<11,36>))
						//error("Name z is not in scope",|project://rascal-test/src/tests/ListRelations.rsc|(240,1,<11,42>,<11,43>))
						//error("Multiple functions found which could be applied",|project://rascal-test/src/tests/ListRelations.rsc|(2352,9,<72,48>,<72,57>))
						//error("Name a is not in scope",|project://rascal-test/src/tests/ListRelations.rsc|(1005,1,<29,48>,<29,49>))
						//error("Name a is not in scope",|project://rascal-test/src/tests/ListRelations.rsc|(441,1,<16,39>,<16,40>))
						//error("Name c is not in scope",|project://rascal-test/src/tests/ListRelations.rsc|(1017,1,<29,60>,<29,61>))

//"Lists"				// error("Could not instantiate type variables in type fun list[&U \<: value](list[&T \<: value], fun &U \<: value(&T \<: value)) with argument types (list[int],fun int(int))",|project://rascal-test/src/tests/Lists.rsc|(11493,15,<340,9>,<340,24>))
						//error("Could not instantiate type variables in type fun set[&U \<: value](set[&T \<: value], fun &U \<: value(&T \<: value)) with argument types (list[int],fun int(int))",|project://rascal-test/src/tests/Lists.rsc|(11493,15,<340,9>,<340,24>))
						//error("set[int] and list[value] incomparable",|project://rascal-test/src/tests/Lists.rsc|(14053,17,<425,2>,<425,19>))
						//error("Unexpected type: type of body expression, int, must be a subtype of the function return type, bool",|project://rascal-test/src/tests/Lists.rsc|(1742,35,<50,30>,<50,65>))

//"Maps"				// Compilation of ListRelation

//"Matching"				//getFUID: fT2, failure({error("Type of pattern could not be computed",|project://rascal-test/src/tests/Matching.rsc|(828,17,<42,10>,<42,27>)),error("Could not calculate function type because of errors calculating the parameter types",|project://rascal-test/src/tests/Matching.rsc|(819,27,<42,1>,<42,28>)),error("Multiple constructors match this pattern, add additional type annotations",|project://rascal-test/src/tests/Matching.rsc|(828,17,<42,10>,<42,27>))})
						//|rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(14990,5,<374,27>,<374,32>): NoSuchField("parameters")

//"Memoization"			// Does not exist

//"Nodes"					// 10 test fail
	
//"Relations"			//error("Name a is not in scope",|project://rascal-test/src/tests/Relations.rsc|(226,1,<11,39>,<11,40>))
						//error("Name z is not in scope",|project://rascal-test/src/tests/Relations.rsc|(239,1,<11,52>,<11,53>))
						//error("Name z is not in scope",|project://rascal-test/src/tests/Relations.rsc|(229,1,<11,42>,<11,43>))
						//error("Type EmptySet not declared",|project://rascal/src/org/rascalmpl/library/Set.rsc|(9490,8,<477,45>,<477,53>))
						//error("rel[&A \<: value, &B \<: value, &B \<: value, &C \<: value, &D \<: value] and rel[&A \<: value, &B \<: value] incomparable",|project://rascal-test/src/tests/Relations.rsc|(688,13,<23,17>,<23,30>))
						//error("Could not instantiate type variables in type fun rel[&T1 \<: value, &T2 \<: value](rel[&T0 \<: value, &T1 \<: value, &T2 \<: value]) with argument types (set[&A \<: value])",|project://rascal-test/src/tests/Relations.rsc|(215,8,<11,28>,<11,36>))
						//error("Name a is not in scope",|project://rascal-test/src/tests/Relations.rsc|(424,1,<16,39>,<16,40>))
						//error("Could not instantiate type variables in type fun rel[&T1 \<: value, &T2 \<: value, &T3 \<: value](rel[&T0 \<: value, &T1 \<: value, &T2 \<: value, &T3 \<: value]) with argument types (set[&A \<: value])",|project://rascal-test/src/tests/Relations.rsc|(215,8,<11,28>,<11,36>))
						//error("Type MultipleKey not declared",|project://rascal/src/org/rascalmpl/library/Set.rsc|(12267,11,<601,58>,<601,69>))
						//error("rel[&A \<: value, &B \<: value, &B \<: value, &C \<: value, &D \<: value] and rel[&B \<: value, &C \<: value, &D \<: value] incomparable",|project://rascal-test/src/tests/Relations.rsc|(654,13,<22,17>,<22,30>))
						//error("Could not instantiate type variables in type fun set[&T1 \<: value](rel[&T0 \<: value, &T1 \<: value]) with argument types (set[&A \<: value])",|project://rascal-test/src/tests/Relations.rsc|(215,8,<11,28>,<11,36>))
						//error("Type EmptySet not declared",|project://rascal/src/org/rascalmpl/library/Set.rsc|(2157,8,<76,45>,<76,53>))
						//error("Type EmptySet not declared",|project://rascal/src/org/rascalmpl/library/Set.rsc|(10000,8,<495,62>,<495,70>))
						//error("Could not instantiate type variables in type fun rel[&T1 \<: value, &T2 \<: value, &T3 \<: value, &T4 \<: value](rel[&T0 \<: value, &T1 \<: value, &T2 \<: value, &T3 \<: value, &T4 \<: value]) with argument types (set[&A \<: value])",|project://rascal-test/src/tests/Relations.rsc|(215,8,<11,28>,<11,36>))


//"Sets"				//error("Name c is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2401,1,<75,55>,<75,56>))
						//error("Name c is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2313,1,<74,32>,<74,33>))
						//error("Name classes is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2383,7,<75,37>,<75,44>))
						//error("Name classes is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2335,7,<74,54>,<74,61>))
						//error("Name classes is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2305,7,<74,24>,<74,31>))
						//error("Name classes is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2291,7,<74,10>,<74,17>))
						//error("Name e is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2441,1,<75,95>,<75,96>))
						//error("Name classes is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2247,7,<70,29>,<70,36>))
						//error("Name c is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2190,1,<69,16>,<69,17>))
						//error("Name e is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2426,1,<75,80>,<75,81>))
						//error("Name e is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2412,1,<75,66>,<75,67>))
						//error("Name e is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2326,1,<74,45>,<74,46>))
						//error("Could not instantiate type variables in type fun map[&K \<: value, set[&V \<: value]](set[&V \<: value], fun &K \<: value(&V \<: value)) with argument types (set[int],fun int(int))",|project://rascal-test/src/tests/Sets.rsc|(2149,21,<67,11>,<67,32>))
						//error("Name classes is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2182,7,<69,8>,<69,15>))
						//error("Name classes is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2200,7,<69,26>,<69,33>))
						// Posted issue

"Strings"  				// 2 test fail
];

loc base = |rascal-test:///tests/library|;
int nsuccess = 0;
int nfail = 0;

void runTests(list[str] names, loc base){
 for(tst <- names){
      println("***** <tst> ***** <base>");
      if(<s, f> := execute(base + (tst + ".rsc"), [], recompile=true, testsuite=true, listing=false, debug=false)){
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
  //runTests(rascalTests, |project://rascal-test/src/tests|);
  println("Overall summary: <nsuccess + nfail> tests executed, <nsuccess> succeeded, <nfail> failed");
  return nfail == 0;
}