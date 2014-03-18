module lang::rascal::grammar::analyze::Lexicals

import lang::rascal::grammar::analyze::Dependency;
import ParseTree;
import Grammar;
import IO;
import lang::rascal::grammar::definition::Literals;

map[Symbol, Production] getRegularLexicals(Grammar g) {
  deps = symbolDependencies(g)+;
  nonRecursiveLex = { s | s <- g.rules, s is lex, <s,s> notin deps };
  
  fg = flattenChoices(g);
  return (s : removeSeq(inlineProductions(fg.rules[s], fg)) | s <- nonRecursiveLex);
}

Production inlineProductions(Production p, Grammar g) = innermost visit(p) {
  case prod(s, ss:/Symbol def,at)  => prod(s, replaceDefinitions(ss, g), at)  when (def is lex || def is \parametrized-lex || def is lit || def is cilit)
};

Production removeSeq(Production p) = visit(p) {
  case prod(s, [seq(x)], at)  => prod(s, x, at)
};

&T replaceDefinitions(&T v, Grammar g)  = innermost visit(v) {
  case lex(l) => seq(g.rules[lex(l)].symbols)
  case lit(s) => seq(g.rules[lit(s)].symbols)
  case cilit(s) => seq(g.rules[cilit(s)].symbols) 
  case \parametrized-lex(l,ps) => seq(g.rules[\parametrized-lex(l,ps)])
};

Grammar flattenChoices(g) = innermost visit(g) {
  case choice(s, alts)            => prod(s, [alt({alt is prod ? seq(alt.symbols) : alt.def | alt <- alts})], {})
  case priority(def, choices)     => choice(def, {*choices})
  case associativity(def, _,alts) => choice(def, alts) 
};



Symbol seq([]) = empty();
Symbol seq([Symbol s]) = s;
Symbol alt({Symbol s}) = s;