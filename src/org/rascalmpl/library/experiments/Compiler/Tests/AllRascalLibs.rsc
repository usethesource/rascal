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
"util/Reflective", 	// OK
"util::ShellExec",	// OK
"util::Webserver"		// OK

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
						// Issue #318

// "Number"				// DEPRECATED: TC gives errors


//"util::LOC"			// error("Field top does not exist on type Tree",|std:///util/LOC.rsc|(943,5,<44,8>,<44,13>))
					// Issue #394
						
];

value main(list[value] args){
  for(lib <- libs){
    println("**** Compiling <lib> ****");
    compile(|project://rascal/src/org/rascalmpl/library/| + (replaceAll(lib, "::", "/") + ".rsc"));
  }
  return true;
}