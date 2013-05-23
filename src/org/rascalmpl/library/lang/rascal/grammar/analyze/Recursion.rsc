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
  
  solve (result) 
    result += {p | p:prod(_,symbols,_) <- rules, all(nt <- symbols, striprec(nt) in result)};
  
  return result;
}

@doc{
  returns all non-terminals that eventually can produce an `exp` at the right-most position
}
set[Symbol] rightRecursive(Grammar g, Symbol exp) {
  rules = {p | /p:prod(_,_,_) := g};
  result = {exp};
  
  solve (result) 
    result += {striprec(nt) | p:prod(nt,[*_, r],_) <- rules, striprec(r) in result};
  
  
  return result;
}

@doc{
  returns all non-terminals that eventually can produce an `exp` at the left-most position
}
set[Symbol] leftRecursive(Grammar g, Symbol exp) {
  rules = {p | /p:prod(_,_,_) := g};
  result = {exp};
  
  solve (result) 
    result += {striprec(nt) | p:prod(nt,[r, *_],_) <- rules, striprec(r) in result};
  
  return result;
}
