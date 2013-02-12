module Jigll

import Grammar;
import ParseTree;

@javaClass{org.rascalmpl.parser.GrammarToJigll}
public java void generate(str name, Grammar grammar);

public void generate(str name, type[&T <: Tree] nont) {
  generate(name, grammar({nont.symbol}, nont.definitions));
}

public void jparse(type[&T <: Tree] nont, str input) {
  jparse(nont.symbol, grammar({nont.symbol}, nont.definitions), input);
}

@javaClass{org.rascalmpl.parser.GrammarToJigll}
public java &T jparse(Symbol nonterminal, Grammar grammar, str input);