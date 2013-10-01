module experiments::Compiler::Tests::AllRascalLibs

import Prelude;
import experiments::Compiler::Compile;

/*
 * Results of compiling Rascal library modules.
 * Priorities:
 *	1. List
 *  2. util::Math
 *  3. Relation
 *  4. ParseTree
 */

list[str] libs = [

//"Ambiguity",			// |rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(13601,5,<349,56>,<349,61>): NoSuchField("parameters")
                       	//   Caused by import of ParseTree 
                    	

//"APIGen", 			// reifiedTypeNodes |rascal://lang::rascal::types::CheckTypes|(178871,21,<3518,22>,<3518,43>): "Not yet implemented" 
                        
//"Boolean", 			// OK
//"DateTime"			// Issue: caused by import of List 
                        //
//"Exception" 			// OK
//"Grammar", 			// |rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(13601,5,<349,56>,<349,61>): NoSuchField("parameters")
                    	//  Caused by associativity in ParseTree
						//
//"IO", 				// OK
"List" 					// |rascal://experiments::Compiler::Rascal2muRascal::RascalType|(2860,5,<55,29>,<55,34>): Undeclared variable: type
						//  Caused by:
						//	public map[&K, &V] mapper(map[&K, &V] M, &L (&K) F, &W (&V) G)
						//	{
  						//		return (F(key) : G(M[key]) | &K key <- M);
						//	}
						// 

//"ListRelation",		// 	error("Field ran does not exist",|rascal:///ListRelation.rsc|(9146,3,<380,23>,<380,26>))
						//	error("Field dom does not exist",|rascal:///ListRelation.rsc|(9151,3,<380,28>,<380,31>))
						//	Caused by:
						//	public set[list[&U]] groupDomainByRange(lrel[&U dom, &T ran] input) {
   						//		return ( i : (input<ran, dom>)[i] | i <- input.ran )<1>;
						//	}
						//	//error("Multiple functions found which could be applied",|rascal:///ListRelation.rsc|(5082,9,<209,10>,<209,19>))
						//	Caused by:
						//	public lrel[&T0, &T1] complement(lrel[&T0, &T1] R)
						//	{
  						//		return (domain(R) * range(R)) - R;
						//	}
						//	//error("Invalid return type set[set[&T \<: value ran]], expected return type set[list[&T \<: value]]",|rascal:///ListRelation.rsc|(9600,44,<394,3>,<394,47>))
						//	Caused by:
						//	public set[list[&T]] groupRangeByDomain(lrel[&U dom, &T ran] input) {
   						//		return ( i : input[i] | i <- input.dom )<1>;
						//	}
						  
					
//"Map", 				// |rascal://experiments::Compiler::Rascal2muRascal::RascalType|(2860,5,<55,29>,<55,34>): Undeclared variable: type
						// Issue: imports List. 
						 
//"Message", 			// OK
//"Node",				// |rascal://experiments::Compiler::Rascal2muRascal::RascalStatement|(1288,7,<27,71>,<27,78>): "visit"
//"Number", 			// TC gives errors
//"Origins",			// OK
//"ParseTree", 			//|rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(13601,5,<349,56>,<349,61>): NoSuchField("parameters")
						//	Caused by associativity function in ParseTree
						//	getFUID: associativity, failure({error("Constructor name is not in scope",|project://rascal/src/org/rascalmpl/library/ParseTree.rsc|(9839,8,<241,76>,<241,84>)),error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/ParseTree.rsc|(9823,55,<241,60>,<241,115>)),error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/ParseTree.rsc|(9770,109,<241,7>,<241,116>))})
						//	|rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(13652,5,<351,27>,<351,32>): NoSuchField("parameters")
						

//"Prelude",			// Depends on all others 
                         
//"Relation", 			// 	error("Field dom does not exist",|rascal:///Relation.rsc|(8776,3,<379,28>,<379,31>))
						//	error("Field ran does not exist",|rascal:///Relation.rsc|(8771,3,<379,23>,<379,26>))
               		   	//	in:
               			//		public set[set[&U]] groupDomainByRange(rel[&U dom, &T ran] input) {
   						//			return ( i : (input<ran, dom>)[i] | i <- input.ran )<1>;
						//		} 
						
//"Set",				// |rascal://experiments::Compiler::Rascal2muRascal::RascalType|(2860,5,<55,29>,<55,34>): Undeclared variable: type
						//	Caused by:
						//		public map[&K,set[&V]] classify(set[&V] input, &K (&V) getClass) = toMap({<getClass(e),e> | e <- input});
						 
//"String",				// |rascal://experiments::Compiler::Rascal2muRascal::RascalExpression|(2763,42,<72,55>,<72,97>): "RexExpLiteral cannot occur in expression"
//"Time", 				// OK
//"ToString", 			// OK
//"Traversal",			// OK
//"Tuple", 				// OK
//"Type", 				// |rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(4067,1,<134,20>,<134,21>): NoSuchKey(|rascal:///Type.rsc|(9039,7,<228,45>,<228,52>))
						 //  Issue: || used in comparable. 
						 
//"ValueIO", 			// OK

//"util::Benchmark",	// 	error("Function of type fun map[str, num](map[str, fun void()], fun int(fun void())) cannot be called with argument types (map[str, fun void()],overloaded:\n\t\tfun int(fun void())\n\t\tfun int())",|rascal:///util/Benchmark.rsc|(5585,26,<178,8>,<178,34>))
  						  // 	error("Function of type fun map[str, num](map[str, fun void()]) cannot be called with argument types (map[str, fun void()],overloaded:\n\t\tfun int(fun void())\n\t\tfun int())",|rascal:///util/Benchmark.rsc|(5585,26,<178,8>,<178,34>))
                         
//"util::Eval",
//"util::FileSystem",	// 	error("Function of type fun FileSystem(loc) cannot be called with argument types (str)",|rascal:///util/Filesystem.rsc|(465,8,<17,57>,<17,65>))
						// error("Function of type fun loc(str, list[loc]) cannot be called with argument types (str,fun bool(loc))",|rascal:///util/Filesystem.rsc|(569,13,<21,10>,<21,23>))
						//	error("Function of type fun set[loc](loc, fun bool(loc)) cannot be called with argument types (str,fun bool(loc))",|rascal:///util/Filesystem.rsc|(569,13,<21,10>,<21,23>))
						//	error("Function of type fun set[loc](loc, str) cannot be called with argument types (str,fun bool(loc))",|rascal:///util/Filesystem.rsc|(569,13,<21,10>,<21,23>))
						
//"util::Highlight",	// |rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(13601,5,<349,56>,<349,61>): NoSuchField("parameters")
//"util::LOC", 			// |rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(13601,5,<349,56>,<349,61>): NoSuchField("parameters")
//"util::Math", 		// |rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(4067,1,<134,20>,<134,21>): NoSuchKey(|std:///util/Math.rsc|(3114,1,<153,5>,<153,6>))
						//   Caused by: || operator in ceil 
						

//"util::Maybe",		// OK
//"util::Monitor",		// OK
//"util::PriorityQueue",// 	getFUID: add, failure({error("Type of pattern could not be computed, please add additional type annotations",|rascal:///util/PriorityQueue.rsc|(3822,23,<122,31>,<122,54>)),error("Could not calculate function type because of errors calculating the parameter types",|rascal:///util/PriorityQueue.rsc|(3799,63,<122,8>,<122,71>)),error("Type of pattern could not be computed, please add additional type annotations",|rascal:///util/PriorityQueue.rsc|(3847,14,<122,56>,<122,70>))})
						//	|rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(13652,5,<351,27>,<351,32>): NoSuchField("parameters")
						

//"util::Reflective", 	// 	getFUID: associativity, failure({error("Constructor name is not in scope",|project://rascal/src/org/rascalmpl/library/ParseTree.rsc|(9839,8,<241,76>,<241,84>)),error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/ParseTree.rsc|(9823,55,<241,60>,<241,115>)),error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/ParseTree.rsc|(9770,109,<241,7>,<241,116>))})
						//	|rascal://experiments::Compiler::Rascal2muRascal::TypeUtils|(13652,5,<351,27>,<351,32>): NoSuchField("parameters")
						
//"util::ShellExec",	// OK
//"util::Webserver",	// 	These errors seem justified, I have asked Davy to review this.
                        //   error("Constructor of type Response response : (Status status, str mimeType, map[str, str] header, str content) cannot be built with argument types (Status)",|rascal:///util/Webserver.rsc|(2032,22,<65,13>,<65,35>))
						//	error("Function of type fun Response(str) cannot be called with argument types (Status)",|rascal:///util/Webserver.rsc|(2032,22,<65,13>,<65,35>))
						//	error("Function of type fun Response(Status, str) cannot be called with argument types (Status)",|rascal:///util/Webserver.rsc|(2032,22,<65,13>,<65,35>))
						
];

value main(list[value] args){
  for(lib <- libs){
    println("**** Compiling <lib> ****");
    compile("module TST import <lib>;");
  }
  return true;
}