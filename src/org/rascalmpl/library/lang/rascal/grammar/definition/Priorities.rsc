module lang::rascal::grammar::definition::Priorities

import ParseTree;
import Grammar;



public alias DoNotNest = rel[Production father, int position, Production child];

public DoNotNest doNotNest(Production p) {
  switch (p) {
    case prod([o*,t],s,attrs([_*,\assoc(left()),_*])) :
      if (match(t, s)) return {<p, size(o), p>};
    case prod([o*,t],s,attrs([_*,\assoc(\assoc()),_*])) :
      if (match(t, s)) return {<p, size(o), p>};
    case prod([t,_*],s,attrs([_*,\assoc(\right()),_*])) :
      if (match(t, s)) return {<p, 0, p>}; 
    case prod([t,o*,u],s,attrs([_*,\assoc(\non-assoc()),_*])) :
      if (match(t, s) && match(u, s)) return {<p, 0, p>,<p,size(o) + 1,p>};       
    case prod([t,_*],s,attrs([_*,\assoc(\non-assoc()),_*])) :
      if (match(t, s)) return {<p, 0, p>}; 
    case prod([o*,t],s,attrs([_*,\assoc(\non-assoc()),_*])) :
      if (match(t, s)) return {<p, size(o), p>};
    case choice(_, set[Production] alts) : 
      return {doNotNest(a) | a <- alts};
     case \lookahead(_,_,q) :
      return doNotNest(q); 
    case priority(_, list[Production] levels) : 
      return priority(levels);
    case \assocativity(_, Associativity a, set[Production] alts) : 
      return associativity(a, alts);
  }
  
  return {};
}

DoNotNest associativity(Associativity a, set[Production] alts) {
  result = {};

  // note that there are nested groups and that each member of one group needs to be paired
  // with all the members of the other group. This explains the use of the / deep match operator.
  for ({Production pivot, set[Production] rest} := alts, /Production child:prod(_,_,_) := pivot) {
    switch (a) {
      case \left(): 
        for (/Production father:prod(lhs:[_*,Symbol r],Symbol rhs,_) <- rest, match(r,rhs)) 
          result += {<father, size(lhs) - 1, child>};
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
  
  return result + {dontNest(a) | a <- alts};
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
      case <\assoc(_,_,set[Production] alts),Production child> :
        todo += alts * {child};
      case <Production father, \assoc(_,_,set[Production] alts)> :
        todo += {father} * alts;
      default:
        ordering += prio;
    }
  }
  
  ordering = ordering+; // priority is transitive

  result = {};
  for (<Production father, Production child> <- ordering) {
    switch (father) {
      case prod([Symbol l,_*,Symbol r],Symbol rhs,_) :
        if (match(l,rhs) && match(l,rhs)) 
          result += {<father, 0, child>};   
        else fail;
      case prod([Symbol l,_*],Symbol rhs,_) :
        if (match(l,rhs))
          result += {<father, 0, child>};   
        else fail;
      case prod([_*,Symbol r],Symbol rhs,_) :
        if (match(r,rhs))
          result += {<father, size(lhs) - 1, child>};   
        else fail;
    }
  }
  
  // and we recurse to find the nested associativity declarations
  return result + {doNotNest(l) | l <- levels};
}