module experiments::Compiler::Tests::AllRascalTests

import IO;
import experiments::Compiler::Execute;

loc base1 = |project:///rascal-test/tests/functionality|;

// Percentage of succeeded tests, see spreadsheet TestOverview.ods

list[str] functionalityTests = [

// OK
//"AliasTests"				// OK
//"AnnotationTests"			// OK
//"AssignmentTests"			// OK
//"BackTrackingTests"		// OK
//"ComprehensionTests"		// OK
							// 3 tests fail that correspond to empty enumerations: interpreter gives false, compiler gives true.
//"DataTypeTests"			// OK
//"ReducerTests"			// OK
//"DeclarationTests"		// OK, these are conscious changes in the scoping rules
							// error("Cannot re-declare name that is already declared in the current function or closure",|project://rascal-test/src/tests/functionality/DeclarationTests.rsc|(985,1,<31,18>,<31,19>))
							// error("Cannot re-declare name that is already declared in the current function or closure",|project://rascal-test/src/tests/functionality/DeclarationTests.rsc|(1071,1,<35,14>,<35,15>))
							// error("Cannot re-declare name that is already declared in the current function or closure",|project://rascal-test/src/tests/functionality/DeclarationTests.rsc|(1167,1,<39,24>,<39,25>))
//"RangeTests"				// OK, 4 tests fail but this is due to false 1. == 1.0 comparisons.
//"RegExpTests"				// OK
 							// Commented out 6: Treatment of redeclared local variables
//"TryCatchTests"			// OK

// Not yet OK

//"AccumulatingTests"		// [15] 2 tests fail: append that crosses function boundary: make tmp scope dependent?

//"CallTests"				// [58] keyword parameters

//"DataDeclarationTests"	// [45]
                            //error("Initializer type Maybe[&T \<: value] not assignable to variable of type Maybe[void]",|project://rascal-test/src/tests/functionality/DataDeclarationTests.rsc|(5906,10,<104,53>,<104,63>))
										//error("Initializer type Exp1[&T \<: value] not assignable to variable of type Exp1[int]",|project://rascal-test/src/tests/functionality/DataDeclarationTests.rsc|(5772,11,<100,58>,<100,69>))
							//error("Initializer type &T \<: value not assignable to variable of type str",|project://rascal-test/src/tests/functionality/DataDeclarationTests.rsc|(5535,12,<95,68>,<95,80>))
							//error("Initializer type &T \<: value not assignable to variable of type str",|project://rascal-test/src/tests/functionality/DataDeclarationTests.rsc|(5061,12,<89,68>,<89,80>))
							// Issue posted
//"FunctionCompositionTests"	//[6]
							// TC does not support function composition, issue #431
							
"PatternTests"			// [420]
							// Uses keyword parameters
							// Checking function matchADTwithKeywords4
							// |rascal://lang::rascal::types::CheckTypes|(140533,19,<2772,21>,<2772,40>): The called signature: checkExp(sort("Expression"), Configuration),
							// does not match the declared signature:	CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  


//"StatementTests"			// [64] Fail in overloaded constructor gives problem ==> Issue posted
						
//"SubscriptTests"			// [50] set-based subscripts of relations ==> Issue posted.

//"ProjectionTests" 		// [4]
							//	Issue #432
//"ScopeTests"				// [14]

//"VisitTests"				// 13 fail [98]
];


