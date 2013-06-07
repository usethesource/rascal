module lang::rascal::grammar::analyze::Recursion

import Grammar;
import ParseTree;
import Map;
import Set;
import List;
import analysis::graphs::Graph;


import lang::rascal::grammar::definition::Symbols;

private  map[Symbol, Symbol] cache = ();
private Symbol addToCache(Symbol s) {
  n = striprec(s);
  cache[s] = n;
  return n;
}

private Grammar current = grammar({}, (), ());

private Graph[Symbol] leftDependencies = {};
private Graph[Symbol] rightDependencies = {};

private void calculateDependencies(Grammar g) {
  cache = ();
  // collect both right and left at the same time
  rr = { *{
    <0, (r in cache ? cache[r] :  addToCache(r)), (nt in cache ? cache[nt] :  addToCache(nt))>
    , <1, (l in cache ? cache[l] :  addToCache(l)), (nt in cache ? cache[nt] :  addToCache(nt))>
    } | /p:prod(nt,syms,_) := g, size(syms) > 0, Symbol r := syms[-1], Symbol l := syms[0]};
    
  rightDependencies = rr[0];
  leftDependencies = rr[1];
  current = g;
}

private Graph[Symbol] getLeftDependencies(Grammar g) {
	if (g != current) {
		calculateDependencies(g);
	}
	return leftDependencies;
}
private Graph[Symbol] getRightDependencies(Grammar g) {
	if (g != current) {
		calculateDependencies(g);
	}
	return rightDependencies;
}

@doc{
  returns all non-terminals that can produce the empty string
}
set[Symbol] nullables(Grammar g) {
  rules = {p | /p:prod(_,_,_) := g};
  
  result = {(nt in cache ? cache[nt] :  addToCache(nt)) | prod(nt, [], _) <- rules};
  
  solve (result) 
    result += {p | p:prod(_,symbols,_) <- rules, all(nt <- symbols, (nt in cache ? cache[nt] :  addToCache(nt)) in result)};
  
  cache = ();
  return result;
}

@doc{
  returns all non-terminals that eventually can produce an `exp` at the right-most position
}
set[Symbol] rightRecursive(Grammar g, Symbol exp) {
  return reach(getRightDependencies(g), {exp});
}

@doc{
  returns all non-terminals that eventually can produce an `exp` at the left-most position
}
set[Symbol] leftRecursive(Grammar g, Symbol exp) {
  return reach(getLeftDependencies(g), {exp});
}
