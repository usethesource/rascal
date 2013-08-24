module experiments::Compiler::muRascal2RVM::ReifiedTypes

import lang::rascal::\syntax::Rascal;
import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;

import experiments::Compiler::Rascal2muRascal::TypeReifier;

import ParseTree;
import IO;

test bool reifiedtype(type[Symbol] st) {
	moduleLoc = |std:///ParseTree.rsc|;
	println("<moduleLoc>");
	Module M = parse(#start[Module], moduleLoc);
   	Configuration c = newConfiguration();
   	Configuration config = checkModule(M.top, c);
   	rt = symbolToValue(Symbol::\adt("Symbol",[]),config);
   	return rt.definitions == st.definitions;
}