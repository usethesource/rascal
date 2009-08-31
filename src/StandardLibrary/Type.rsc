module Type

import ParseTree;

@reified
@doc{
  The definition of reified types as an abstract data-type.
  Note that any built-in type[&T] acts as an alias for any Type tree that represents a type T.
  
  Examples:
     rascal>type[int] intType = #int;
     rascal>intType == \int; // returns true
     rascal>&T parse(type[&T] type) { if (&T result := foo()) { return result; \} throw "parse error"; \}
}
public data Type =
  \value() |
  \int() |
  \real() |
  \bool() |
  \map(Type \key, Type \value) |
  \list(Type elem) |
  \set(Type elem) |
  \rel(list[Type] fields) |
  \tuple(list[Type] fields) |
  \void() |
  \func(Type \return, list[Type] arguments) |
  \node() |
  \non-terminal(Symbol symbol) |
  \adt(str name, list[Constructor] constructors) |
  \adt(str name, list[Type] parameters, list[Constructor] constructors) | 
  \alias(str name, Type aliased) |
  \alias(str name, list[Type] parameters, Type aliased) |
  \loc() |
  \reified(Type reified) |
  \parameter(str name, Type bound) |
  \parameter(str name);
  
public data Constructor = 
  \constructor(str name, list[Type] arguments);  