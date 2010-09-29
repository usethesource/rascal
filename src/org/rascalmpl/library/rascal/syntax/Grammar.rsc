@doc{
  This modules defines an simple but effective internal format for the representation of context-free grammars.
  
  The core of the format is the Production type from the ParseTree module. A grammar is simply collection of such
  productions. Where there is interaction between productions, usually to define disambiguation filters, the Production
  type is extended with combinators.
}
module rascal::syntax::Grammar

import ParseTree;
import Set;

@doc{
  A grammar is a set of productions and set of start symbols.
  We also provide constructors for grammar modules (grammars with names and imports).
}
data Grammar = \grammar(set[Symbol] start, map[Symbol, set[Production]] rules)
             // | \module(str name, set[str] imports, Grammar grammar)
             // | \modules(str main, set[Grammar] modules)
             ;

@doc{
  Conveniently construct a grammar from a set of production rules
}
public Grammar grammar(set[Symbol] starts, set[Production] prods) {
  return grammar(starts, index(prods, Symbol (Production p) { return p.rhs; }));
}
           
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
data Production = \choice(Symbol rhs, set[Production] alternatives)                  
                | \first(Symbol rhs, list[Production] choices)
                | \assoc(Symbol rhs, Associativity \assoc, set[Production] alternatives)               
                | \diff(Symbol rhs, Production language, set[Production] alternatives)
                | \restrict(Symbol rhs, Production language, set[Production] restrictions)
                | \others(Symbol rhs)
                | \action(Symbol rhs, Production prod, Tree action)
                ;

@doc{
  this symbol indicates the language of lookahead restrictions for a certain other symbol
}
data Symbol = restricted(Symbol s);

@doc{
  These combinators are defined on Symbol, but we assume that only char-classes are passed in.
}
data Symbol = intersection(Symbol lhs, Symbol rhs)
            | union(Symbol lhs, Symbol rhs)
            | difference(Symbol lhs, Symbol rhs)
            | complement(Symbol cc);
            

@doc{
  Compose two grammars, by adding the rules of g2 to the rules of g1.
  The start symbols of g1 will be the start symbols of the resulting grammar.
}
public Grammar compose(Grammar g1, Grammar g2) {
  set[Production] empty = {};
  for (s <- g2.rules)
    g1.rules[s]?empty += g2.rules[s];
  return g1;
}    

@doc{
  This function is for debugging of Rascal itself, it produces the grammar defined by a certain module.
  it should dissappear as soon as the # operator can produce the reified representation of non-terminals, which should
  include a full grammar
}
@reflect   
@javaClass{org.rascalmpl.library.rascal.syntax.Grammar}    
public Grammar java getGrammar(str mod);

@doc{
  This function is for debugging of Rascal, it parses a module with the new bootstrapped Rascal parser.
}
@reflect   
@javaClass{org.rascalmpl.library.rascal.syntax.Grammar}    
public ParseTree java parseModule(loc mod, bool old);

@doc{
  This function is for debugging of Rascal, it parses a command with the new bootstrapped Rascal parser.
}
@reflect   
@javaClass{org.rascalmpl.library.rascal.syntax.Grammar}    
public ParseTree java parseCommand(str cmd, bool old);

