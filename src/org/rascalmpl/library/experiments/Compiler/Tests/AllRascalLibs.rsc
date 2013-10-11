module experiments::Compiler::Tests::AllRascalLibs

import Prelude;
import experiments::Compiler::Compile;

/*
 * Results of compiling Rascal library modules.
 */

list[str] libs = [

/***** OK *****/

//"Boolean", 			// OK
//"DateTime"			// OK
//"Exception" 			// OK
//"IO"					// OK
//"List" 				// OK
//"ListRelation"		// OK
//"Map" 				// OK
//"Message", 			// OK
//"Node"				// OK
//"Origins",			// OK
//"Relation",			// OK
//"Set",				// OK
//"String",				// OK
//"Time", 				// OK
//"Type", 				// OK
//"ToString", 			// OK
//"Traversal",			// OK
//"Tuple", 				// OK
//"ValueIO", 			// OK
//"util::Eval"			// OK
//"util::FileSystem" 	// OK
//"util::Math" 			// OK
//"util::Maybe"			// OK
//"util::Monitor",		// OK
//"util::ShellExec",	// OK
//"util::Webserver"		// OK

/***** Not yet OK *****/

//"Ambiguity"			// error("Expected type list, not fun list[&T \<: value](list[&T \<: value])",|project://rascal/src/org/rascalmpl/library/Ambiguity.rsc|(9985,7,<248,42>,<248,49>))
						// error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/Ambiguity.rsc|(9496,18,<234,31>,<234,49>))
						// error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/Ambiguity.rsc|(10313,18,<257,38>,<257,56>))
						// error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/Ambiguity.rsc|(10281,25,<257,6>,<257,31>))
						// error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/Ambiguity.rsc|(10689,18,<263,38>,<263,56>))
						// error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/Ambiguity.rsc|(10657,25,<263,6>,<263,31>))
						// error("list[Symbol] and fun list[&T \<: value](list[&T \<: value]) incomparable",|project://rascal/src/org/rascalmpl/library/Ambiguity.rsc|(10029,13,<248,86>,<248,99>))
						// error("Name l is not in scope",|project://rascal/src/org/rascalmpl/library/Ambiguity.rsc|(9860,1,<244,73>,<244,74>))
						// error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/Ambiguity.rsc|(9471,18,<234,6>,<234,24>))
						// error("list[Symbol] and fun list[&T \<: value](list[&T \<: value]) incomparable",|project://rascal/src/org/rascalmpl/library/Ambiguity.rsc|(9770,13,<243,86>,<243,99>))
						// error("Expected type list, not fun list[&T \<: value](list[&T \<: value])",|project://rascal/src/org/rascalmpl/library/Ambiguity.rsc|(9726,7,<243,42>,<243,49>))
						// error("Name l is not in scope",|project://rascal/src/org/rascalmpl/library/Ambiguity.rsc|(10119,1,<249,73>,<249,74>))
						// error("Cannot re-declare name that is already declared in the current function or closure",|project://rascal/src/org/rascalmpl/library/Ambiguity.rsc|(8058,1,<195,20>,<195,21>))
                    	
//"APIGen" 				// reifiedTypeNodes |rascal://lang::rascal::types::CheckTypes|(178871,21,<3518,22>,<3518,43>): "Not yet implemented" 
                        //
"Grammar" 			// error("Cannot assign type set[value] into field of type set[Production]",|project://rascal/src/org/rascalmpl/library/Grammar.rsc|(2176,25,<70,56>,<70,81>))

//"Number"				// DEPRECATED: TC gives errors

//"ParseTree", 			// error("Type of pattern could not be computed, please add additional type annotations",|rascal:///ParseTree.rsc|(19030,14,<511,22>,<511,36>))

//"Prelude",			// Depends on all others 
						 
//"util::Benchmark"		// error("Function of type fun map[str, num](map[str, fun void()], fun int(fun void())) cannot be called with argument types (map[str, fun void()],overloaded:\n\t\tfun int(fun void())\n\t\tfun int())",|rascal:///util/Benchmark.rsc|(5603,26,<179,8>,<179,34>))
						// error("Function of type fun map[str, num](map[str, fun void()]) cannot be called with argument types (map[str, fun void()],overloaded:\n\t\tfun int(fun void())\n\t\tfun int())",|rascal:///util/Benchmark.rsc|(5603,26,<179,8>,<179,34>))
						// Overloaded function issue

//"util::Highlight"		// import String, ParseTree

//"util::LOC"				// error("Name n is not in scope",|std:///util/LOC.rsc|(362,1,<20,19>,<20,20>))
						// error("Name n is not in scope",|std:///util/LOC.rsc|(382,1,<20,39>,<20,40>))
						// error("Name stats2 is not in scope",|std:///util/LOC.rsc|(532,6,<26,19>,<26,25>))
						// error("Field top does not exist on type Tree",|std:///util/LOC.rsc|(943,5,<44,8>,<44,13>))
						// error("Name writeKids is not in scope",|std:///util/LOC.rsc|(1769,9,<87,8>,<87,17>))
						// error("Name stats2 is not in scope",|std:///util/LOC.rsc|(577,6,<26,64>,<26,70>))
						// error("Name stats2 is not in scope",|std:///util/LOC.rsc|(559,6,<26,46>,<26,52>))
						// error("Name writeKids is not in scope",|std:///util/LOC.rsc|(1293,9,<64,8>,<64,17>))

//"util::PriorityQueue"	//error("Cannot assign to an existing constructor or function name",|rascal:///util/PriorityQueue.rsc|(6101,3,<209,14>,<209,17>))
						// error("Invalid return type list[value], expected return type list[BinomialTree]",|rascal:///util/PriorityQueue.rsc|(4124,25,<133,4>,<133,29>))
						// error("Invalid return type list[value], expected return type list[BinomialTree]",|rascal:///util/PriorityQueue.rsc|(4228,17,<137,4>,<137,21>))
						// error("Field root does not exist on type BinomialTree",|rascal:///util/PriorityQueue.rsc|(2065,6,<58,26>,<58,32>))
						// error("Function of type fun void(value) cannot be called with argument types (str,str)",|rascal:///util/PriorityQueue.rsc|(6188,26,<213,3>,<213,29>))
						// error("Function of type fun void() cannot be called with argument types (str,str)",|rascal:///util/PriorityQueue.rsc|(6188,26,<213,3>,<213,29>))
						// error("Constructor of type PriorityQueue priorityQueue : (list[BinomialTree] trees, int minIndex) cannot be built with argument types (int,int)",|rascal:///util/PriorityQueue.rsc|(2879,28,<86,23>,<86,51>))
						// error("Unable to bind subject type list[value] to assignable",|rascal:///util/PriorityQueue.rsc|(6151,24,<210,13>,<210,37>))
//"util/Reflective" 	// 	checks + compiles but depends on ParseTree.
						
];

value main(list[value] args){
  for(lib <- libs){
    println("**** Compiling <lib> ****");
    //compile("module TST import <lib>;");
    compile(|project://rascal/src/org/rascalmpl/library/| + (replaceAll(lib, "::", "/") + ".rsc"));
  }
  return true;
}