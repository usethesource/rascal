module Type

import ParseTree;

@reified
@doc{
  The definition of reified types as an abstract data-type, for documentation purposes. 
  
  You should read type[&T] for Type.
  
  Example usage:
     rascal>type[int] intType = #int;
     rascal>intType == \int; // returns true
     rascal>&T parse(type[&T] type) { if (&T result := foo()) { return result; \} throw "parse error"; \}
     
  Note that alias and parameter types do not have a reified representation since they are resolved statically.
}
public data Type =
  \value() |
  \int() |
  \real() |
  \bool() |
  \map(Type \key, Type \value) |
  \list(Type element) |
  \set(Type element) |
  \rel() | 
  \rel(Type t1) | 
  \rel(Type t1, Type t2) | 
  \rel(Type t1, Type t2, Type t3) | 
  \rel(Type t1, Type t2, Type t3, Type t4) | 
  \rel(Type t1, Type t2, Type t3, Type t4, Type t5) | 
  \rel(Type t1, Type t2, Type t3, Type t4, Type t5, Type t6, Type t7) | 
  \tuple() | 
  \tuple(Type t1) | 
  \tuple(Type t1, Type t2) | 
  \tuple(Type t1, Type t2, Type t3) | 
  \tuple(Type t1, Type t2, Type t3, Type t4) | 
  \tuple(Type t1, Type t2, Type t3, Type t4, Type t5) | 
  \tuple(Type t1, Type t2, Type t3, Type t4, Type t5, Type t6, Type t7) |  
  \void() |
  \func(Type \return) | 
  \func(Type \return, Type t1) | 
  \func(Type \return, Type t1, Type t2) | 
  \func(Type \return, Type t1, Type t2, Type t3) | 
  \func(Type \return, Type t1, Type t2, Type t3, Type t4) | 
  \func(Type \return, Type t1, Type t2, Type t3, Type t4, Type t5) | 
  \func(Type \return, Type t1, Type t2, Type t3, Type t4, Type t5, Type t6, Type t7) |  
  \node() |
  \non-terminal(Symbol symbol) |
  \adt(str name) |
  \adt(str name, list[Type] parameters) |
  \adt(str name, list[Constructor] constructors) |
  \adt(str name, list[Type] parameters, list[constructor] constructors) | 
  \loc() |
  \alias(str name, Type aliased) |
  \alias(str name, list[Type] parameters, Type aliased) |
  \reified(Type reified)
  
public data constructor[&T] = 
  \constructor(str name) | 
  \constructor(str name, Type t1) | 
  \constructor(str name, Type t1, Type t2) | 
  \constructor(str name, Type t1, Type t2, Type t3) | 
  \constructor(str name, Type t1, Type t2, Type t3, Type t4) | 
  \constructor(str name, Type t1, Type t2, Type t3, Type t4, Type t5) | 
  \constructor(str name, Type t1, Type t2, Type t3, Type t4, Type t5, Type t6, Type t7);
   