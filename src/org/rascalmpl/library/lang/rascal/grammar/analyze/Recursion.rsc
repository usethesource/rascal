module lang::rascal::grammar::analyze::Recursion

import Grammar;
import ParseTree;

import lang::rascal::grammar::definition::Symbols;

private bool globalCache = false;
void turnOnSymbolCache() { globalCache = true; }
void turnOffSymbolCache() { globalCache = false; clearCache(); }


private  map[Symbol, Symbol] cache = ();
private void clearCache() { if (!globalCache) cache = (); }
private Symbol addToCache(Symbol s) {
  n = striprec(s);
  cache[s] = n;
  return n;
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
  rules = {p | /p:prod(_,_,_) := g};
  result = {exp};
  
  solve (result) 
    result += {(nt in cache ? cache[nt] :  addToCache(nt)) | p:prod(nt,[*_, r],_) <- rules, (r in cache ? cache[r] :  addToCache(r)) in result};
  
  clearCache();
  return result;
}

@doc{
  returns all non-terminals that eventually can produce an `exp` at the left-most position
}
set[Symbol] leftRecursive(Grammar g, Symbol exp) {
  rules = {p | /p:prod(_,_,_) := g};
  result = {exp};
  
  solve (result) 
    result += {(nt in cache ? cache[nt] :  addToCache(nt)) | p:prod(nt,[r, *_],_) <- rules, (r in cache ? cache[r] :  addToCache(r)) in result};
  
  clearCache();
  return result;
}
