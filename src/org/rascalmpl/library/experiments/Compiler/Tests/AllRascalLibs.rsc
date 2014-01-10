module experiments::Compiler::Tests::AllRascalLibs

import Prelude;
import experiments::Compiler::Compile;

/*
 * Results of compiling Rascal library modules.
 */

list[str] libs = [

/***** OK *****/

"Boolean", 			// OK
"DateTime",			// OK
"Exception", 		// OK
"Grammar", 			// OK
"IO",				// OK
"List", 			// OK
"ListRelation",		// OK
"Map", 				// OK
"Message", 			// OK
"Node",				// OK
"Origins",			// OK
"ParseTree", 		// OK		
"Prelude",			// OK	
"Relation",			// OK
"Set",				// OK
"String",			// OK
"Time", 			// OK
"Type", 			// OK
"ToString", 		// OK
"Traversal",		// OK
"Tuple", 			// OK
"ValueIO", 			// OK
"util::Benchmark",	// OK
"util::Eval",		// OK
"util::FileSystem", // OK
"util::Highlight",	// OK
"util::Math",		// OK
"util::Maybe",		// OK
"util::Monitor",	// OK
"util::PriorityQueue",// OK
"util::Reflective", 	// OK
"util::ShellExec",	// OK
"util::Webserver"		// OK

/***** Not yet OK *****/

//"Ambiguity"			// #483
						//|rascal://lang::rascal::types::CheckTypes|(31671,1,<634,13>,<634,14>): Expected map[RName, int], but got map[RName, value]     	


//"APIGen" 			 	// #482
						//error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3641,16,<89,62>,<89,78>))
						//error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3281,33,<80,24>,<80,57>))
						//error("Name cs is not in scope",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7837,2,<207,39>,<207,41>))
						//error("Expected type bool, found fail",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7726,25,<206,46>,<206,71>))
						//error("Multiple constructors and/or productions match this pattern, add additional type annotations",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(2134,15,<61,11>,<61,26>))
						//error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(2134,15,<61,11>,<61,26>))
						//error("Field definitions does not exist on type type",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7738,13,<206,58>,<206,71>))
						//error("Name t2 is not in scope",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3093,2,<73,87>,<73,89>))
						//error("Function of type fun str(str) cannot be called with argument types (inferred(11))",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(2159,18,<61,36>,<61,54>))
						//error("Multiple constructors and/or productions match this pattern, add additional type annotations",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(6028,12,<166,11>,<166,23>))
						//error("Function of type fun str(Symbol) cannot be called with argument types (inferred(41))",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3603,21,<89,24>,<89,45>))
						//error("Function of type fun str(Symbol) cannot be called with argument types (value)",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3468,22,<85,26>,<85,48>))
						//error("Field definitions does not exist on type type",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7769,13,<206,89>,<206,102>))
						//error("Expected type str, found fail",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7795,48,<206,115>,<207,45>))
						//error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(6028,12,<166,11>,<166,23>))
						//error("Expected type bool, found fail",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7753,39,<206,73>,<206,112>))
						//error("Invalid return type str, expected return type void",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3323,61,<81,5>,<81,66>))
						//error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3259,56,<80,2>,<80,58>))

// "Number"				// DEPRECATED: TC gives errors


//"util::LOC"			// #394
						// error("Field top does not exist on type Tree",|std:///util/LOC.rsc|(943,5,<44,8>,<44,13>))
						
];

value main(list[value] args){
  for(lib <- libs){
    println("**** Compiling <lib> ****");
    compile(|project://rascal/src/org/rascalmpl/library/| + (replaceAll(lib, "::", "/") + ".rsc"));
  }
  return true;
}