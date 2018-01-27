@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascalcore::grammar::definition::Priorities

//import ParseTree;
import lang::rascalcore::check::AType;
import lang::rascalcore::grammar::definition::Grammar;
import Set;
import List;
import IO;
import util::Maybe;
 
import lang::rascalcore::grammar::definition::Productions;
import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::Lookahead;
// import lang::rascalcore::grammar::analyze::Recursion;

public alias Priorities = rel[AProduction father, AProduction child];
public alias DoNotNest = rel[AProduction father, int position, AProduction child];

public DoNotNest doNotNest(AGrammar g) {
  DoNotNest result = {};
  
  for (s <- g.rules) {
    lefties = {s}; // leftRecursive(g, s);
    righties = {s}; //rightRecursive(g, s);
    <ordering,ass> = doNotNest(g.rules[s], lefties, righties);
    result += ass;
    
    ordering = ordering+; // priority is transitive
   
    for (<AProduction father, AProduction child> <- ordering) {
      switch (father) {
        case prod(AType rhs,lhs:[AType l,_*,AType r],_) : {
          if (match(l,lefties) && match(r,righties)) {
            if (prod(AType crhs,clhs:[_*,AType cl],_) := child, match(cl,righties)) {
            result += {<father, 0, child>};
          }
          if (prod(AType crhs,clhs:[AType cl,_*],_) := child, match(cl,lefties)) {
            result += {<father, size(lhs) - 1, child>};
          }
        }   
        else fail;
      }
      case prod(AType rhs,lhs:[AType l,_*],_) :
        if (match(l,lefties), prod(AType crhs,clhs:[_*,AType cl],_) := child, match(cl,righties)) {
          result += {<father, 0, child>};
        }   
        else { 
          fail;
        }
      case prod(AType rhs,lhs:[_*,AType r],_) :
        if (match(r,righties), prod(AType crhs,clhs:[AType cl,_*],_) := child, match(cl,lefties)) {
          result += {<father, size(lhs) - 1, child>};
        }   
        else { 
          fail;
        }
      }
    } 
  }
  
  return result // TODO: in the future the except relation needs to be reported separately because it should not be indirect.
       + {*except(p, g) | /AProduction p <- g, p is prod || p is regular}
       ;
}

@doc{
This one-liner searches a given production for "except restrictions". 
For every position in the production that is restricted, and for every restriction it finds 
at this position, it adds a 'do-not-nest' tuple to the result.
}
public DoNotNest except(AProduction p:prod(AType _, list[AType] lhs, SyntaxRole _), AGrammar g) 
  = { <p, i, q>  | i <- index(lhs), conditional(s, excepts) := delabel(lhs[i]), isdef(g, s), except(c) <- excepts, /q:prod(s,_,_) := g.rules[s], s.label==c};
 

//TODO: compiler issues when  g.rules[s]? is inlined
bool isdef(AGrammar g, AType s) = g.rules[s]?;


public DoNotNest except(AProduction p:regular(AType s), AGrammar g) {
  Maybe[AProduction] find(str c, AType t) = (/q:prod(t,_,_) := (g.rules[t]?choice(s,{})) && t.label==c) ? just(q) : nothing();
  
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
  
  return {};
}


