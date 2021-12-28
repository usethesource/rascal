@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::grammar::definition::Priorities

extend ParseTree;
import Grammar;
import Set;
import List;
import IO;
import util::Maybe;
 
import lang::rascal::grammar::definition::Productions;
import lang::rascal::grammar::definition::Symbols;
import lang::rascal::grammar::definition::References;
import lang::rascal::format::Grammar;

data Associativity = prio();

public alias Extracted = rel[Production father, Associativity rule, Production child];
public alias DoNotNest = rel[Production father, int position, Production child];

@doc{
.Synopsis
Extract which productions are not to be nested under which other productions, at given 
recursive positions in the parents' defining symbols list.

.Description
This DoNotNest relation is generated from the grammar using the > priority definitions,
the associativity groups and the ! restriction operator. 

* The > generates a partial order among production rules, which is transitively closed
* The associativity groups fit into this partial order as equivalence classes on the same
  level in the priority ordering. Within these groups left and right recursive rules are
  limited according to the associativity declaration of the given group.
* The DoNotNest relation contains eventually only restrictions in case between two
  related productions an ambiguity can be "proven" to exist between the outermost (left-most
  and right-most) recursive occurences of the defined non-terminal. This is such that
  the disambiguation by priority and associativity remains 'syntax-safe'
* the non-assoc groups and ! are notably not 'syntax-safe', they remove sentences from non-terminals.
}
public DoNotNest doNotNest(Grammar g) {
  g = references(g); 
  DoNotNest result = {};
  
  for (s <- g.rules) {
    // note how the analysis is still _per non-terminal_
    // TODO: support relations between mutually recursive non-terminals
    // TODO: support 'deep' priority (for the ML case)
    // TODO: instead of DoNotNest generate data-dependent constraints
    Extracted defined = extract(g.rules[s]);

    // select and then close the different relations: left, right, non-assoc and priorities 
    // this is to make sure modular specifications (where rules for the same non-terminal are split 
    // over several definitions) have the same semantics as a flat, single rule definition.  
     
    // associativity groups are closed, i.e. `a left b && b left c ==> a left c` 
    lefts  = {<f, c> | <f, \left(), c>      <- defined}+;
    rights = {<f, c> | <f, \right(), c>     <- defined}+;
    nons    = {<f, c> | <f, \non-assoc(), c> <- defined}+;
    prios  = {<f, c> | <f, \prio(), c>      <- defined};
    
    // `a left b && b > c ==> a > c`, and for right and non-assoc the same
    // `a > b && b left c ==> a > c`, and for right and non-assoc the same
    // TODO: what about open alternative groups specified with |?
    groups = lefts + rights + nons;
    prios += prios o groups + groups o prios; 
    
    // the now complete priority relation is transitive
    prios  = prios+; 
   
    // here we test for some accidental associativity contradictions, remove them and warn about the removal
    // TODO extract checking into separate function
    for (<f, c> <- (lefts & rights)) {
      if (f == c)
        println("warning, not syntax-safe: <prod2rascal(f)> is both left and right associative.");
      else 
        println("warning, not syntax-safe: <prod2rascal(f)> and <prod2rascal(c)> are both left and right associative to eachother.");
    }
    
    // here we test for accidental priority contradictions, remove them and warn about the removal.
    // TODO extract checking into separate function
    for (<f, c> <- (prios & prios<1,0>)) {
      if (f == c)
        println("warning, not syntax-safe: <prod2rascal(f)> \> <prod2rascal(c)>, has a priority with itself."); 
      else 
        println("warning, not syntax-safe: <prod2rascal(f)> {\<,\>} <prod2rascal(c)>, reflexive priority."); 
    }
     
    // and now we generated the filters, but only if 
    // ambiguity can be shown by matching left with right recursive positions
    // TODO: support mutually recursive non-terminals here
    result += 
        // left with right recursive 
        {<f,0,c> | <f:prod(Symbol ss, [Symbol lr, *_], _), 
                    c:prod(Symbol t, [*_, Symbol rr], _)> <- (prios + rights + nons)
                 , same(ss, lr), same(t, rr), same(ss, t)} 
                   
        // right with left recursive            
        + {<f,size(pre),c> | <f:prod(Symbol ss, [*pre, Symbol rr], _), 
                              c:prod(Symbol t, [Symbol lr,   *_], _)> <- (prios + lefts + nons)
                           , same(ss, rr), same(t, lr), same(ss, t)}
        ; 
        
     // and we warn about recursive productions which have been left ambiguous:
    allProds  = {p | /p:prod(_,_,_) := g.rules[s]};
    ambiguous = {<p, q>  | p:prod(Symbol ss, [Symbol lr, *_], _) <- allProds, same(s, lr),
                           q:prod(Symbol t, [*_, Symbol rr], _) <- allProds,
                            same(t, rr), same(ss, t)};
    ambiguous += {<p, q> | p:prod(Symbol ss, [*_, Symbol rr], _) <- allProds, same(s, rr), 
                           q:prod(Symbol t, [Symbol lr,   *_], _) <- allProds,
                           same(t, lr), same(ss, t), <q, p> notin ambiguous}
              ;
              
    ambiguous -= (prios + prios<1,0>); // somehow the pairs are ordered
    ambiguous -= (groups + groups<1,0>); // somehow the pairs are associative
                  
    // TODO extract checking into separate function
    for (<p,q> <- ambiguous) {
         if (p == q) 
           println("warning, ambiguity predicted: <prod2rascal(p)> lacks left or right associativity");
         else   
           println("warning, ambiguity predicted: <prod2rascal(p)> and <prod2rascal(q)> lack left or right associativity or priority (\>)");    
    }
  }
    
  return result + {*except(p, g) | /Production p <- g, p is prod || p is regular};
}

