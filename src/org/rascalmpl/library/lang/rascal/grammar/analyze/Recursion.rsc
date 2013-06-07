module lang::rascal::grammar::analyze::Recursion

import Grammar;
import ParseTree;
import Map;
import Set;
import analysis::graphs::Graph;


import lang::rascal::grammar::definition::Symbols;

private bool globalCache = false;
void turnOnSymbolCache() { globalCache = true; }
void turnOffSymbolCache() { globalCache = false; clearCache(); }


private  map[Symbol, Symbol] cache = ();
private map[Grammar, set[Production]] prodCache = ();
private void clearCache() { if (!globalCache) { cache = (); prodCache = (); } }
private Symbol addToCache(Symbol s) {
  n = striprec(s);
  cache[s] = n;
  return n;
}

private set[Production] getProds(Grammar g) {
	if (g notin prodCache) {
		prodCache[g] = { p | /p:prod(_,_,_) := g};
	}
	return prodCache[g];
}

@doc{
  returns all non-terminals that can produce the empty string
}
set[Symbol] nullables(Grammar g) {
  rules = {p | /p:prod(_,_,_) := g};
  
  result = {(nt in cache ? cache[nt] :  addToCache(nt)) | prod(nt, [], _) <- rules};
  
  solve (result) 
    result += {p | p:prod(_,symbols,_) <- rules, all(nt <- symbols, (nt in cache ? cache[nt] :  addToCache(nt)) in result)};
  
  clearCache();
  return result;
}

@doc{
  returns all non-terminals that eventually can produce an `exp` at the right-most position
}
set[Symbol] rightRecursive(Grammar g, Symbol exp) {
  prods = {<(r in cache ? cache[r] :  addToCache(r)), (nt in cache ? cache[nt] :  addToCache(nt))> | prod(nt,[*_, r],_) <- getProds(g)};
  clearCache();
  return reach(prods, {exp});
}

@doc{
  returns all non-terminals that eventually can produce an `exp` at the left-most position
}
set[Symbol] leftRecursive(Grammar g, Symbol exp) {
  prods = {<(r in cache ? cache[r] :  addToCache(r)), (nt in cache ? cache[nt] :  addToCache(nt))> | prod(nt,[r, *_],_) <- getProds(g)};
  clearCache();
  return reach(prods, {exp});
}
