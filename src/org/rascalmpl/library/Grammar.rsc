@license{
  Copyright (c) 2009-2015 CWI
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

import Exception;
import Message;
extend ParseTree;
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

public Grammar grammar(set[Symbol] starts, set[Production] prods) {
  rules = ();

  for (p <- prods) {
    t = (p.def is label) ? p.def.symbol : p.def;
    rules[t] = t in rules ? choice(t, {p, *rules[t]}) : choice(t, {p});
  } 
  return grammar(starts, rules);
} 
           
Grammar grammar(type[&T <: Tree] sym)
	= grammar({sym.symbol}, sym.definitions);

  
@doc{
  An item is an index into the symbol list of a production rule
}  
data Item = item(Production production, int index);

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
  g1.starts += g2.starts;
  return innermost visit(g1) {
    case c:choice(_, {p, *r, Production x:priority(_,/p)}) => c[alternatives = {x, *r}]
    case c:choice(_, {p, *r, Production x:associativity(_,_,/p)}) => c[alternatives = {x, *r}]
  };
}    

public rel[str, str] extends(GrammarDefinition def) {
  return {<m,e> | m <- def.modules, \module(_, _, exts , _) := def.modules[m], e <- exts}+;
}

public rel[str,str] imports(GrammarDefinition def) {
  return {<m,i> | m <- def.modules, \module(_, imps, _ , _) := def.modules[m], i <- imps};
}


