module lang::rascal::grammar::analyze::Recursion

import Grammar;
import ParseTree;

import lang::rascal::grammar::definition::Symbols;

@doc{
  returns all non-terminals that can produce the empty string
}
set[Symbol] nullables(Grammar g) {
  rules = {p | /p:prod(_,_,_) := g};
  result = {striprec(nt) | prod(nt, [], _) <- rules};
  
  map[Symbol, Symbol] cache = ();
  Symbol addToCache(Symbol s) {
  	n = striprec(s);
  	cache[s] = n;
  	return n;
  }
  
  solve (result) 
    result += {p | p:prod(_,symbols,_) <- rules, all(nt <- symbols, (nt in cache ? cache[nt] :  addToCache(nt)) in result)};
  
  return result;
}

@doc{
  returns all non-terminals that eventually can produce an `exp` at the right-most position
}
set[Symbol] rightRecursive(Grammar g, Symbol exp) {
  rules = {p | /p:prod(_,_,_) := g};
  result = {exp};
  
  map[Symbol, Symbol] cache = ();
  Symbol addToCache(Symbol s) {
  	n = striprec(s);
  	cache[s] = n;
  	return n;
  }
  solve (result) 
    result += {(nt in cache ? cache[nt] :  addToCache(nt)) | p:prod(nt,[*_, r],_) <- rules, (r in cache ? cache[r] :  addToCache(r)) in result};
  
  
  return result;
}

@doc{
  returns all non-terminals that eventually can produce an `exp` at the left-most position
}
set[Symbol] leftRecursive(Grammar g, Symbol exp) {
  rules = {p | /p:prod(_,_,_) := g};
  result = {exp};
  
  map[Symbol, Symbol] cache = ();
  Symbol addToCache(Symbol s) {
  	n = striprec(s);
  	cache[s] = n;
  	return n;
  }
  solve (result) 
    result += {(nt in cache ? cache[nt] :  addToCache(nt)) | p:prod(nt,[r, *_],_) <- rules, (r in cache ? cache[r] :  addToCache(r)) in result};
  
  return result;
}
