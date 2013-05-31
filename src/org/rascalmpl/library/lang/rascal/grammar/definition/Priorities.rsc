@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::grammar::definition::Priorities

import ParseTree;
import Grammar;
import Set;
import List;
import IO;
import util::Maybe;
 
import lang::rascal::grammar::definition::Productions;
import lang::rascal::grammar::definition::Symbols;

public alias DoNotNest = rel[Production father, int position, Production child];

public DoNotNest doNotNest(Grammar g) {
  return {*doNotNest(g.rules[s]) | s <- g.rules}
       + {*except(p, g) | /Production p <- g, p is prod || p is regular}
       ;
}

@doc{
This one-liner searches a given production for "except restrictions". 
For every position in the production that is restricted, and for every restriction it finds 
at this position, it adds a 'do-not-nest' tuple to the result.
}
public DoNotNest except(Production p:prod(Symbol _, list[Symbol] lhs, set[Attr] _), Grammar g) 
  = { <p, i, q>  | i <- index(lhs), conditional(s, excepts) := delabel(lhs[i]), g.rules[s]?, except(c) <- excepts, /q:prod(label(c,s),_,_) := g.rules[s]};
  
public DoNotNest except(Production p:regular(Symbol s), Grammar g) {
  Maybe[Production] find(str c, Symbol t) = (/q:prod(label(c,t),_,_) := (g.rules[t]?choice(s,{}))) ? just(q) : nothing();
  
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


public DoNotNest doNotNest(Production p) {
  switch (p) {
    case prod(s, [*Symbol \o, t],{_*,\assoc(left())}) :
      if (match(t, s)) return {<p, size(\o), p>};
    case prod(s,[*Symbol \o, t],{_*,\assoc(\assoc())}) :
      if (match(t, s)) return {<p, size(\o), p>};
    case prod(s,[t,_*],{_*,\assoc(\right())}) :
      if (match(t, s)) return {<p, 0, p>}; 
    case prod(s,[t, *Symbol \o, u],{_*,\assoc(\non-assoc())}) :
      if (match(t, s) && match(u, s)) return {<p, 0, p>,<p,size(\o) + 1,p>};       
    case prod(s,[t,_*],{_*,\assoc(\non-assoc())}) :
      if (match(t, s)) return {<p, 0, p>}; 
    case prod(s,[*Symbol \o, t],{_*,\assoc(\non-assoc())}) :
      if (match(t, s)) return {<p, size(\o), p>};
    case choice(_, set[Production] alts) :
      return {*doNotNest(a) | a <- alts}; 
    case \lookahead(_,_,q) :
      return doNotNest(q); 
    case priority(_, list[Production] levels) : 
      return priority(levels);
    case \associativity(_, Associativity a, set[Production] alts) : 
      return associativity(a, alts);
  }
  
  return {};
}

DoNotNest associativity(Associativity a, set[Production] alts) {
  result = {};
  
  // note that there are nested groups and that each member of a nested group needs to be paired
  // with all the members of the other nested group. This explains the use of the / deep match operator.
  for ({Production pivot, *Production rest} := alts,  Production child:prod(_,_,_) := pivot) {
    switch (a) {
      case \left(): 
        for (/Production father:prod(Symbol rhs, lhs:[_*,Symbol r],_) <- rest, match(r,rhs)) 
          result += {<father, size(lhs) - 1, child>};
      case \assoc():
        for (/Production father:prod(Symbol rhs,lhs:[_*,Symbol r],_) <- alts, match(r,rhs)) 
          result += {<father, size(lhs) - 1, child>};
      case \right():
        for (/Production father:prod(Symbol rhs,lhs:[Symbol l,_*],_) <- alts, match(l,rhs)) 
          result += {<father, 0, child>};
      case \non-assoc(): {
        for (/Production father:prod(Symbol rhs,lhs:[_*,Symbol r],_) <- alts, match(r,rhs)) 
          result += {<father, size(lhs) - 1, child>};
        for (/Production father:prod(Symbol rhs,lhs:[Symbol l,_*],_) <- alts, match(l,rhs)) 
          result += {<father, 0, child>};
      }
    } 
  }
  return result + {*doNotNest(x) | x <- alts};  
}

public DoNotNest priority(list[Production] levels) {
  // collect basic filter
  ordering = { <father,child> | [pre*,Production father, Production child, post*] := levels };

  // flatten nested structure to obtain direct relations
  todo = ordering;
  ordering = {};
  while (todo != {}) {
    <prio,todo> = takeOneFrom(todo);
    switch (prio) {
      case <choice(_,set[Production] alts),Production child> :
        todo += alts * {child};
      case <Production father, choice(_,set[Production] alts)> :
        todo += {father} * alts;
      case <associativity(_,_,set[Production] alts),Production child> :
        todo += alts * {child};
      case <Production father, associativity(_,_,set[Production] alts)> :
        todo += {father} * alts;
      default:
        ordering += prio;
    }
  }
  
  ordering = ordering+; // priority is transitive

  result = {};
  for (<Production father, Production child> <- ordering) {
    switch (father) {
      case prod(Symbol rhs,lhs:[Symbol l,_*,Symbol r],_) : {
        if (match(l,rhs) && match(r,rhs)) {
          if (prod(Symbol crhs,clhs:[_*,Symbol cl],_) := child, match(cl,crhs)) {
            result += {<father, 0, child>};
          }
          if (prod(Symbol crhs,clhs:[Symbol cl,_*],_) := child, match(cl,crhs)) {
            result += {<father, size(lhs) - 1, child>};
          }
        }   
        else fail;
      }
      case prod(Symbol rhs,lhs:[Symbol l,_*],_) :
        if (match(l,rhs), prod(Symbol crhs,clhs:[_*,Symbol cl],_) := child, match(cl,crhs)) {
          result += {<father, 0, child>};
        }   
        else { 
          fail;
        }
      case prod(Symbol rhs,lhs:[_*,Symbol r],_) :
        if (match(r,rhs), prod(Symbol crhs,clhs:[Symbol cl,_*],_) := child, match(cl,crhs)) {
          result += {<father, size(lhs) - 1, child>};
        }   
        else { 
          fail;
        }
    }
  }
  
  // and we recurse to find the nested associativity declarations
  return result + {*doNotNest(l) | l <- levels};  
}

@doc{
  Simply replace the structures for priority and associativity by normal alternatives, ceteris paribus.
}
public Grammar prioAssocToChoice(Grammar g) = visit(g) {
  case \priority(def, list[Production] levels) => choice(def, {*levels})
  case \associativity(def, _, alts)            => choice(def, alts)
};

data Symbol = ind(Symbol symbol, str index);

Symbol ind(ind(Symbol s, str i), str j) = ind(s, "<i>_<j>");

&T nolabels(&T p) = visit(p) { 
  case ind(s, _) => s 
};

set[Production] exclude(set[Production] base, Production child) 
  = { e | e <- base, nolabels(e) != nolabels(child) };

Production setHead(Symbol nt, Production p) = visit (p) {
  case prod(_, b, a) => prod(nt, b, a)
  case choice(Symbol _, as) => choice(nt, as)
};

alias InvGrammar = map[set[Production] alts, Symbol nt];

@doc{
  This function rewrites a grammar to introduce one new non-terminal. It takes an existing non-terminal, and removes
  one rule. Then it changes the use site of the old non-terminal in a specific rule at at a specific position to use this
  new non-terminal. The function reuses existing definitions if they already define the same set of rules. 
}
tuple[Grammar, InvGrammar, Symbol] factorOne(Symbol (Symbol) new, Grammar g, InvGrammar done, Production alternative, <Production father, int pos, Production child>) {
   Symbol nt;
   Symbol def = alternative.def;
       
   // filter the direct child
   ex = exclude(g.rules[alternative.symbols[pos]].alternatives, child);
   nex = nolabels(ex);
       
   // see if we have a non-terminal that defines this exact set already, otherwise generate a new one
   if (nex in done) { 
     nt = done[nex];     
   } else {
     nt = new(alternative.symbols[pos]);
     done[nex] = nt;
   }
     
   // change the old definition to use the new non-terminal
   // I want to be able to write this instead: `g.rules[def] = visit(g.rules[def]) { case alternative => alternative[symbol[pos] = nt] };`
   old = alternative;
   g.rules[def].alternatives -= {old};
   alternative.symbols[pos] = nt;
   g.rules[def].alternatives += {alternative};
   
   // add the new definition to the grammar 
   g.rules[nt] = setHead(nt, choice(nt, ex));
   
   return <g, done, nt>;
}

@doc{
  Factor the rules for non-terminals with do-not-nest patterns such that
  new non-terminals are introduced for the parts where less alternatives are
  allowed as declared by the patterns. The new non-terminals are labeled
  using ind(Symbol, str index) constructor, such that they can easily be
  removed later when constructing trees for example.
}
Grammar factor(Grammar g, DoNotNest patterns) {
  n = 1;
  Symbol new(Symbol x) { res = ind(x, "<n>");  n += 1; return res; }
 
  done = (g.rules[nt].alternatives : nt | nt <- g.rules); 
  
  solve(g) 
    // in every iteration we apply all patterns to the current definitions,
    // this generates new definitions that may need patterns applied to
    // we stop when no more filters can be applied 
    for (def <- g.rules, alternative <- g.rules[def].alternatives                    // loop over the grammar
       , pattern:<parent, int pos, child> <- patterns                                // loop over all patterns
       , parent == nolabels(alternative)                                             // found a match 
       , nolabels(child) in nolabels(g.rules[alternative.symbols[pos]]).alternatives // stop condition: do not apply if not necessary
       ) { 
       <g, done, nt> = factorOne(new, g, done, alternative, pattern);
       
       // now take care of the deeper nested problems on the left side
       if (pos == 0) {
         visit(g.rules[nt].alternatives) {      // for all alternatives of the new non-terminal
           case p:prod(x,[*syms, r],as) :       // which are right-recursive
             if (nolabels(r) == nolabels(nt))  
               <g, done, x> = factorOne(new, g, done, p, <p, size(syms), child>); // create a new non-terminal without the left-recursive child 
         }
       }
       // and the right side
       if (pos == size(parent.symbols) - 1) { 
         visit(g.rules[nt].alternatives) {     // for all alternatives of the new non-terminal
           case p:prod(x,[r, *syms],as) :      // which are left-recursive
             if (nolabels(r) == nolabels(nt))  
               <g, done, x> = factorOne(new, g, done, p, <p, 0, child>); // create a new non-terminal without the left-recursive child
         } 
       }
    }
  
  return g;
}

