module experiments::Compiler::Examples::Tst1

import experiments::Compiler::muRascal::Syntax;
import ParseTree;
import Ambiguity;
import IO;

Tree parseMuRascal(loc s) {
  pt = parse( #start[Module], s);
  return pt;					   
}

value main(list[value] args) = parseMuRascal(|project://rascal/src/org/rascalmpl/library/experiments/Compiler/muRascal2RVM/LibraryGamma.mu|);


