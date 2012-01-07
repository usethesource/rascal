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
  
Synopsis: This is a module that reflects Rascal's type system, implemented in Rascal itself.

Description:  

The goal of this module is to provide:
  * reflection capabilities that are useful for deserialization and validation of data, and 
  * to provide the basic building blocks for syntax trees (see [ParseTree])

The following definition is built into Rascal:
  * <pre>data type[&T] = type(Symbol symbol, map[Symbol,Production] definitions);</pre>
  
The # operator will always produce a value of type[&T], where &T is bound to the type that was reified.

Examples:
<screen>
#int
#rel[int,int]
data B = t();
#B
syntax A = "a";
#A;
type(\int(),())
</screen>     
    

}

module Type

@doc{Symbols are values that represent Rascal's types. These are the atomic types.}  
data Symbol
  = \int()
  | \bool()
  | \real()
  | \str()
  | \num()
  | \node()
  | \void()
  | \value()
  | \loc()
  | \datetime()
  ;
 
@doc{Labels are used to give names to symbols, such as field names, constructor names, etc.}  
data Symbol 
  = \label(str name, Symbol symbol)
  ;
  
@doc{These are the composite types}  
data Symbol 
  = \set(Symbol symbol)
  | \rel(list[Symbol] symbols)
  | \tuple(list[Symbol] symbols)
  | \list(Symbol symbol)
  | \map(Symbol from, Symbol to)
  | \bag(Symbol symbol)
  | \adt(str name, list[Symbol] parameters)
  | \cons(Symbol \adt, list[Symbol] parameters)
  | \alias(str name, list[Symbol] parameters, Symbol aliased)
  | \func(Symbol ret, list[Symbol] parameters)
  | \reified(Symbol symbol)
  ;

@doc{Parameter represents a type variable.}  
data Symbol
  = \parameter(str name, Symbol bound)
  ;


@doc{Productions represent abstract (recursive) definitions of abstract data type constructors and functions}  
data Production
  = \cons(Symbol def, list[Symbol] symbols, set[Attr] attributes)
  | \func(Symbol def, list[Symbol] symbols, set[Attr] attributes)
  | \choice(Symbol def, set[Production] alternatives)
  ;

@doc{
  Attributes register additional semantics annotations of a definition. 
}
data Attr 
  = \tag(value \tag) 
  ;
  
public Symbol \var-func(Symbol ret, list[Symbol] parameters, Symbol varArg) =
              \func(ret, parameters + \list(varArg));

// The following normalization rules canonicalize grammars to prevent arbitrary case distinctions later

@doc{Nested choice is flattened}
public Production choice(Symbol s, {set[Production] a, choice(Symbol t, set[Production] b)})
  = choice(s, a+b);
  

@doc{Functions with variable argument lists are normalized to normal functions}
public Production \var-func(Symbol ret, str name, list[tuple[Symbol typ, str label]] parameters, Symbol varArg, str varLabel) =
       \func(ret, name, parameters + [<\list(varArg), varLabel>]);
       
public bool subtype(type[&T] t, type[&U] u) = subtype(t.symbol, u.symbol);

@doc{
  This function documents and implements the subtype relation of Rascal's type system. 
}
public bool subtype(Symbol s, s) = true;
public default bool subtype(Symbol s, Symbol t) = false;

public bool subtype(Symbol _, \value()) = true;
public bool subtype(\void(), Symbol _) = true;
public bool subtype(Symbol::\cons(Symbol a, list[Symbol] _), a) = true;
public bool subtype(\adt(str _), \node()) = true;
public bool subtype(\adt(str _, list[Symbol] _), \node()) = true;
public bool subtype(\adt(str n, list[Symbol] l), \adt(n, list[Symbol] r)) = subtype(l, r);
public bool subtype(\alias(str _, list[Symbol] _, Symbol aliased), Symbol r) = subtype(aliased, r);
public bool subtype(Symbol l, \alias(str _, Symbol aliased)) = subtype(l, aliased);
public bool subtype(\int(), \num()) = true;
public bool subtype(\real(), \num()) = true;
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
public bool subtype(Symbol::\func(Symbol r1, list[Symbol] p1), Symbol::\func(Symbol r2, list[Symbol] p2)) = subtype(r1, r2) && subtype(p2, p1); // note the contra-variance of the argument types

public bool subtype(list[Symbol] l, list[Symbol] r) = all(i <- [0..size(l) - 1], subtype(l[i], r[i])) when size(l) == size(r);
public default bool subtype(list[Symbol] l, list[Symbol] r) = false;

data Exception 
  = typeCastException(type[value] from, type[value] to);

public &T typeCast(type[&T] typ, value v) {
  if (&T x := v)
    return x;
  throw typeCastException(typeOf(v), typ);
}

@doc{
Synopsis: returns the dynamic type of a value as a reified type
Description: 

As opposed to the # operator, which produces the type of a value statically, this
function produces the dynamic type of a value, represented by a symbol.


Examples:
<screen>
import Type;
value x = 1;
typeOf(x)
</screen>

Pitfalls: 
  * Note that the typeOf function does not produce definitions, like the [Reify] operator does, 
since values may escape the scope in which they've been constructed leaving their contents possibly undefined.
}
@javaClass{org.rascalmpl.library.Type}
@reflect
public java Symbol typeOf(value v);
