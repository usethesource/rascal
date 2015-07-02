module lang::rascal::grammar::definition::Tokens

import lang::rascal::grammar::analyze::Dependency;
import ParseTree;
import Grammar;
import IO;
import lang::rascal::grammar::definition::Literals;
//import lang::rascal::grammar::definition::Characters;

Grammar flattenTokens(Grammar g) {
  g = literals(g);
  deps = symbolDependencies(g)+;
  
  if (s <- g.rules, s is \token, <s,s> in deps) {
    throw "found a recursively defined token <s>";
  }
  
  return simplify(inlineProductions(flattenChoices(g)));
}

Grammar inlineProductions(Grammar g) = innermost visit(g) {
  case prod(\token(n), ss:/Symbol def,at)  => prod(\token(n), replaceDefinitions(ss, g), at)  when (def is \token || def is lit || def is cilit)
};

Grammar simplify(Grammar g) = visit(g) {
  case p:prod(\token(_),_,_) => simplify(p) 
};
 
Production simplify(Production p) = innermost visit(p) {
  case prod(s, [seq(x)], at)         => prod(s, x, at)
  case seq([])                       => empty()
  case seq([*pre,seq(middle),*post]) => seq([*pre,*middle,*post])
  case seq([*pre,empty(),*post])     => seq([*pre,*post])
  case seq([Symbol s])               => s
  case alt({Symbol s})               => s
  case conditional(Symbol s, {})     => s
  case conditional(Symbol s, {*other, c}) => conditional(s,other) when c is \follow || c is \not-follow || c is \precede || c is \not-precede
};

&T replaceDefinitions(&T v, Grammar g)  = innermost visit(v) {
  case \token(l) => seq(g.rules[\token(l)].symbols)
  case lit(s) => seq(g.rules[lit(s)].symbols)
  case cilit(s) => seq(g.rules[cilit(s)].symbols) 
};

Grammar flattenChoices(g) = innermost visit(g) {
  case choice(\token(n), alts)    => prod(\token(n), [alt({alt is prod ? seq(alt.symbols) : alt.def | alt <- alts})], {})
  case choice(\lit(n), {a})       => a // there is only one
  case choice(\cilit(n), {a})     => a // there is only one
  case priority(\token(n), choices)     : throw "priority not supported in token definition of <n>";
  case associativity(\token(n), _,alts) : throw "associativity not supported in token definition of <n>";
};