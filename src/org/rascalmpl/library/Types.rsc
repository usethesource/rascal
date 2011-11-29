@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@unfinished
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl}
@doc{
  
This is a module that reflects Rascal's type system, implemented in Rascal itself:
  * for reflection purposes, and 
  * to provide a syntax and semantics for reified types.
     
DISCLAIMER: This module is ***unfinished*** and the type(Symbol constructor) is currently not represented by the following definition.
     
The type(Symbol symbol) constructor is builtin. Rascal enforces that &T is always bound to the type represented by the |symbol| field.

This means that:
  * The # operator produces this type() constructor when called on a type, i.e. #int will produce the value \type(\int()) of type[int]
  * Programmatically constructing a value such as type(\int()) will produce a value of static type[value] and of dynamic type[int]
    
data type[&T] = type(Symbol symbol);
     
}
module Types

@doc{Symbols are values that represent Rascal's types}  
/*
data Symbol
  = \int()
  | \bool()
  | \real()
  | \str()
  | \num()
  | \void()
  | \value()
  | \loc()
  | \datetime()
  | \set(Symbol symbol)
  | \rel(list[Symbol] symbols)
  | \tuple(list[Symbol] symbols)
  | \rel(list[tuple[Symbol typ, str label]] fields)
  | \tuple(list[tuple[Symbol typ, str label]] fields)
  | \list(Symbol symbol)
  | \map(Symbol from, Symbol to)
  | \bag(Symbol symbol)
  | \adt(str name)
  | \adt(str name, list[Symbol] typeParameters)
  | \cons(Symbol adt, str name)
  | \alias(str name, Symbol aliased)
  | \alias(str name, list[Symbol] typeParameters, Symbol aliased)
  | \func(Symbol ret, list[Symbol] parameters)
  | \var-func(Symbol ret, list[Symbol] parameters, Symbol varArg)
  | \type(Symbol symbol)
  ;

@doc{Productions represent abstract (recursive) definitions of types}  
data Production
  = \cons(Symbol adt, str name, list[tuple[Symbol typ, str label]] parameters)
  | \alias(str name, list[Symbol] typeParameters, Symbol symbol)
  | \func(Symbol ret, str name, list[tuple[Symbol typ, str label]] parameters)
  | \var-func(Symbol ret, str name, list[tuple[Symbol typ, str label]] parameters, Symbol varArg)
  ;

public bool subtype(type[&T] t, type[&U] u) = subtype(t.symbol, u.symbol);

@doc{
This function documents and implements the subtype relation (not proper!) of Rascal's type system. 
}

public bool subtype(Symbol s, s) = true;
public default bool subtype(Symbol s, Symbol t) = false;

public bool subtype(Symbol _, \value()) = true;
public bool subtype(\void(), Symbol _) = true;
public bool subtype(\cons(Symbol a, str _), a) = true;
public bool subtype(\adt(str _), \node()) = true;
public bool subtype(\adt(str _, list[Symbol] _), \node()) = true;
public bool subtype(\adt(str n, list[Symbol] l), \adt(n, list[Symbol] r)) = subtype(l, r);
public bool subtype(\alias(str _, Symbol aliased), Symbol r) = subtype(aliased, r);
public bool subtype(Symbol l, \alias(str _, Symbol aliased)) = subtype(l, aliased);
public bool subtype(\int(), num()) = true;
public bool subtype(\real(), num()) = true;
public bool subtype(Symbol l, \tuple(list[tuple[Symbol typ, str label]]  fields)) = subtype(l, \tuple([e | <_,e> <- fields]));
public bool subtype(Symbol l, \rel(list[tuple[Symbol typ, str label]]  fields)) = subtype(l, \rel([e | <_,e> <- fields])); 
public bool subtype(\tuple(list[tuple[Symbol typ, str label]]  fields), Symbol r) = subtype(\tuple([e | <_,e> <- fields]), r);
public bool subtype(\rel(list[tuple[Symbol typ, str label]]  fields), Symbol r) = subtype(\rel([e | <_,e> <- fields]), r); 
public bool subtype(\tuple(list[Symbol] l), \tuple(list[Symbol] r)) = subtype(l, r);
public bool subtype(\rel(list[Symbol] l), \rel(list[Symbol] r)) = subtype(l, r);
public bool subtype(\list(Symbol s), \list(Symbol t)) = subtype(s, t);  
public bool subtype(\set(Symbol s), \set(Symbol t)) = subtype(s, t);  
public bool subtype(\bag(Symbol s), \bag(Symbol t)) = subtype(s, t);  
public bool subtype(\map(Symbol from1, Symbol to1), \map(Symbol from2, Symbol to2)) = subtype(from1, from2) && subtype(to1, to2);
public bool subtype(\func(Symbol r1, list[Symbol] p1), \func(Symbol r2, list[Symbol] p2)) = subtype(r1, r2) && subtype(p2, p1); // note the contra-variance of the argument types

public default bool subtype(list[Symbol] l, list[Symbol] r) = false;
public bool subtype(list[Symbol] l, list[Symbol] r) = all(i <- [0..size(l) - 1], subtype(l[i], r[i])) when size(l) == size(r);
*/

data Exception 
  = typeCastException(type[value] from, type[value] to);

public &T typeCast(type[&T] _, value v) {
  if (&T x := v)
    return x;
  throw typeCastException(#value, typ);
}

@javaClass{org.rascalmpl.library.Types}
@reflect
public java type[value] typeOf(value v);