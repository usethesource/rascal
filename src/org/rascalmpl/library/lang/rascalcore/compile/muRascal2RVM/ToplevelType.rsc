module lang::rascalcore::compile::muRascal2RVM::ToplevelType

import List;

/* The following list should have the same order as the one in:
 * org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ToplevelType
 */

public list[str] toplevelTypes =
	[ "void", 
	  "bool", 
	  "int", 
	  "real", 
	  "rat", 
	  "num", 
	  "str", 
	  "loc", 
	  "datetime", 
	  "list", 
	  "node", 
	  "constructor", 
	  "lrel", 
	  "map", 
	  "set", 
	  "rel", 
	  "tuple", 
	  "value"
	];
	
int getToplevelType(str name){
  return indexOf(toplevelTypes, name);
}