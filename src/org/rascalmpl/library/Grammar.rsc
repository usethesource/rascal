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

@synopsis{A simple but effective internal format for the representation of context-free grammars.}
module Grammar

extend ParseTree;


@synopsis{The Grammar datatype}
@description{
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
  map[Symbol, Production] rules = ();

  for (p <- prods) {
    t = (p.def is label) ? p.def.symbol : p.def;

    if (t in rules) {
        if (choice(_, existing) := rules[t]) {
            rules[t] = choice(t, existing + p);
        }
        else {
            rules[t] = choice(t, {p, rules[t]});
        }
    }
    else {
        rules[t] = choice(t, {p});
    }
  } 
  return grammar(starts, rules);
} 
           
Grammar grammar(type[&T <: Tree] sym)
	= grammar({sym.symbol}, sym.definitions);

  

@synopsis{An item is an index into the symbol list of a production rule.}  
data Item = item(Production production, int index);


@synopsis{Compose two grammars.}
@description{
Compose two grammars by adding the rules of g2 to the rules of g1.
The start symbols of g1 will be the start symbols of the resulting grammar.
}
public Grammar compose(Grammar g1, Grammar g2) {
  for (s <- g2.rules)
    if (g1.rules[s]?)
      g1.rules[s] = choice(s, {g1.rules[s], g2.rules[s]});
    else
      g1.rules[s] = g2.rules[s];
  g1.starts += g2.starts;

  reduced_rules = ();
  for(s <- g1.rules){
  	  c = g1.rules[s];
  	  if (c is choice) {
  	    c.alternatives -= { *choices | priority(_, choices) <- c.alternatives } +
  		                  { *alts | associativity(_, _, alts) <- c.alternatives};
  	  }	                
  	  reduced_rules[s] = c;
  }
  
  return grammar(g1.starts, reduced_rules);
}    

// TODO:COMPILER
// The above code (temporarily?) replaces the following code
// Reason: the algorithm is faster and compiled code chokes in the set matches
// for not yet known reason.

//public Grammar compose(Grammar g1, Grammar g2) {
//  set[Production] empty = {};
//  for (s <- g2.rules)
//    if (g1.rules[s]?)
//      g1.rules[s] = choice(s, {g1.rules[s], g2.rules[s]});
//    else
//      g1.rules[s] = g2.rules[s];
//  g1.starts += g2.starts;
//
//  return innermost visit(g1) {
//    case c:choice(_, {p, *r, Production x:priority(_,/p)}) => c[alternatives = {x, *r}]
//    case c:choice(_, {p, *r, Production x:associativity(_,_,/p)}) => c[alternatives = {x, *r}]
//  };
//}    

@synopsis{Compute a relation from extender to extended module for the given grammar}
@description{
Note that this relation is already transitively closed because that is the semantics of extend.
}
rel[str \module, str extended] extends(GrammarDefinition def) {
  return {<m,e> | m <- def.modules, \module(_, _, exts , _) := def.modules[m], e <- exts}+;
}

@synopsis{Compute a relation from importer to imported modules for the given grammar}
rel[str \module, str imported] imports(GrammarDefinition def) {
  return {<m,i> | m <- def.modules, \module(_, imps, _ , _) := def.modules[m], i <- imps};
}

@synopsis{Compute which modules directly depend on which other modules.}
@description{
This function computes dependencies via import and extend relations. Every module
X that imports Y or extends Y ends up in the result as <X, Y>. The extends relation
that we use is already transitively closed. Next to this we also add dependencies
<X, Z> for all modules X that import Y which extends Z. Because of the transitive
nature of module extension, a module that extends another module exposes all
rules to any importing module. 
}
rel[str \module, str dependency] dependencies(GrammarDefinition def) {
  imps = imports(def);
  exts = extends(def);

  return imps + exts + imps o exts;
}


