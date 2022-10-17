---
title: Algebraic Data Type
---

#### Synopsis

An Algebraic Data Type (ADT) is a composite type, defined by a number of "constructors" for each type, and each constructor has a number of (typed) fields.

#### Description

First things first: algebraic data types are not to be confused with ((AbstractDataType))s, which are definitions of data-structures that clearly separate the external interface from the (hidden) implementation of the data structure. Both sare the abbreviation "ADT".

In functional languages, and also in Rascal, algebraic datatypes
are used to define new structured data types of arbitrary complexity. A algebraic data type consists of alternative "constructors" (tree nodes) that each have a number of "fields" of a given type. By combining algebraic data-types (using them as fields of others), you can construct arbitrarily complex hierarchical structures, such as:
* the abstract syntax of logical formulas
* representations of complex run-time or static types
* abstract syntax trees of programming languages and domain specific languages

See [Algebraic Data Types]((Rascal:Declarations-AlgebraicDataType)) and 
[Constructors]((Rascal:Values-Constructor)) in the [Rascal Language Reference]((Rascal)).

#### Examples

*  An algebraic type for trees with integer leafs and labeled internal nodes:
```rascal
data MyTree 
   = leaf(int n) 
   | tree(str name, MyTree left, MyTree right);
```

* An algebraic type for boolean formulas :
```rascal
data Formula 
   = and(Formula l, Formula r)
   | or(Formula l, Formula r)
   | not(Formula n)
   | \true()
   | \false()
   ;
```

#### Benefits

* ADTs are a very general way of constructing hierarchical data-types. You can model almost anything with them.
* ADTs instances are typically `immutable`, so you can share them among different parts of a meta-program without interference

#### Pitfalls

* To model cycles and cross-edges, typically an ADT is not sufficient. It is possible to model such things on top of ADTs but it gets complex quickly with the use of closures. Better use ((Relation))s for those graph-like structures.


