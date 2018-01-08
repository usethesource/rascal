@doc{
  This module implements the semantics of the ... and :cons notations in Rascal grammars.
  
  To give the proper semantics to priorities, this must be run before the priority relation
  is expanded.
}
module lang::rascal::grammar::definition::References

extend Grammar;
extend ParseTree;
import lang::rascal::grammar::definition::Symbols;

Grammar references(Grammar g) = visit (g) {
  case others(Symbol s)              => g.rules[s] 
    when s in g.rules
    
  case reference(Symbol s, str name) => p 
    when s in g.rules, /Production p:prod(label(name, t), _, _) := g.rules[s], striprec(s) == striprec(t)
};
