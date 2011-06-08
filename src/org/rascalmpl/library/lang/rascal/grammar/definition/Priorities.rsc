module lang::rascal::grammar::definition::Priorities

import ParseTree;
import Grammar;
import Set;
import List;
import IO;
 
import lang::rascal::grammar::definition::Productions;
import lang::rascal::grammar::definition::Symbols;

public alias DoNotNest = rel[Production father, int position, Production child];

public DoNotNest doNotNest(Production p) {
  switch (p) {
    case prod([list[Symbol] o,t],s,attrs([_*,\assoc(left()),_*])) :
      if (match(t, s)) return {<p, size(o), p>};
    case prod([list[Symbol] o,t],s,attrs([_*,\assoc(\assoc()),_*])) :
      if (match(t, s)) return {<p, size(o), p>};
    case prod([t,_*],s,attrs([_*,\assoc(\right()),_*])) :
      if (match(t, s)) return {<p, 0, p>}; 
    case prod([t,list[Symbol] o,u],s,attrs([_*,\assoc(\non-assoc()),_*])) :
      if (match(t, s) && match(u, s)) return {<p, 0, p>,<p,size(o) + 1,p>};       
    case prod([t,_*],s,attrs([_*,\assoc(\non-assoc()),_*])) :
      if (match(t, s)) return {<p, 0, p>}; 
    case prod([list[Symbol] o,t],s,attrs([_*,\assoc(\non-assoc()),_*])) :
      if (match(t, s)) return {<p, size(o), p>};
    case choice(_, set[Production] alts) : 
      return {doNotNest(a) | a <- alts};
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
  for ({Production pivot, set[Production] rest} := alts,  Production child:prod(_,_,_) := pivot) {
    switch (a) {
      case \left(): 
        for (/Production father:prod(lhs:[_*,Symbol r],Symbol rhs,_) <- rest, match(r,rhs)) {
          result += {<father, size(lhs) - 1, child>};
        }
      case \assoc():
        for (/Production father:prod(lhs:[_*,Symbol r],Symbol rhs,_) <- alts, match(r,rhs)) 
          result += {<father, size(lhs) - 1, child>};
      case \right():
        for (/Production father:prod(lhs:[Symbol l,_*],Symbol rhs,_) <- alts, match(l,rhs)) 
          result += {<father, 0, child>};
      case \non-assoc(): {
        for (/Production father:prod(lhs:[_*,Symbol r],Symbol rhs,_) <- alts, match(r,rhs)) 
          result += {<father, size(lhs) - 1, child>};
        for (/Production father:prod(lhs:[Symbol l,_*],Symbol rhs,_) <- alts, match(l,rhs)) 
          result += {<father, 0, child>};
      }
    } 
  }
  
  return result + {doNotNest(x) | x <- alts};
}

DoNotNest priority(list[Production] levels) {
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
      case prod(lhs:[Symbol l,_*,Symbol r],Symbol rhs,_) : {
        if (match(l,rhs) && match(r,rhs)) {
          result += {<father, 0, child>, <father, size(lhs) - 1, child>};
        }   
        else {
          fail;
        }
      }
      case prod(lhs:[Symbol l,_*],Symbol rhs,_) :
        if (match(l,rhs)) {
          result += {<father, 0, child>};
        }   
        else { 
          fail;
        }
      case prod(lhs:[_*,Symbol r],Symbol rhs,_) :
        if (match(r,rhs)) {
          result += {<father, size(lhs) - 1, child>};
        }   
        else { 
          fail;
        }
    }
  }
  
  // and we recurse to find the nested associativity declarations
  return result + {doNotNest(l) | l <- levels};
}