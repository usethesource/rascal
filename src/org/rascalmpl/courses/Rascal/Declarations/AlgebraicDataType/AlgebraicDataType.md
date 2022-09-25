---
title: Algebraic Data Type
keywords:
  - data
  - struct
  - record
  - keywords
  - term
  - AST
---

#### Synopsis

Define a user-defined type (Algebraic Data Type).

#### Syntax

```rascal
// declaring a normal data type with 2 constructors of which the second also has keyword parameters:
data _Type_
  = _ConstructorName1_ ( _ParameterType1_ _ParameterName1_, _ParameterType2_ _ParameterName2_, ...)
  | _ConstructorName2_ ( _ParameterType1_ _ParameterName1_, ..., _KeywordType1_ _KeywordType1_ = _KeywordDefaultExp1_, ...)
  ;

// declaring a type-parametrized data-type:
data _Type_ [&_TypeParameter1_, &_TypeParameter2_]
  = _ConstructorName1_ ( &_TypeParameter1_ _ParameterName1_, &_TypeParameter2_ _ParameterName2_, ...)
  | _ConstructorName2_ ( &_TypeParameter1_ _ParameterName1_, ..., _KeywordType1_ _KeywordType1_ = _KeywordDefaultExp1_, ...)
  ;  


// declaring common keyword parameters:
data _Type_(_KeywordType1_ _KeywordType1_ = _KeywordDefaultExp1_, ...);
```

#### Types

#### Function

#### Description

The user-defined types in Rascal are either concrete ((SyntaxDefinition))s, ((Declarations-Alias))es, or ((AlgebraicDataType))s ("ADTs"). We use ADTs to define the shapes of structured, hierarchical data, that can also be recursive. Many think of ADTs as tree-like data-structures, others think of them as many-sorted algebraic signatures, and then again the concept of a "case class" from object-oriented programming also comes very close.

In Rascal, algebraic data types have to be declared first by listing for each type a number of ((Value-Constructor))s, and then values can be constructed using ((Expressions-Call)) to the declared constructor functions.

Constructor declarations are very much like function signatures:
* They have positional parameters with types
* They have keyword parameters with types and default values.

However, unlike function signatures, constructor signatures can not have ((Patterns)) as parameters. Only ((Pattern-Variable))s are allowed.

Algebraic data-types can have type parameters, such as ((util::Maybe)) for more generically applicable data-structures.

When "common keyword parameters" are declared, they are woven into the declarations of all visible constructors.

When there are functions with the same name and the same ADT as return type in scope, a constructor becomes
one of overloaded alternatives. See also ((Expressions-Call)) for more semantics of overloading. Constructor
functions are always considered to be `default`, so they are tried only after all the other functions have failed.

#### Examples

The following data declaration defines the datatype `Bool` that contains various constants (`tt()` and `ff()`
and constructor functions `conj` and `disj`.
```rascal-shell,continue
data Bool 
  = tt() 
  | ff() 
  | conj(Bool L, Bool R)  
  | disj(Bool L, Bool R)
  ;
```
 
Terms of type `Bool` can be constructed using the defined constructors:
```rascal-shell,continue
conj(tt(),ff());
```

Now let's add a "common" keyword field:
```rascal,continue
data Bool(loc origin=|unknown:///|);
tt(origin=|home:///MyDocuments/test.bool|)
```

A parametrized data-type can be useful at times. The following
also demonstrates an overloaded function:

```rascal-shell
data SortedList[&T] = sorted(list[&T] lst);
SortedList[&T] sorted([*&T a, &T e, *&T b, &T f, *&T c]) = sorted([*a, f, *b, e, *c]) when f < e;
sorted([3,2,1])
```

The (overloaded) `sorted` constructor is only built when the `sorted` function is done finding a list that is sorted
by swapping elements that are out of order using [list matching]((Patterns-List)). 

Since `SortedList` is type-parametrized it works on any kind of type:
```rascal-shell,continue
sorted(["tic", "tac", "toe"])

#### Benefits

#### Pitfalls

