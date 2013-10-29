module experiments::Compiler::Examples::ParseTreeDataType

extend Type;

data Tree 
     = appl(Production prod, list[Tree] args) /*1*/
     | cycle(Symbol symbol, int cycleLength) 
     | amb(set[Tree] alternatives)  
     | char(int character)
     ;

data Production 
     = prod(Symbol def, list[Symbol] symbols, set[Attr] attributes) /*3*/
     | regular(Symbol def)
     | error(Production prod, int dot)
     | skipped()
     ;
     
data Attr 
     = \assoc(Associativity \assoc) /*4*/
     | \bracket()
     ;
     
data Associativity 
     = \left() /*5*/
     | \right() 
     | \assoc() 
     | \non-assoc()
     ;

data CharRange = range(int begin, int end) /*6*/;

alias CharClass = list[CharRange] /*7*/;


data Symbol = \start(Symbol symbol) /*8*/;

// These symbols are the named non-terminals.
data Symbol 
     = \sort(str name) /*9*/  
     | \lex(str name) 
     | \layouts(str name) 
     | \keywords(str name)
     | \parameterized-sort(str name, list[Symbol] parameters)  
     | \parameterized-lex(str name, list[Symbol] parameters)  
     ; 

// These are the terminal symbols.
data Symbol 
     = \lit(str string) /*10*/
     | \cilit(str string)
     | \char-class(list[CharRange] ranges)
     ;
    
// These are the regular expressions.
data Symbol
     = \empty() /*11*/
     | \opt(Symbol symbol)  
     | \iter(Symbol symbol)   
     | \iter-star(Symbol symbol)   
     | \iter-seps(Symbol symbol, list[Symbol] separators)   
     | \iter-star-seps(Symbol symbol, list[Symbol] separators) 
     | \alt(set[Symbol] alternatives)
     | \seq(list[Symbol] symbols)
     ;
  

data Symbol = \conditional(Symbol symbol, set[Condition] conditions) /*12*/;


data Condition
     = \follow(Symbol symbol) /*13*/
     | \not-follow(Symbol symbol)
     | \precede(Symbol symbol)
     | \not-precede(Symbol symbol)
     | \delete(Symbol symbol)
     | \at-column(int column) 
     | \begin-of-line()  
     | \end-of-line()  
     | \except(str label)
     ;

@doc{
Here we extend productions with basic combinators allowing to
construct ordered and un-ordered compositions, and associativity groups.

The intended semantics are that 
  * 'choice' means unordered choice (defined in |Type|)
  * 'priority'  means ordered choice, where alternatives are tried from left to right,
  * 'assoc'  means all alternatives are acceptable, but nested on the declared side
  * 'others' means '...', which is substituted for a choice among the other definitions
  * 'reference' means a reference to another production rule which should be substituted there,
                for extending priority chains and such.
} 
data Production 
  = \priority(Symbol def, list[Production] choices)
  | \associativity(Symbol def, Associativity \assoc, set[Production] alternatives)
  | \others(Symbol def)
  | \reference(Symbol def, str cons)
  ;
