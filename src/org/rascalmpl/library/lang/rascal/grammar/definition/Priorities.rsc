@bootstrapParser
module lang::rascal::grammar::definition::Priorities

import ParseTree;
import Grammar;
import Set;
import List;
import IO;
 
import lang::rascal::grammar::definition::Productions;
import lang::rascal::grammar::definition::Symbols;

public alias DoNotNest = rel[Production father, int position, Production child];

public DoNotNest doNotNest(Grammar g) {
  return {*doNotNest(g.rules[s]) | s <- g.rules};
}

public DoNotNest doNotNest(Production p) {
  switch (p) {
    case prod(s, [list[Symbol] o,t],{_*,\assoc(left())}) :
      if (match(t, s)) return {<p, size(o), p>};
    case prod(s,[list[Symbol] o,t],{_*,\assoc(\assoc())}) :
      if (match(t, s)) return {<p, size(o), p>};
    case prod(s,[t,_*],{_*,\assoc(\right())}) :
      if (match(t, s)) return {<p, 0, p>}; 
    case prod(s,[t,list[Symbol] o,u],{_*,\assoc(\non-assoc())}) :
      if (match(t, s) && match(u, s)) return {<p, 0, p>,<p,size(o) + 1,p>};       
    case prod(s,[t,_*],{_*,\assoc(\non-assoc())}) :
      if (match(t, s)) return {<p, 0, p>}; 
    case prod(s,[list[Symbol] o,t],{_*,\assoc(\non-assoc())}) :
      if (match(t, s)) return {<p, size(o), p>};
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
  for ({Production pivot, set[Production] rest} := alts,  Production child:prod(_,_,_) := pivot) {
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
        if (match(l,rhs)) {
          result += {<father, 0, child>};
        }   
        else { 
          fail;
        }
      case prod(Symbol rhs,lhs:[_*,Symbol r],_) :
        if (match(r,rhs)) {
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
