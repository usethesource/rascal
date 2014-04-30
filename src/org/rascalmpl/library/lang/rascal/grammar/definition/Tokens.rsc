module lang::rascal::grammar::definition::Tokens

import lang::rascal::grammar::analyze::Dependency;
import ParseTree;
import Grammar;
import IO;
import lang::rascal::grammar::definition::Literals;

Grammar flattenTokens(Grammar g) {
  deps = symbolDependencies(g)+;
  
  if (s <- g.rules, s is \token, <s,s> in deps) {
    throw "found a recursively defined token <s>";
  }
  
  return inlineProductions(flattenChoices(g));
}

Grammar inlineProductions(Grammar g) = innermost visit(g) {
  case prod(\token(n), ss:/Symbol def,at)  => prod(\token(n), replaceDefinitions(ss, g), at)  when (def is \token || def is lit || def is cilit)
};

Production removeSeq(Production p) = visit(p) {
  case prod(s, [seq(x)], at)  => prod(s, x, at)
};

&T replaceDefinitions(&T v, Grammar g)  = innermost visit(v) {
  case \token(l) => seq(g.rules[\token(l)].symbols)
  case lit(s) => seq(g.rules[lit(s)].symbols)
  case cilit(s) => seq(g.rules[cilit(s)].symbols) 
};

Grammar flattenChoices(g) = innermost visit(g) {
  case choice(\token(n), alts)    => prod(\token(n), [alt({alt is prod ? seq(alt.symbols) : alt.def | alt <- alts})], {})
  case choice(\lit(n), alts)      => prod(\lit(n), [alt({alt is prod ? seq(alt.symbols) : alt.def | alt <- alts})], {})
  case choice(\cilit(n), alts)    => prod(\cilit(n), [alt({alt is prod ? seq(alt.symbols) : alt.def | alt <- alts})], {})
  case priority(def, choices)     : throw "priority not supported in lexical definition for <def>";
  case associativity(def, _,alts) : throw "associativity not supported in lexical definition for <def>";
};


Symbol seq([]) = empty();
Symbol seq([Symbol s]) = s;
Symbol alt({Symbol s}) = s;