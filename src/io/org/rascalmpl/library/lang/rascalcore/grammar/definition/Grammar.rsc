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
.Synopsis
A simple but effective internal format for the representation of context-free grammars.
}
module lang::rascalcore::grammar::definition::Grammar

import Exception;
import Message;
//extend ParseTree;
extend lang::rascalcore::check::AType;
import Set;
import IO;

@doc{
.Synopsis
The Grammar datatype

.Description
Grammar is the internal representation (AST) of syntax definitions used in Rascal.
A grammar is a set of productions and set of start symbols. The productions are 
stored in a map for efficient access.
}
data AGrammar 
  = \grammar(set[AType] starts, map[AType sort, AProduction def] rules)
  ;

data AGrammarModule
  = \module(str name, set[str] imports, set[str] extends, AGrammar grammar);
 
data AGrammarDefinition
  = \definition(str main, map[str name, AGrammarModule \mod] modules);

anno loc AProduction@\loc;
 
public AGrammar grammar(set[AType] starts, set[AProduction] prods) {
  rules = ();

  for (p <- prods) {
    t = (p.def is label) ? p.def.symbol : p.def;
    rules[t] = t in rules ? choice(t, {p, *rules[t]}) : choice(t, {p});
  } 
  return grammar(starts, rules);
} 
           
AGrammar grammar(type[&T <: Tree] sym)
    = grammar({sym.symbol}, sym.definitions);

  
@doc{
.Synopsis
An item is an index into the symbol list of a production rule.
}  
data Item = item(AProduction aproduction, int index);

@doc{
.Synopsis
Compose two grammars.

.Description
Compose two grammars by adding the rules of g2 to the rules of g1.
The start symbols of g1 will be the start symbols of the resulting grammar.
}
public AGrammar compose(AGrammar g1, AGrammar g2) {
  set[AProduction] empty = {};
  for (s <- g2.rules)
    if (g1.rules[s]?)
      g1.rules[s] = choice(s, {g1.rules[s], g2.rules[s]});
    else
      g1.rules[s] = g2.rules[s];
  g1.starts += g2.starts;

  reduced_rules = ();
  for(s <- g1.rules){
      c = g1.rules[s];
      c.alternatives -= { *choices | priority(_, choices) <- c.alternatives } +
                        { *alts | associativity(_, _, alts) <- c.alternatives};
      reduced_rules[s] = c;
  }
  
  return grammar(g1.starts, reduced_rules);
}    

// TODO:COMPILER
// The above code (temporarily?) replaces the following code
// Reason: the algorithm is faster and compiled code chokes in the set matches
// for not yet known reason.

//public AGrammar compose(AGrammar g1, AGrammar g2) {
//  set[AProduction] empty = {};
//  for (s <- g2.rules)
//    if (g1.rules[s]?)
//      g1.rules[s] = choice(s, {g1.rules[s], g2.rules[s]});
//    else
//      g1.rules[s] = g2.rules[s];
//  g1.starts += g2.starts;
//
//  return innermost visit(g1) {
//    case c:choice(_, {p, *r, AProduction x:priority(_,/p)}) => c[alternatives = {x, *r}]
//    case c:choice(_, {p, *r, AProduction x:associativity(_,_,/p)}) => c[alternatives = {x, *r}]
//  };
//}    

public rel[str, str] extends(AGrammarDefinition def) {
  return {<m,e> | m <- def.modules, \module(_, _, exts , _) := def.modules[m], e <- exts}+;
}

public rel[str,str] imports(AGrammarDefinition def) {
  return {<m,i> | m <- def.modules, \module(_, imps, _ , _) := def.modules[m], i <- imps};
}


