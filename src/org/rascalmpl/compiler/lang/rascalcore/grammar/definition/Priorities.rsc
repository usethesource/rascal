  @license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascalcore::grammar::definition::Priorities

//extend ParseTree;
//import lang::rascalcore::grammar::definition::Grammar;
import lang::rascalcore::check::ATypeBase;

import Set;
import List;
import IO;
import util::Maybe;
import Node;
import Message;
 
import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::References;
import lang::rascalcore::format::Grammar;



data Associativity = prio();

public alias Extracted = rel[AProduction father, Associativity rule, AProduction child];
public alias DoNotNest = rel[AProduction father, int position, AProduction child];

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
public tuple[list[Message],DoNotNest] doNotNest(AGrammar g) {
  g = references(g); 
  result = {};
  msgs = [];
  
  for (s <- g.rules) {
    // note how the analysis is still _per non-terminal_
    // TODO: support relations between mutually recursive non-terminals
    // TODO: support 'deep' priority (for the ML case)
    // TODO: instead of DoNotNest generate data-dependent constraints
    defined = extract(g.rules[s]);

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
        msgs += warning("Not syntax-safe: <prod2rascal(f)> is both left and right associative.", f@\loc);
      else 
        msgs += warning("Not syntax-safe: <prod2rascal(f)> and <prod2rascal(c)> are both left and right associative to eachother.", f@\loc);
    }
    
    // here we test for accidental priority contradictions, remove them and warn about the removal.
    // TODO extract checking into separate function
    for (<f, c> <- (prios & prios<1,0>)) {
      if (f == c)
        msgs += warning("Not syntax-safe: <prod2rascal(f)> \> <prod2rascal(c)>, has a priority with itself.", f@\loc); 
      else 
        msgs += warning("Not syntax-safe: <prod2rascal(f)> {\<,\>} <prod2rascal(c)>, reflexive priority.", f@\loc); 
    }
     
    // and now we generated the filters, but only if 
    // ambiguity can be shown by matching left with right recursive positions
    // TODO: support mutually recursive non-terminals here
    result += 
        // left with right recursive 
        {<f,0,c> | <f:prod(AType ss, [AType lr, *_]), 
                    c:prod(AType t, [*_, AType rr])> <- (prios + rights + nons)
                 , same(ss, lr), same(t, rr), same(ss, t)} 
                   
        // right with left recursive            
        + {<f,size(pre),c> | <f:prod(AType ss, [pre*, AType rr]), 
                              c:prod(AType t, [AType lr,   *_])> <- (prios + lefts + nons)
                           , same(ss, rr), same(t, lr), same(ss, t)}
        ; 
        
     // and we warn about recursive productions which have been left ambiguous:
    allProds  = {p | /p:prod(_,_) := g.rules[s]};
    ambiguous = {<p, q>  | p:prod(AType ss, [AType lr, *_]) <- allProds, same(s, lr),
                           q:prod(AType t, [*_, AType rr]) <- allProds,
                            same(t, rr), same(ss, t)};
    ambiguous += {<p, q> | p:prod(AType ss, [pre*, AType rr]) <- allProds, same(s, rr), 
                           q:prod(AType t, [AType lr,   *_]) <- allProds,
                           same(t, lr), same(ss, t), <q, p> notin ambiguous}
              ;
              
    ambiguous -= (prios + prios<1,0>); // somehow the pairs are ordered
    ambiguous -= (groups + groups<1,0>); // somehow the pairs are associative
                  
    // TODO extract checking into separate function
    for (<p,q> <- ambiguous) {
         if (p == q) 
           msgs += warning("Ambiguity predicted: <prod2rascal(p)> lacks left or right associativity", p@\loc);
         else   
           msgs += warning("Ambiguity predicted: <prod2rascal(p)> and <prod2rascal(q)> lack left or right associativity or priority (\>)", p@\loc);    
    }
  }
    
  return < msgs, result + {*except(p, g) | /AProduction p <- g, p is prod || p is regular} >;
}

default Extracted extract(AProduction _) = {}; 

Extracted extract(choice(AType s, set[AProduction] alts)) 
  = {*extract(a) | a <- alts};

