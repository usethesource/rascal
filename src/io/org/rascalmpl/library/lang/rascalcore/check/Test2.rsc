module lang::rascalcore::check::Test2

import lang::rascal::grammar::definition::Modules;
import lang::rascal::grammar::definition::Productions;
import Grammar;
import ParseTree;
import Set;
import IO;

public Grammar expandRegularSymbols(Grammar G) {
  for (Symbol def <- G.rules) {
    if (choice(def, {regular(def)}) := G.rules[def]) { 
      ;
    }
  }
  return G;
}