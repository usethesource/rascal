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

//"Ambiguity",			// |rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(13601,5,<349,56>,<349,61>): NoSuchField("parameters")
                       	//   Caused by import of ParseTree 
                    	
//"APIGen", 			// reifiedTypeNodes |rascal://lang::rascal::types::CheckTypes|(178871,21,<3518,22>,<3518,43>): "Not yet implemented" 
                        //
//"Grammar", 			// |rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(13601,5,<349,56>,<349,61>): NoSuchField("parameters")
                    	//  Caused by associativity in ParseTree

//"Number"				// DEPRECATED: TC gives errors

//"ParseTree", 			// error("Type of pattern could not be computed, please add additional type annotations",|rascal:///ParseTree.rsc|(19030,14,<511,22>,<511,36>))

//"Prelude",			// Depends on all others 
						 
//"util::Benchmark",	// error("Function of type fun map[str, num](map[str, fun void()], fun int(fun void())) cannot be called with argument types (map[str, fun void()],overloaded:\n\t\tfun int(fun void())\n\t\tfun int())",|rascal:///util/Benchmark.rsc|(5603,26,<179,8>,<179,34>))
						// error("Function of type fun map[str, num](map[str, fun void()]) cannot be called with argument types (map[str, fun void()],overloaded:\n\t\tfun int(fun void())\n\t\tfun int())",|rascal:///util/Benchmark.rsc|(5603,26,<179,8>,<179,34>))


//"util::Highlight"		// import String, ParseTree

//"util::LOC"			// getFUID: slocStats, failure({error("Could not calculate function type because of errors calculating the parameter types",|rascal:///util/LOC.rsc|(570,50,<25,0>,<25,50>)),error("Type of pattern could not be computed, please add additional type annotations",|rascal:///util/LOC.rsc|(594,12,<25,24>,<25,36>))})
						// |rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(13668,5,<351,27>,<351,32>): NoSuchField("parameters")

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