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
  A Symbol constructor that can be used to generate new non-terminal names from existing non-terminal names.
  Applications include the introduction of levels to a grammar with priorities, or non-terminals that exclude
  certain productions to implement associativity.
}
data Symbol = prime(Symbol symbol, str reason, list[int] primes);

@doc{
  This rules simplifies complex nested primes non-terminals to improve readability of generated grammars
  and to limit the need for case distinctions in some parts of the back-end
}
rule collapse 
  prime(prime(Symbol s, str r1, list[int] p1), str r2, list[int] p2) =>
  prime(s, r1 + " and " + r2, p1 + p2);
     
@doc{
  This rule pushes labels out of primes to limit case distinctions in the back-end
}
rule label prime(label(str l, Symbol s), str r, list[int] p) => label(l, prime(s, r, p));
    
@doc{
  This function is for debugging of Rascal itself, it produces the grammar defined by a certain module.
  it should dissappear as soon as the # operator can produce the reified representation of non-terminals, which should
  include a full grammar
}
@reflect   
@javaClass{org.rascalmpl.library.rascal.parser.Grammar}    
public Grammar java getGrammar(str mod);
   