default Extracted extract(Production _) = {}; 

Extracted extract(choice(Symbol s, set[Production] alts)) 
  = {*extract(a) | a <- alts};

// note that nested associativity and priority under associativity was removed by an earlier rewrite rule
Extracted extract(associativity(Symbol s, Associativity a, set[Production] alts))  
  = {<x, a, y> | <x, y> <- alts * alts};

Extracted extract(priority(Symbol s, list[Production] levels)) 
  = {*extract(high, low) | [*_, Production high, Production low, *_] := levels};

// the follow binary extract rules generate all priority _combinations_ in case of nested groups, 
// and also make sure these nested groups can generate the necessary associativity relations 
Extracted extract(high:prod(_,_,_), low:prod(_,_,_)) 
  = {<high, prio(), low>};
  
Extracted extract(choice(_, set[Production] alts), Production low)
  = {*extract(high, low) | high <- alts}; 
  
Extracted extract(Production high, choice(_, set[Production] alts))
  = {*extract(high, low) | low <- alts};
 
Extracted extract(Production a:associativity(_, _, set[Production] alts), Production low)
  = {*extract(high, low) | high <- alts}
  + extract(a); 
  
Extracted extract(Production high, Production a:associativity(_, _, set[Production] alts))
  = {*extract(high, low) | low <- alts}
  + extract(a);  

Extracted extract(Production p:priority(Symbol _, list[Production] alts), Production low)
  = extract(p)
  + {*extract(high, low) | high <- alts};   

Extracted extract(Production high, Production p:priority(Symbol _, list[Production] alts))
  = extract(p)
  + {*extract(high, low) | low <- alts};   

    
@doc{
This one-liner searches a given production for "except restrictions". 
For every position in the production that is restricted, and for every restriction it finds 
at this position, it adds a 'do-not-nest' tuple to the result.
}
public DoNotNest except(Production p:prod(Symbol _, list[Symbol] lhs, set[Attr] _), Grammar g) 
  = { <p, i, q>  | i <- index(lhs), conditional(s, excepts) := delabel(lhs[i]), isdef(g, s)
                 , except(c) <- excepts, /q:prod(label(c,s),_,_) := g.rules[s]};
 
//TODO: compiler issues when  g.rules[s]? is inlined
bool isdef(Grammar g, Symbol s) = g.rules[s]?;

//TODO compiler issues when  find is local to except
Maybe[Production] find(str c, Symbol s, Symbol t, Grammar g) {
    rules = g.rules[t]?choice(s,{});
    if(/Production q:prod(label(c,t),_,_) := rules) 
        return just(q);
    else {
        return nothing();
    }
}

public DoNotNest except(Production p:regular(Symbol s), Grammar g) {
  
  switch (s) {
    case \opt(conditional(t,cs)) : 
      return {<p,0,q> | except(c) <- cs, just(q) := find(c,s,t,g)};
    case \iter-star(conditional(t,cs)) :
      return {<p,0,q> | except(c) <- cs, just(q) := find(c,s,t,g)};
    case \iter(conditional(t,cs)) :
      return {<p,0,q> | except(c) <- cs, just(q) := find(c,s,t,g)};
    case \iter-seps(conditional(t,cs),ss) :
      return {<p,0,q> | except(c) <- cs, just(q) := find(c,s,t,g)}
           + {<p,i+1,q> | i <- index(ss), conditional(u,css) := ss[i], except(ds) <- css, just(q) := find(ds,s,u,g)};
    case \iter-seps(_,ss) :
      return {<p,i+1,q> | i <- index(ss), conditional(u,css) := ss[i], except(ds) <- css, just(q) := find(ds,s,u,g)};
    case \iter-star-seps(conditional(t,cs),ss) :
      return {<p,0,q> | except(c) <- cs, just(q) := find(c,s,t,g)}
           + {<p,i+1,q> | i <- index(ss), conditional(u,css) := ss[i], except(ds) <- css, just(q) := find(ds,s,u,g)};
    case \iter-star-seps(_,ss) :
      return {<p,i+1,q> | i <- index(ss), conditional(u,css) := ss[i], except(ds) <- css, just(q) := find(ds,s,u,g)};       
    case \alt(as) :
      return {<p,0,q> | conditional(t,cs) <- as, except(c) <- cs, just(q) := find(c,s,t,g)};
    case \seq(ss) :
      return {<p,i,q> | i <- index(ss), conditional(t,cs) <- ss, except(c) <- cs, just(q) := find(c,s,t,g)};
     default: return {};
  }
}



private bool same(Symbol x, Symbol ref) = striprec(x) == striprec(ref);
