module lang::rascalcore::compile::Examples::Tst1

import Type;
    
data Tree //(loc src = |unknown:///|(0,0,<0,0>,<0,0>))
     = appl(Production prod, list[Tree] args) // <1>
     | cycle(Symbol symbol, int cycleLength)  // <2>
     | amb(set[Tree] alternatives) // <3> 
     | char(int character) // <4>
     ;

@doc{
.Synopsis
Production in ParseTrees 

.Description

The type `Production` is introduced in <<Prelude-Type>>, see <<Type-Production>>. Here we extend it with the symbols
that can occur in a ParseTree. We also extend productions with basic combinators allowing to
construct ordered and un-ordered compositions, and associativity groups.

<1> A `prod` is a rule of a grammar, with a defined non-terminal, a list
    of terminal and/or non-terminal symbols and a possibly empty set of attributes.
  
<2> A `regular` is a regular expression, i.e. a repeated construct.

<3> A `error` represents a parse error.

<4> A `skipped` represents skipped input during error recovery.

<5> `priority` means operator precedence, where the order of the list indicates the binding strength of each rule;
<6> `assoc`  means all alternatives are acceptable, but nested on the declared side;
<7> `reference` means a reference to another production rule which should be substituted there,
    for extending priority chains and such.
} 
data Production 
     = prod(Symbol def, list[Symbol] symbols, set[Attr] attributes) // <1>
     | regular(Symbol def) // <2>
     | error(Production prod, int dot) // <3>
     | skipped() // <4>
     ;
     
data Production 
     = \priority(Symbol def, list[Production] choices) // <5>
     | \associativity(Symbol def, Associativity \assoc, set[Production] alternatives) // <6>
     | \reference(Symbol def, str cons) // <7>
     ;

@doc{
.Synopsis
Attributes in productions.

.Description

An `Attr` (attribute) documents additional semantics of a production rule. Neither tags nor
brackets are processed by the parser generator. Rather downstream processors are
activated by these. Associativity is a parser generator feature though. 
}
data Attr = \bracket() | /*deprecated*/ \assoc(Associativity \assoc);

@doc{
.Synopsis
Associativity attribute. 
 
.Description

Associativity defines the various kinds of associativity of a specific production.
}  
data Associativity 
     = \left()
     | \right() 
     | \assoc() 
     | \non-assoc()
     ;

@doc{
.Synopsis
Character ranges and character class
.Description

*  `CharRange` defines a range of characters.
*  A `CharClass` consists of a list of characters ranges.
}
data CharRange = range(int begin, int end);

alias CharClass = list[CharRange];

@doc{
.Synopsis
Symbols that can occur in a ParseTree

.Description

The type `Symbol` is introduced in <<Prelude-Type>>, see <<Type-Symbol>>, to represent the basic Rascal types,
e.g., `int`, `list`, and `rel`. Here we extend it with the symbols that may occur in a ParseTree.

<1>  The `start` symbol wraps any symbol to indicate that it is a start symbol of the grammar and
        may occur at the root of a parse tree.
<2>  Context-free non-terminal
<3>  Lexical non-terminal
<4>  Layout symbols
<5>  Terminal symbols that are keywords
<6>  Parameterized context-free non-terminal
<7> Parameterized lexical non-terminal
<8>  Terminal.
<9>  Case-insensitive terminal.
<10> Character class
<11> Empty symbol
<12> Optional symbol
<13> List of one or more symbols without separators
<14> List of zero or more symbols without separators
<15> List of one or more symbols with separators
<16> List of zero or more symbols with separators
<17> Alternative of symbols
<18> Sequence of symbols
<19> Conditional occurrence of a symbol.

}
data Symbol // <1>
     = \start(Symbol symbol);

// These symbols are the named non-terminals.
data Symbol 
     = \sort(str name) // <2> 
     | \lex(str name)  // <3>
     | \layouts(str name)  // <4>
     | \keywords(str name) // <5>
     | \parameterized-sort(str name, list[Symbol] parameters) // <6>
     | \parameterized-lex(str name, list[Symbol] parameters)  // <7>
     ; 

// These are the terminal symbols.
data Symbol 
     = \lit(str string)   // <8>
     | \cilit(str string) // <9>
     | \char-class(list[CharRange] ranges) // <10>
     ;
    
// These are the regular expressions.
data Symbol
     = \empty() // <11>
     | \opt(Symbol symbol)  // <12>
     | \iter(Symbol symbol) // <13>
     | \iter-star(Symbol symbol)  // <14>
     | \iter-seps(Symbol symbol, list[Symbol] separators)      // <15> 
     | \iter-star-seps(Symbol symbol, list[Symbol] separators) // <16>
     | \alt(set[Symbol] alternatives) // <17>
     | \seq(list[Symbol] symbols)     // <18>
     ;
  
data Symbol // <19>
     = \conditional(Symbol symbol, set[Condition] conditions);

public bool subtype(Symbol::\sort(_), Symbol::\adt("Tree", _)) = true;

@doc{
.Synopsis
Datatype for declaring preconditions and postconditions on symbols

.Description

A `Condition` can be attached to a symbol; it restricts the applicability
of that symbol while parsing input text. For instance, `follow` requires that it
is followed by another symbol and `at-column` requires that it occurs 
at a certain position in the current line of the input text.
}
data Condition
     = \follow(Symbol symbol)
     | \not-follow(Symbol symbol)
     | \precede(Symbol symbol)
     | \not-precede(Symbol symbol)
     | \delete(Symbol symbol)
     | \at-column(int column) 
     | \begin-of-line()  
     | \end-of-line()  
     | \except(str label)
     ;


anno str Tree@doc;

//data AnotherAndData = a();
//
//anno list[int] AnotherAndData@l;
//
//value main(){ //test bool anotherAnd() {
//    v = a()[@l = [1,2,3]];
//    return v;
////    
////    //list[list[int]] res = [];
////    //if(v@l? && [*int x,*int y] := v@l) {
////    //   res = res + [ x, y ];
////    //   fail;
////    //}
////    //return res ==  [[],
////    //                [1,2,3],
////    //                [1],
////    //                [2,3],
////    //                [1,2],
////    //                [3],
////    //                [1,2,3],
////    //                []
////    //                ];
//}

//value main() //test bool testList6() 
//    = [1, [*int _, int N, *int _], 3] := [1, [10,20], 3] && N > 10;

// test bool functionTypeArgumentVariance3() {
//  value f = int (str x ) { return 1; };
//  return int (int _) _ !:= f;
//} 
