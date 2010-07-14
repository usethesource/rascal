@doc{
  This modules defines an simple but effective internal format for the representation of context-free grammars.
  
  The core of the format is the Production type from the ParseTree module. A grammar is simply collection of such
  productions. Where there is interaction between productions, usually to define disambiguation filters, the Production
  type is extended with combinators.
}
module rascal::parser::Grammar

import ParseTree;

@doc{
  A grammar is simply a set of productions
}
data Grammar = grammar(set[Symbol] start, set[Production] productions);

@doc{
Here we extend productions with basic combinators allowing to
construct ordered and un-ordered compositions, and also a difference operator.

The intended semantics are that 
 	'choice' means unordered choice,
 	'first'  means ordered choice, where alternatives are tried from left to right,
	'diff'   means all alternatives of the first argument are accepted, unless one
 		         of the alternatives from the right argument are accepted.
    'assoc'  means all alternatives are acceptible, but nested on the declared side
    'others' means '...', which is substituted for a choice among the other definitions
    'restrict' means the language defined by rhs, but predicated on a certain lookahead restriction
} 
data Production = choice(Symbol rhs, set[Production] alternatives)                  
                | first(Symbol rhs, list[Production] choices)
                | \assoc(Symbol rhs, Associativity \assoc, set[Production] alternatives)               
                | diff(Symbol rhs, Production language, set[Production] alternatives)
                | restrict(Symbol rhs, Production language, set[list[Symbol]] restrictions)
                | others(Symbol rhs)
                ;

@doc{
  These combinators are defined on Symbol, but we assume that only char-class constructors are passed in
}
data Symbol = intersection(Symbol lhs, Symbol rhs)
            | union(Symbol lhs, Symbol rhs)
            | difference(Symbol lhs, Symbol rhs)
            | complement(Symbol cc);
            
@doc{
  A Symbol constructor that can introduce levels for a certain non-terminal, which can be used to implement priorities and associativity
}
data Symbol = level(Symbol symbol, int level);
            