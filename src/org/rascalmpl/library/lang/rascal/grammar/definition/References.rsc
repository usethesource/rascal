@synopsis{This module implements the semantics of the ... and :cons notations in Rascal grammars.
  
  To give the proper semantics to priorities, this must be run before the priority relation
  is expanded.}
module lang::rascal::grammar::definition::References

extend Grammar;
extend ParseTree;
import lang::rascal::grammar::definition::Symbols;

Grammar references(Grammar g) 
  = visit (g) {
      case reference(Symbol s, str name) => p 
        when ss := striprec(s)
           , ss in g.rules
           , /Production p:prod(label(name, t), _, _) := g.rules[ss]
           , ss == striprec(t)
  };