// note that nested associativity and priority under associativity was removed by an earlier rewrite rule
Extracted extract(associativity(AType s, Associativity a, set[AProduction] alts))  
  = {<x, a, y> | <x, y> <- alts * alts};

Extracted extract(priority(AType s, list[AProduction] levels)) 
  = {*extract(high, low) | [pre*, AProduction high, AProduction low, post*] := levels};

// the follow binary extract rules generate all priority _combinations_ in case of nested groups, 
// and also make sure these nested groups can generate the necessary associativity relations 
Extracted extract(high:prod(_,_), low:prod(_,_)) 
  = {<high, prio(), low>};
  
Extracted extract(choice(_, set[AProduction] alts), AProduction low)
  = {*extract(high, low) | high <- alts}; 
  
Extracted extract(AProduction high, choice(_, set[AProduction] alts))
  = {*extract(high, low) | low <- alts};
 
Extracted extract(AProduction a:associativity(_, _, set[AProduction] alts), AProduction low)
  = {*extract(high, low) | high <- alts}
  + extract(a); 
  
Extracted extract(AProduction high, AProduction a:associativity(_, _, set[AProduction] alts))
  = {*extract(high, low) | low <- alts}
  + extract(a);  

Extracted extract(AProduction p:priority(AType _, list[AProduction] alts), AProduction low)
  = extract(p)
  + {*extract(high, low) | high <- alts};   

Extracted extract(AProduction high, AProduction p:priority(AType _, list[AProduction] alts))
  = extract(p)
  + {*extract(high, low) | low <- alts};   

    
@doc{
This one-liner searches a given production for "except restrictions". 
For every position in the production that is restricted, and for every restriction it finds 
at this position, it adds a 'do-not-nest' tuple to the result.
}
public DoNotNest except(AProduction p:prod(AType _, list[AType] lhs), AGrammar g) 
  = { <p, i, q>  | i <- index(lhs), conditional(s, excepts) := delabel(lhs[i]), isdef(g, s)
                 , except(c) <- excepts, /q:prod(s,_) := g.rules[s], s.label==c};
 
//TODO: compiler issues when  g.rules[s]? is inlined
bool isdef(AGrammar g, AType s) = g.rules[s]?;


public DoNotNest except(AProduction p:regular(AType s), AGrammar g) {
  Maybe[AProduction] find(str c, AType t) = (/q:prod(t,_) := (g.rules[t]?choice(s,{}))) ? just(q) : nothing();
  
  switch (s) {
    case \opt(conditional(t,cs)) : 
      return {<p,0,q> | except(c) <- cs, just(q) := find(c,t)};
    case \iter-star(conditional(t,cs)) :
      return {<p,0,q> | except(c) <- cs, just(q) := find(c,t)};
    case \iter(conditional(t,cs)) :
      return {<p,0,q> | except(c) <- cs, just(q) := find(c,t)};
    case \iter-seps(conditional(t,cs),ss) :
      return {<p,0,q> | except(c) <- cs, just(q) := find(c,t)}
           + {<p,i+1,q> | i <- index(ss), conditional(u,css) := ss[i], except(ds) <- css, just(q) := find(ds,u)};
    case \iter-seps(_,ss) :
      return {<p,i+1,q> | i <- index(ss), conditional(u,css) := ss[i], except(ds) <- css, just(q) := find(ds,u)};
    case \iter-star-seps(conditional(t,cs),ss) :
      return {<p,0,q> | except(c) <- cs, just(q) := find(c,t)}
           + {<p,i+1,q> | i <- index(ss), conditional(u,css) := ss[i], except(ds) <- css, just(q) := find(ds,u)};
    case \iter-star-seps(_,ss) :
      return {<p,i+1,q> | i <- index(ss), conditional(u,css) := ss[i], except(ds) <- css, just(q) := find(ds,u)};       
    case \alt(as) :
      return {<p,0,q> | conditional(t,cs) <- as, except(c) <- cs, just(q) := find(c,t)};
    case \seq(ss) :
      return {<p,i,q> | i <- index(ss), conditional(t,cs) <- ss, except(c) <- cs, just(q) := find(c,t)};
     default: return {};
  }
}



private bool same(AType x, AType ref) = striprec(x) == striprec(ref);
