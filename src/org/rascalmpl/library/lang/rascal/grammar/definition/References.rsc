@doc{
  This module implements the semantics of the ... and :cons notations in Rascal grammars.
  
  To give the proper semantics to priorities, this must be run before the priority relation
  is expanded.
}
module lang::rascal::grammar::definition::References

extend Grammar;
extend ParseTree;

Grammar references(Grammar g) = visit (g) {
  case others(Symbol s)              => g.rules[s] 
    when s in g.rules
    
  case reference(Symbol s, str name) => p 
    when s in g.rules, /Production p:prod(label(name, s), _, _) := g.rules[s]
};
