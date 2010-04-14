module rascal::parser::Grammar

import ParseTree;

// A grammar is simply a set of productions
data Grammar = grammar(set[Symbol] start, set[Production] productions);

// Here we extend productions with basic combinators allowing to
// construct ordered and un-ordered compositions, and also a difference operator.
//
// The intended semantics are that 
// 		'choice' means unordered choice,
// 		'first'  means ordered choice, where alternatives are tried from left to right,
// 		'diff'   means all alternatives of the first argument are accepted, unless one
// 		         of the alternatives from the right argument are accepted.
//      'assoc'  means all alternatives are acceptible, but nested on the declared side
//      'others' means '...', which is substituted for a choice among the other definitions
//      'restrict' means the language defined by rhs, but predicated on a certain lookahead restriction 
data Production = choice(Symbol rhs, set[Production] alternatives)                  
                | first(Symbol rhs, list[Production] choices)
                | assoc(Symbol rhs, Associativity assoc, set[Production] alternatives)               
                | diff(Symbol rhs, Production language, set[Production] alternatives)
                | restrict(Symbol rhs, Production language, set[list[CharRange]] restrictions)
                | others(Symbol rhs)
                ;