list[str] rascalTests = [

//"Booleans"				// OK
							// Commented out fromInt test

//"Integers"				// OK
//"Tuples"					// OK
//"SolvedIssues"			// OK

//"Nodes"					// OK
//"Strings"  				// OK
//"StringTests"				// OK

// Not yet OK

//"Equality"				// OK
							// Added parentheses for ? operator

//"BacktrackingTests"		// [12]
							// error("Name s is not in scope",|project://rascal-test/src/tests/BacktrackingTests.rsc|(8573,1,<223,10>,<223,11>))
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
						
//"Functions"			// [3]
						// Checking function callKwp
						// |rascal://lang::rascal::types::CheckTypes|(206380,13,<4071,16>,<4071,29>): The called signature: checkExp(sort("Expression"), Configuration),
						// does not match the declared signature:	CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  CheckResult checkExp(sort("Expression"), Configuration); (concrete pattern);  

//"IO"					// [6]
						// adding grammar for ValueIO
						// |rascal://experiments::Compiler::RVM::Run|(217,715,<12,0>,<23,28>): Java("RuntimeException","PANIC: (instruction execution): null")
						//	at org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM.executeProgram(|file:///RVM.java|(0,0,<1381,0>,<1381,0>))

//"ListRelations"		// [19]
						// error("Name b is not in scope",|project://rascal-test/src/tests/ListRelations.rsc|(1014,1,<29,57>,<29,58>))
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

//"Lists"				// [101]
						// error("Could not instantiate type variables in type fun list[&U \<: value](list[&T \<: value], fun &U \<: value(&T \<: value)) with argument types (list[int],fun int(int))",|project://rascal-test/src/tests/Lists.rsc|(11493,15,<340,9>,<340,24>))
						//error("Could not instantiate type variables in type fun set[&U \<: value](set[&T \<: value], fun &U \<: value(&T \<: value)) with argument types (list[int],fun int(int))",|project://rascal-test/src/tests/Lists.rsc|(11493,15,<340,9>,<340,24>))
						//error("set[int] and list[value] incomparable",|project://rascal-test/src/tests/Lists.rsc|(14053,17,<425,2>,<425,19>))
						//error("Unexpected type: type of body expression, int, must be a subtype of the function return type, bool",|project://rascal-test/src/tests/Lists.rsc|(1742,35,<50,30>,<50,65>))

//"Maps"				// [23] Compilation of ListRelation


//"Matching"			// [4]
						// Checking function fT2
						// getFUID: fT2, failure({error("Type of pattern could not be computed",|rascal:///experiments/Compiler/Examples/Tst.rsc|(529,10,<21,9>,<21,19>)),error("Could not calculate function type because of errors calculating the parameter types",|rascal:///experiments/Compiler/Examples/Tst.rsc|(520,20,<21,0>,<21,20>)),error("Multiple constructors match this pattern, add additional type annotations",|rascal:///experiments/Compiler/Examples/Tst.rsc|(529,10,<21,9>,<21,19>))})
						// |rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(14990,5,<374,27>,<374,32>): NoSuchField("parameters")
						// Issue #430
	
//"Relations"			// [19]
						//error("Name a is not in scope",|project://rascal-test/src/tests/Relations.rsc|(226,1,<11,39>,<11,40>))
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


//"Sets"				//[35]
						//error("Name c is not in scope",|project://rascal-test/src/tests/Sets.rsc|(2401,1,<75,55>,<75,56>))
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
];

