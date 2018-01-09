@doc{
  This module implements the semantics of the ... and :cons notations in Rascal grammars.
  
  To give the proper semantics to priorities, this must be run before the priority relation
  is expanded.
}
module lang::rascal::grammar::definition::References

extend Grammar;
extend ParseTree;
import Node;
import lang::rascal::grammar::definition::Symbols;
import IO;

Grammar references(Grammar g) {
   g = visit (g) {
      case reference(Symbol s, str name) => p 
        when ss := striprec(s)
           , ss in g.rules
           , /Production p:prod(label(name, t), _, _) := g.rules[ss]
           , ss == striprec(t)
   };
  
   g = visit (g) {
      case others(Symbol s)              => g.rules[s] 
        when striprec(s) in g.rules
   };
    
   g = visit (g) {
      case priority(s, l) => priority(s, [e | e <-l, !(e is others)])
      case choice(s, alts) => choice(s, {e | e <- alts, !(e is others)})
      case associativity(s, a, alts) => associativity(s, a, {e | e <- alts, !(e is others)})
   } 
  
  return g;
}
