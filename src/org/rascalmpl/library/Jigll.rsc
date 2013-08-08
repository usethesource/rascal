module Jigll

import Grammar;
import ParseTree;
import lang::rascal::grammar::definition::Literals;
import lang::rascal::grammar::definition::Priorities;
import lang::rascal::grammar::definition::Regular;
import IO;
import Node;

@javaClass{org.rascalmpl.parser.GrammarToJigll}
public java void generateGrammar(Grammar grammar);

@javaClass{org.rascalmpl.parser.GrammarToJigll}
public java void generateGraph();

public void generate(type[&T <: Tree] nont) {
  gr = grammar({nont.symbol}, nont.definitions, ());
  gr = makeRegularStubs(expandRegularSymbols(makeRegularStubs(gr)));
  gr = literals(gr);
  gr = addNotAllowedSets(gr);
  gr = prioAssocToChoice(gr);
 
  generateGrammar(gr);
}

public &T<:Tree jparse(type[&T <: Tree] nont, str input) {
  return jparse(nont.symbol, input);
}

public &T<:Tree jparse2(type[&T <: Tree] nont, str f) {
  return jparse(nont.symbol, readFile(|file:///| + f));
}

@javaClass{org.rascalmpl.parser.GrammarToJigll}
public java &T<:Tree jparse(Symbol nonterminal, str input);

@javaClass{org.rascalmpl.parser.GrammarToJigll}
public java void save(str inpu);