list[str] libraryTests = [

// OK

//"BooleanTests"		// OK
//"IntegerTests"		// OK
"MathTests"				// OK
//"NumberTests"			// OK
//"StringTests"			// OK

// Not yet OK

//"GraphTests"			// [23]
						//error("Could not instantiate type variables in type fun set[&T \<: value](rel[&T \<: value from, &T \<: value to], set[&T \<: value], set[&T \<: value]) with argument types (set[void],set[void],set[void])",|project://rascal-test/src/tests/library/GraphTests.rsc|(1084,18,<27,33>,<27,51>))
						//error("Could not instantiate type variables in type fun set[&T \<: value](rel[&T \<: value from, &T \<: value to]) with argument types (set[void])",|project://rascal-test/src/tests/library/GraphTests.rsc|(766,10,<18,33>,<18,43>))
						//error("Could not instantiate type variables in type fun set[&T \<: value](rel[&T \<: value from, &T \<: value to]) with argument types (set[void])",|project://rascal-test/src/tests/library/GraphTests.rsc|(2748,7,<57,30>,<57,37>))
						//error("Could not instantiate type variables in type fun set[&T \<: value](rel[&T \<: value from, &T \<: value to], set[&T \<: value], set[&T \<: value]) with argument types (set[void],set[void],set[void])",|project://rascal-test/src/tests/library/GraphTests.rsc|(1548,18,<35,33>,<35,51>))

//"ListTests" 			// [132]
						//error("Could not instantiate type variables in type fun map[&A \<: value, &B \<: value](lrel[&A \<: value, &B \<: value]) with argument types (list[void])",|project://rascal-test/src/tests/library/ListTests.rsc|(8668,21,<223,38>,<223,59>))
						//error("Could not instantiate type variables in type fun map[&A \<: value, &B \<: value](lrel[&A \<: value, &B \<: value]) with argument types (list[void])",|project://rascal-test/src/tests/library/ListTests.rsc|(8735,15,<224,38>,<224,53>))
						//error("Could not instantiate type variables in type fun &T \<: value(list[&T \<: value], fun &T \<: value(&T \<: value, &T \<: value), &T \<: value) with argument types (list[int],fun int(int, int),int)",|project://rascal-test/src/tests/library/ListTests.rsc|(4472,29,<123,16>,<123,45>))
						//error("Could not instantiate type variables in type fun list[&U \<: value](list[&T \<: value], fun &U \<: value(&T \<: value)) with argument types (list[int],fun int(int))",|project://rascal-test/src/tests/library/ListTests.rsc|(3384,22,<93,72>,<93,94>))
						//error("Could not instantiate type variables in type fun map[&A \<: value, set[&B \<: value]](lrel[&A \<: value, &B \<: value]) with argument types (list[void])",|project://rascal-test/src/tests/library/ListTests.rsc|(9023,15,<233,32>,<233,47>))
						//error("Could not instantiate type variables in type fun map[&A \<: value, set[&B \<: value]](lrel[&A \<: value, &B \<: value]) with argument types (list[void])",|project://rascal-test/src/tests/library/ListTests.rsc|(9078,9,<234,32>,<234,41>))

//"MapTests"			// [36]
						//error("Could not instantiate type variables in type fun map[&K \<: value, &V \<: value](map[&K \<: value, &V \<: value], fun &L \<: value(&K \<: value), fun &W \<: value(&V \<: value)) with argument types (map[int, int],fun int(int),fun int(int))",|project://rascal-test/src/tests/library/MapTests.rsc|(2612,29,<67,32>,<67,61>))
						//error("Could not instantiate type variables in type fun map[&K \<: value, &V \<: value](map[&K \<: value, &V \<: value], fun &L \<: value(&K \<: value), fun &W \<: value(&V \<: value)) with argument types (map[void, void],fun int(int),fun int(int))",|project://rascal-test/src/tests/library/MapTests.rsc|(2552,20,<66,32>,<66,52>))
						//error("Could not instantiate type variables in type fun map[&K \<: value, &V \<: value](map[&K \<: value, &V \<: value], fun &L \<: value(&K \<: value), fun &W \<: value(&V \<: value)) with argument types (map[int, int],fun int(int),fun int(int))",|project://rascal-test/src/tests/library/MapTests.rsc|(2474,29,<65,32>,<65,61>))
						//error("Could not instantiate type variables in type fun map[&K \<: value, &V \<: value](map[&K \<: value, &V \<: value], fun &L \<: value(&K \<: value), fun &W \<: value(&V \<: value)) with argument types (map[void, void],fun int(int),fun int(int))",|project://rascal-test/src/tests/library/MapTests.rsc|(2414,20,<64,32>,<64,52>))
						//error("Unexpected type: type of body expression, map[int, int], must be a subtype of the function return type, bool",|project://rascal-test/src/tests/library/MapTests.rsc|(1640,26,<40,36>,<40,62>))

//"RelationTests"		// [50]

//"SetTests"			// [54]
						// error("Could not instantiate type variables in type fun map[&A \<: value, set[&B \<: value]](rel[&A \<: value, &B \<: value]) with argument types (set[void])",|project://rascal-test/src/tests/library/SetTests.rsc|(3902,14,<91,32>,<91,46>))
						//error("Could not instantiate type variables in type fun map[&A \<: value, set[&B \<: value]](rel[&A \<: value, &B \<: value]) with argument types (set[void])",|project://rascal-test/src/tests/library/SetTests.rsc|(3956,9,<92,32>,<92,41>))
						//error("Could not instantiate type variables in type fun set[&U \<: value](set[&T \<: value], fun &U \<: value(&T \<: value)) with argument types (set[int],fun int(int))",|project://rascal-test/src/tests/library/SetTests.rsc|(1800,22,<38,70>,<38,92>))
						//error("Could not instantiate type variables in type fun &T \<: value(set[&T \<: value], fun &T \<: value(&T \<: value, &T \<: value), &T \<: value) with argument types (set[int],fun int(int, int),int)",|project://rascal-test/src/tests/library/SetTests.rsc|(2720,29,<60,12>,<60,41>))
						//error("Could not instantiate type variables in type fun map[&A \<: value, &B \<: value](rel[&A \<: value, &B \<: value]) with argument types (set[void])",|project://rascal-test/src/tests/library/SetTests.rsc|(4286,15,<98,38>,<98,53>))
						//error("Could not instantiate type variables in type fun map[&A \<: value, &B \<: value](rel[&A \<: value, &B \<: value]) with argument types (set[void])",|project://rascal-test/src/tests/library/SetTests.rsc|(4220,20,<97,38>,<97,58>))


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
  //runTests(libraryTests, |project://rascal-test/src/tests/library|);
  println("Overall summary: <nsuccess + nfail> tests executed, <nsuccess> succeeded, <nfail> failed");
  return nfail == 0;
}