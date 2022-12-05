@doc{
  This module implements the semantics of the ... and :cons notations in Rascal grammars.
  
  To give the proper semantics to priorities, this must be run before the priority relation
  is expanded.
}
module lang::rascalcore::grammar::definition::References

//extend lang::rascalcore::grammar::definition::Grammar;
import lang::rascalcore::check::ATypeBase;
import Node;
import lang::rascalcore::grammar::definition::Symbols;

AGrammar references(AGrammar g) {

  return visit (g) {
      case reference(AType s, str name): {
        if( ss := striprec(s)  
           , ss in g.rules
           , /AProduction p:prod(t, _) := g.rules[ss] &&  t.label==name
           , ss == striprec(t))
           insert p;
        }
  };
  
  }
