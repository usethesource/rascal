module Jigll

import Grammar;
import ParseTree;
import lang::rascal::grammar::definition::Literals;
import lang::rascal::grammar::definition::Priorities;
import lang::rascal::grammar::definition::Regular;
import IO;

@javaClass{org.rascalmpl.parser.GrammarToJigll}
public java void generate(str name, Grammar grammar);

public void generate(str name, type[&T <: Tree] nont) {
  generate(name, grammar({nont.symbol}, nont.definitions), ());
}

data Grammar = nogrammar();
data Tree = notree();

private value cache = notree();
private Grammar gr = nogrammar();

public &T<:Tree jparse(type[&T <: Tree] nont, str input) {
  if(nont != cache) {
      gr = grammar({nont.symbol}, nont.definitions, ());
	  gr = expandRegularSymbols(makeRegularStubs(gr));
	  gr = literals(gr);
	  gr = removeLables(gr);
	  gr = addNotAllowedSets(gr);
	  gr = prioAssocToChoice(gr);
      cache = nont;     
  }
 
  return jparse(nont, nont.symbol, gr, input);
}

@javaClass{org.rascalmpl.parser.GrammarToJigll}
public java &T<:Tree jparse(type[&T <: Tree] nont, Symbol nonterminal, Grammar grammar, str input);

// in the future this has to go because the labels are worth some money
private Grammar removeLables(Grammar g) 
	   = visit (g) {
	      case label(name, s) => s
	   };
		
		
		
