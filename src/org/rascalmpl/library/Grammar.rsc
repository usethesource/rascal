@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl - CWI}
@contributor{Vadim Zaytsev - Vadim.Zaytsev@cwi.nl - CWI}
@doc{
  This modules defines an simple but effective internal format for the representation of context-free grammars.
}
module Grammar

import ParseTree;
import Set;
import IO;

@doc{
  Grammar is the internal representation (AST) of syntax definitions used in Rascal.
  A grammar is a set of productions and set of start symbols. The productions are 
  stored in a map for efficient access.
}
data Grammar 
  = \grammar(set[Symbol] starts, map[Symbol sort, Production def] rules)
  ;

data GrammarModule
  = \module(str name, set[str] imports, set[str] extends, Grammar grammar);
 
data GrammarDefinition
  = \definition(str main, map[str name, GrammarModule \mod] modules);

anno loc Production@\loc;
 
public Grammar grammar(set[Symbol] starts, set[Production] prods) {
  rules = ();
  for (p <- prods) {
    t = (p.def is label) ? p.def.symbol : p.def;
    rules[t] = t in rules ? choice(t, {p, rules[t]}) : choice(t, {p});
  } 
  return grammar(starts, rules);
} 
           
@doc{
Here we extend productions with basic combinators allowing to
construct ordered and un-ordered compositions, and associativity groups.

The intended semantics are that 
 	'choice' means unordered choice,
 	'priority'  means ordered choice, where alternatives are tried from left to right,
    'assoc'  means all alternatives are acceptible, but nested on the declared side
    'others' means '...', which is substituted for a choice among the other definitions
    'reference' means a reference to another production rule which should be substituted there,
                for extending priority chains and such.
} 
data Production 
  = \choice(Symbol def, set[Production] alternatives)
  | \priority(Symbol def, list[Production] choices)
  | \associativity(Symbol def, Associativity \assoc, set[Production] alternatives)
  | \others(Symbol def)
  | \reference(Symbol def, str cons)
  ;

@doc{
  These combinators are defined on Symbol, but it is checked (elsewhere) that only char-classes are passed in.
}
data Symbol 
  = intersection(Symbol lhs, Symbol rhs)
  | union(Symbol lhs, Symbol rhs)
  | difference(Symbol lhs, Symbol rhs)
  | complement(Symbol cc)
  ;
  
@doc{
  An item is an index into the symbol list of a production rule
}  
data Item = item(Production production, int index);

// The following normalization rules canonicalize grammars to prevent arbitrary case distinctions later

@doc{Nested choice is flattened}
public Production choice(Symbol s, {set[Production] a, choice(Symbol t, set[Production] b)})
  = choice(s, a+b);
  
@doc{Nested priority is flattened}
public Production priority(Symbol s, [list[Production] a, priority(Symbol t, list[Production] b),list[Production] c])
  = priority(s,a+b+c);
   
@doc{Choice under associativity is flattened}
public Production associativity(Symbol s, Associativity as, {set[Production] a, choice(Symbol t, set[Production] b)}) 
  = associativity(s, as, a+b); 
  
@doc{Nested (equal) associativity is flattened}             
public Production associativity(Symbol rhs, Associativity a, {associativity(Symbol rhs2, Associativity b, set[Production] alts), set[Production] rest}) {
  if (a == b)  
    return associativity(rhs, a, rest + alts) ;
  else
    fail;
}

public Production associativity(Symbol rhs, Associativity a, {prod(Symbol rhs, list[Symbol] lhs, set[Attr] as), set[Production] rest}) {
  if (!(\assoc(_) <- as)) 
	  return \associativity(rhs, a, rest + {prod(rhs, lhs, as + {\assoc(a)})});
  else fail;
}

@doc{Priority under an associativity group defaults to choice}
public Production associativity(Symbol s, Associativity as, {set[Production] a, priority(Symbol t, list[Production] b)}) 
  = associativity(s, as, a + { e | e <- b}); 

@doc{
  Compose two grammars, by adding the rules of g2 to the rules of g1.
  The start symbols of g1 will be the start symbols of the resulting grammar.
}
public Grammar compose(Grammar g1, Grammar g2) {
  set[Production] empty = {};
  for (s <- g2.rules)
    if (g1.rules[s]?)
      g1.rules[s] = choice(s, {g1.rules[s], g2.rules[s]});
    else
      g1.rules[s] = g2.rules[s];
  return g1;
}    

public rel[str, str] extends(GrammarDefinition def) {
  return {<m,e> | m <- def.modules, \module(_, _, exts , _) := def.modules[m], e <- exts}+;
}

public rel[str,str] imports(GrammarDefinition def) {
  return {<m,i> | m <- def.modules, \module(_, imps, _ , _) := def.modules[m], i <- imps};
}

