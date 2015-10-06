module experiments::Compiler::Rascal2muRascal::ParseModule

import ParseTree;
import lang::rascal::\syntax::Rascal;

lang::rascal::\syntax::Rascal::Module parseModuleGetTop(loc moduleLoc) = parse(#start[Module], moduleLoc).top;