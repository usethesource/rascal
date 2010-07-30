@doc{
  This module implements the semantic of > in Rascal's syntax definition formalism.
  The factorize function translates the > into a hierarchy of related non-terminals
  which encode the relative priority of productions.
}
module rascal::parser::Priority
 
import rascal::parser::Grammar;
import rascal::parser::Normalization;
import ParseTree;
import List;
import Set;
import IO;

@doc{Generates a new non-terminal name for a priority level}
public Symbol level(Symbol s, int l) {
  return (l == 0) ? s : prime(s, "prio", [l]);
}

@doc{Generates a new non-terminal name for an associativity group}
public Symbol exclude(Symbol s, int p) {
  return prime(s, "assoc", [p]);
}

public Grammar factorize(Grammar g) {
  g.productions = {priority(p) | p <- g.productions};
  g.productions = {associativity(p) | p <- g.productions};
  return g;
}

@doc{
  Introduce new non-terminals to encode the (transitive) priority relation for a non-terminal. There can be
  only one priority relation per non-terminal. The static checker should enforce this.
  
  The semantics of this function depends highly on the normalization rules in rascal::parser::Normalization
}
public set[Production] priority(Production p) {
   // this code assumes there is only one first operator under a choice after normalization and static checking.
   if (choice(Symbol s, {set[Production] unordered, first(s, list[Production] prods)}) := p) {
      int len = size(prods);
   
      // first we give each level a number
      levels = for ([list[Production] prefix, Production elem, list[Production] postfix] := prods)
                 append makePrio(elem, s, size(postfix)); 
          
      // then we define each level by itself and copying all the productions from lower levels into it
      return { redefine(choice(s, {elem, removeFirst(unordered), toSet(prefix)}), level(s, l))
             | [list[Production] prefix, Production elem, list[Production] postfix] := levels
             , int l := size(postfix)
             };  
   }
   else {
     return {p};
   }
}

@doc{
  This function factors a non-terminal into several non-terminals to encode the semantics of associativity rules.
  It is assumed all first combinators have been removed earlier, and the grammar is normalized.
}
public set[Production] associativity(Production p) {
  int prime = 0; // to assign unique names to new non-terminals
  int newPrime() { 
    prime += 1; 
    return prime; 
  };
  
  if (choice(Symbol s, set[Production] alts) := p) {
     // first we split the total set in productions with and without associativity
     assocs  = { p | p:\assoc(_,_,_) <- alts}
             + { \assoc(s, a, {p}) | p:prod(_,_,attrs([_*,\assoc(a),_*])) <- alts};
     rest  = alts - assocs;
     
     // then we give each associativity group a number, and remove recursion here and there
     groups  = {<newPrime(), makeAssoc(p, s, a, prime)> | p:\assoc(s,a,g) <- assocs}; 
     
     // these are the original rules with just some recursion removed
     basic   = rest + groups<1>; 
     
     // now we generate new non-terminals that each exclude one of the groups
     new     = {redefine(choice(s, basic - p), exclude(s,i)) | <i,g> <- groups}; 
     
     // finally we reconstruct the basic non-terminal and remove all assoc rules
     return {removeAssoc(choice(s,basic))} + {removeAssoc(p) | p <- new};
  }    
  else {
    return {p};
  }
}

set[Production] removeFirst(set[Production] ps) {
  return visit(ps) {
      case first(Symbol s, list[Production] alts) => choice(s, toSet(alts))
  }
}

set[Production] removeAssoc(Production p) {
  return visit(p) {
      case \assoc(Symbol s, Associativity a, set[Production] alts) => choice(s, alts)
  }
}

@doc{
  Redefines some list of productions to produce a new symbol; it deals with all production combinators
}  
public list[Production] redefine(list[Production] prods, Symbol s) {
  return visit (prods) {
    case prod(list[Symbol] lhs, _, Attributes a) => prod(lhs, s, a)
    case choice(_, set[Production] alts) => choice(s, alts)
    case \assoc(_, Associativity a, Production p) => \assoc(s, a, p)
    case \diff(_, Production p, set[Production] alts) => \diff(s, p, alts)
    case \restrict(_, Production language, list[CharClass] restrictions) => restrict(s, language, restrictions)
    case Production x : throw "missed a case: <x>";
  }
}

@doc{
  Recognizes left-most and right-most recursive productions.
  It replaces each recursive non-terminal, if left-most or right-most, with a wrapped (level) non-terminal
}
public Production makePrio(Production p, Symbol s, int l) {
  return visit (p) {
    case prod([leftRec, list[Symbol] middle, rightRec],s,Attributes a) => 
         prod([level(s, l), middle, level(s, l)],s,a)   
    when checkSymbol(leftRec, s), checkSymbol(rightRec,s)   
    case prod([leftRec, list[Symbol] tail],s,Attributes a)      => 
         prod([level(s, l), tail], s, a)               
    when checkSymbol(leftRec, s)    
    case prod([list[Symbol] front, leftRec], s, Attributes a)   => 
         prod([front, level(s, l)], s, a)              
    when checkSymbol(leftRec, s)    
  }
}   

public Production makeAssoc(Production p, Symbol s, Associativity a, int l) {
  return visit (p) {
    case prod([Symbol leftRec, list[Symbol] middle, Symbol rightRec],s,Attributes a) => 
         prod([leftRec, middle, exclude(rightRec,l)],s,a)
      when a == \left() || a == \assoc(), checkSymbol(leftRec, s), checkSymbol(rightRec,s)
    case prod([Symbol leftRec, list[Symbol] middle, Symbol rightRec],s,Attributes a) => 
         prod([exclude(leftRec,l), middle, rightRec],s,a)
      when a == \right(), checkSymbol(leftRec, s), checkSymbol(rightRec,s)   
    case prod([Symbol leftRec, list[Symbol] middle, Symbol rightRec],s,Attributes a) => 
         prod([exclude(s,l), middle, exclude(s,l)],s,a)
      when a == \non-assoc(), checkSymbol(leftRec, s), checkSymbol(rightRec,s)
  }   
}

private bool checkSymbol(Symbol checked, Symbol referenced) {
  return referenced == checked || label(_, referenced) := checked;
}

private Attributes add(Attributes attrs, Attr a) {
  switch(attrs) {
    case \no-attrs() : return attrs([a]);
    case attrs(list[Attr] as) : return attrs([as, a]);
    default: throw "missed a case <attrs>";
  }
}