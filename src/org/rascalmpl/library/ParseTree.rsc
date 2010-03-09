module ParseTree

/*
 * Functions for parsing:
 * - parse
 */
 
 /*
  * The universal parse tree format. Parsse functions always return values of type Tree.
  */
 
data ParseTree = 
     parsetree(Tree top, int amb_cnt);

data Tree =
     appl(Production prod, list[Tree] args) |
	 cycle(Symbol symbol, int cycleLength) |
	 amb(set[Tree] alternatives) | 
	 char(int character);

data Production =
     prod(list[Symbol] lhs, Symbol rhs, Attributes attributes) | 
     \list(Symbol rhs);

data Attributes = 
     \no-attrs() | \attrs(list[Attr] attrs);

data Attr =
     assoc(Associativity assoc) | term(value term) |
     id(str moduleName) | bracket() | reject() | prefer() | avoid();

data Associativity =
     \left() | \right() | \assoc() | \non-assoc();

data CharRange =
     single(int start) | range(int start, int end);

data Constructor = cons(str name);

data Symbol =
     \lit(str string) |
     \cilit(str string) | 
     \cf(Symbol symbol)  |
     \lex(Symbol symbol)  |
     \empty()  |
     \opt(Symbol symbol)  |
     \sort (str string)  | 
     \iter(Symbol symbol)  | 
     \iter-star(Symbol symbol)  | 
     \iter-sep(Symbol symbol, Symbol separator)  | 
     \iter-star-sep(Symbol symbol, Symbol separator)  | 
     \iter-n(Symbol symbol, int number)  | 
     \iter-sep-n(Symbol symbol, Symbol separator, int number)  | 
     \layout()  | 
     \char-class(list[CharRange] ranges);
     
@deprecated
data Symbol =
     \alt(Symbol lhs, Symbol rhs)  |
     \tuple(Symbol head, list[Symbol] rest)  |
     \seq(list[Symbol] symbols)  |
     \func(list[Symbol] symbols, Symbol symbol)  | 
     \parameterized-sort(str sort, list[Symbol] parameters)  | 
     \strategy(Symbol lhs, Symbol rhs)  |
     \var-sym(Symbol symbol);
     
@doc{provides access to the source location of a parse tree node}
anno loc Tree@\loc;

@doc{provides a documentation string for this parse tree node}
anno str Tree@doc;

@doc{provides the target of a link}
anno loc Tree@\link;

@doc{Parse the contents of a resource pointed to by the input parameter and return a parse tree.}
@javaClass{org.rascalmpl.library.ParseTree}
@reflect{uses information about imported SDF modules at call site}
public &T<:Tree java parse(type[&T<:Tree] start, loc input);

@doc{Parse a string and return a parse tree.}
@javaClass{org.rascalmpl.library.ParseTree}
@reflect{uses information about imported SDF modules at call site}
public &T<:Tree java parse(type[&T<:Tree] start, str input);

@doc{Yields the string of characters that form the leafs of the given parse tree.}
@javaClass{org.rascalmpl.library.ParseTree}
public str java unparse(Tree tree);