public tuple[Priorities prio,DoNotNest ass] doNotNest(AProduction p, set[AType] lefties, set[AType] righties) {
  switch (p) {
    case prod(s, [t, *AType \o, u],_,attributes={_*,\assoc(left())}) :
      if (match(t, lefties), match(u,righties)) return <{},{<p, size(\o) + 1, p>}>;
    case prod(s,[t, *AType \o, u],_,attributes={_*,\assoc(\assoc())}) :
      if (match(t, lefties), match(u, righties)) return <{},{<p, size(\o) + 1, p>}>;
    case prod(s,[t,_*,u],_,attributes={_*,\assoc(\right())}) :
      if (match(t, lefties), match(u, righties)) return <{},{<p, 0, p>}>; 
    case prod(s,[t, *AType \o, u],_,attributes={_*,\assoc(\non-assoc())}) :
      if (match(t, lefties) && match(u, righties)) return <{},{<p, 0, p>,<p,size(\o) + 1,p>}>;       
    case prod(s,[t,_*],_,attributes={_*,\assoc(\non-assoc())}) :
      if (match(t, lefties)) return <{},{<p, 0, p>}>; 
    case prod(s,[*AType \o, t],_,attributes={_*,\assoc(\non-assoc())}) :
      if (match(t, righties)) return <{},{<p, size(\o), p>}>;
    case choice(_, set[AProduction] alts) : {
        Priorities pr = {}; DoNotNest as = {};
        for (a <- alts, <prA,asA> := doNotNest(a, lefties, righties)) {
          pr += prA;
          as += asA;
        }
        return <pr, as>; 
      }
    case \lookahead(_,_,q) :
      return doNotNest(q, lefties, righties); 
    case priority(_, list[AProduction] levels) : 
      return priority(levels, lefties, righties);
    case \associativity(_, Associativity a, set[AProduction] alts) : 
      return associativity(a, alts, lefties, righties);
  }
  
  return <{},{}>;
}

tuple[Priorities,DoNotNest] associativity(Associativity a, set[AProduction] alts, set[AType] lefties, set[AType] righties) {
  result = {};
  
  // note that there are nested groups and that each member of a nested group needs to be paired
  // with all the members of the other nested group. This explains the use of the / deep match operator.
  for ({AProduction pivot, *AProduction rest} := alts,  AProduction child:prod(_,_,_) := pivot) {
    switch (a) {
      case \left(): 
        result += {<father, size(lhs) - 1, child> | /AProduction father:prod(AType rhs,lhs:[_*,AType r],_) <- rest, match(r,righties)};  
      case \assoc():
        result += {<father, size(lhs) - 1, child> | /AProduction father:prod(AType rhs,lhs:[_*,AType r],_) <- rest, match(r,righties)};
      case \right():
        result += {<father, 0, child>             | /AProduction father:prod(AType rhs,lhs:[AType l,_*],_) <- rest, match(l,lefties)};
      case \non-assoc(): {
        result += {<father, size(lhs) - 1, child> | /AProduction father:prod(AType rhs,lhs:[_*,AType r],_) <- rest, match(r,righties)}
                + {<father, 0, child>             | /AProduction father:prod(AType rhs,lhs:[AType l,_*],_) <- rest, match(l,lefties)};
      }
    } 
  }
  
  pr = {};
  for (x <- alts, <prX,asX> := doNotNest(x, lefties, righties)) {
    pr += prX;
    result += asX;
  }
  
  return <pr, result>;
}

public tuple[Priorities,DoNotNest] priority(list[AProduction] levels, set[AType] lefties, set[AType] righties) {
  // collect basic filter
  ordering = { <father,child> | [pre*,AProduction father, AProduction child, post*] := levels };

  // flatten nested structure to obtain direct relations
  todo = ordering;
  ordering = {};
  while (todo != {}) {
    <prio,todo> = takeOneFrom(todo);
    switch (prio) {
      case <choice(_,set[AProduction] alts),AProduction child> :
        todo += alts * {child};
      case <AProduction father, choice(_,set[AProduction] alts)> :
        todo += {father} * alts;
      case <associativity(_,_,set[AProduction] alts),AProduction child> :
        todo += alts * {child};
      case <AProduction father, associativity(_,_,set[AProduction] alts)> :
        todo += {father} * alts;
      default:
        ordering += prio;
    }
  }
  
  DoNotNest as = {};
  for (x <- levels, <prX,asX> := doNotNest(x, lefties, righties)) {
    ordering += prX;
    as += asX;
  }
  
  return <ordering, as>;
}

private bool match(AType x, set[AType] reference) = striprec(x) in reference;
