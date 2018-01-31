@doc{
  This module implements the semantics of the ... and :cons notations in Rascal grammars.
  
  To give the proper semantics to priorities, this must be run before the priority relation
  is expanded.
}
module lang::rascalcore::grammar::definition::References

extend lang::rascalcore::grammar::definition::Grammar;
//extend ParseTree;
import lang::rascalcore::check::AType;
import Node;
import lang::rascalcore::grammar::definition::Symbols;
import IO;

AGrammar references(AGrammar g) 
  = visit (g) {
      case reference(AType s, str name) => p 
        when ss := striprec(s)
           , ss in g.rules
           , /AProduction p:prod(t, _/*, _*/) := g.rules[ss] && t.label==name
           , ss == striprec(t)
  };
