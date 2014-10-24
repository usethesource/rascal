module experiments::Compiler::muRascal2RVM::ObsoleteReifiedTypes

import lang::rascal::\syntax::Rascal;
import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes; 

import experiments::Compiler::Rascal2muRascal::TypeReifier;

import ParseTree;
import IO;

public bool reifiedtype(Symbol \type, type[value] st) {
	moduleLoc = |rascal:///ParseTree.rsc|;
	Module M = parse(#start[Module], moduleLoc);
   	Configuration c = newConfiguration();
   	Configuration config = checkModule(M.top, c);
   	return symbolToValue(\type /*,config*/) == st;
}