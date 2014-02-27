module util::Parser

import Grammar;

&T <: Tree parse(type[&T <: Tree] nonTerminal, str input, loc inputSrc) 
  = parse(nonTerminal, grammar({nonTerminal}, 

&T <: Tree parse(type[&T <: Tree] nonTerminal, loc src);

&T <: Tree parse(type[&T <: Tree] nonTerminal, Grammar grammar, loc src);

Grammar loadGrammar(loc parser);

