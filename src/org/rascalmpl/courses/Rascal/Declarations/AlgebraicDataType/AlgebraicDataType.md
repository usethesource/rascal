---
title: Algebraic Data Type
keywords:
  - data
  - algebraic datatype
  - algebraic data type
  - algebraic data-type
  - struct
  - record
  - keywords
  - term
  - AST
  - decoration
  - annotation
  - keyword fields
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

In Rascal, algebraic data types have to be declared first by listing for each type a number of ((Values-Constructor))s, and then values can be constructed using ((Expressions-Call)) to the declared constructor functions.

Constructor declarations are very much like function signatures:
* They have positional parameters with types
* They have keyword parameters with types and default values.

However, unlike function signatures, constructor signatures can not have ((Patterns)) as parameters. Only ((Patterns-Variable))s are allowed.

Algebraic data-types can have type parameters, such as ((util::Maybe)) for more generically applicable data-structures.

When "common keyword parameters" are declared, they are woven into the declarations of all visible constructors.

When there are functions with the same name and the same ADT as return type in scope, a constructor becomes
one of overloaded alternatives. See also ((Expressions-Call)) for more semantics of overloading. Constructor
functions are always considered to be `default`, so they are tried only after all the other functions have failed.

Finally, Algebraic Data Types in Rascal support forward declaration in order to use a data type before it has been declared (please see examples). 

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
example = conj(tt(),ff());
example.L
example.R
```

Now let's add a "common" keyword field:
```rascal-shell,continue
data Bool(loc origin=|unknown:///|);
example = tt(origin=|home:///MyDocuments/test.bool|);
example.origin
```

A parametrized data-type can be useful at times. The following
also demonstrates an overloaded function:

```rascal-shell,continue
data SortedList[&T] = sorted(list[&T] lst);
SortedList[&T] sorted([*&T a, &T e, *&T b, &T f, *&T c]) = sorted([*a, f, *b, e, *c]) when f < e;
sorted([3,2,1])
```

The (overloaded) `sorted` constructor is only built when the `sorted` function is done finding a list that is sorted
by swapping elements that are out of order using [list matching]((Patterns-List)). 

Since `SortedList` is type-parametrized it works on any kind of type:
```rascal-shell,continue
sorted(["tic", "tac", "toe"])
```

More direct usage of overloaded constructors and functions is "normalizing rewrite rules". 
Here we use an axiomatic definition of `conj` and `disj` to rewrite all applications of `conj` to `tt` or `ff`:

```rascal-shell,continue
Bool conj(ff(), Bool _) = ff();
Bool conj(tt(), Bool b) = b;
Bool disj(tt(), Bool _) = tt();
Bool disj(ff(), Bool b) = b;
```

Now let's try it out:
```rascal-shell,continue
disj(conj(tt(),tt()), ff())
```

Recursive declarations are straightforward, consider for instance the representation of a list of 
numbers as a tree:

```rascal-shell,continue
data L = Cell(int nm) | Cell(int nm, L rest_of_list); 
```

A value for `L` now looks like:

```rascal-shell,continue
L q = Cell(1, Cell(2, Cell(3, Cell(4))));
```

Notice here that it was possible to use `L` in its own definition. But what about having to 
define two (or more) recursive data types, each depending on the definition of the other?

At first sight, it would seem impossible to define a data type in terms of another that has not 
yet been defined while itself is using the data type we are trying to define!

This condition is known as [mutual recursion](https://en.wikipedia.org/wiki/Mutual_recursion) 
(and the related data types would be called "mutually recursive") and can be resolved by using 
"forward declaration".

To demonstrate forward declaration for mutually recursive data types in Rascal, let's try to 
define a graph whose nodes can have an arbitrary number of children, each of which can be either 
an integer or a further graph whose nodes can have an arbitrary number of children, each of which 
can be either an integer... and so on:

```rascal-shell,continue
data ND;                              // Forward declaration of ND to include it in the definition of Atom.
data Atom = Atom(int a) | Atom(ND b); // An attempt to define Atom without the forward declaration would fail here.
data ND = ND(list[Atom] l);           // Final declaration of ND which re-uses Atom which itself re-uses ND
```

The definition of a value for `Atom` would now be:

```rascal-shell,continue
Atom g = Atom(ND([Atom(1), Atom(ND([Atom(2), Atom(3)]))]));
```

#### Benefits

#### Pitfalls

