---
title: AbstractDataType
---

#### Synopsis

A definition of a data type.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

An [Abstract Data Type](http://en.wikipedia.org/wiki/Abstract_data_type) is a mathematical description of a structure that can be implemented in various ways.  Well-known examples are [stack](http://en.wikipedia.org/wiki/Stack_(data_structure)) and [tree](http://en.wikipedia.org/wiki/Tree_(data_structure)). For instance, a stack data type can be characterized by `empty` (the empty stack), two functions `push` and `pop` and axioms that define them. At the implementation level, a stack can be implemented using a list, array or something else. In object-oriented programming abstract data types are a way of [Information Hiding](https://en.wikipedia.org/wiki/Information_hiding), namely to hide the implementation of a data-structure from its programming interface.

Abstract data types are **not to be confused** with [Algebraic Data Types](https://en.wikipedia.org/wiki/Algebraic_data_type), although you could use the latter to create the former. Both are abbreviated with "ADT". In functional languages, and also in Rascal, algebraic datatypes (also "ADTs" for short)
are used to define new structured data types of arbitrary complexity. A algebraic data type consists of alternative "constructors" (tree nodes) that each have a number of "fields" of a given type. By combining algebraic data-types (using them as fields of others), you can construct arbitrarily complex hierarchical structures, such as:
* the abstract syntax of logical formulas
* representations of complex run-time or static types
* abstract syntax trees of programming languages and domain specific languages

See [Algebraic Data Types]((Rascal:Declarations-AlgebraicDataType)) and 
[Constructors]((Rascal:Values-Constructor)) in the [Rascal Language Reference]((Rascal)).

Abstract Data Types are not as important for Rascal programmers as Algebraic Data Types are,
but the builtin lists, sets, relations and maps can be seen as Abstract Data Types. This is because
you as a programmer do not have to understand how they are implemented under-the-hood.

#### Examples

## Abstract Data Types in Daily Life

*  A stack of trays in the local cafetaria: ![]((dispenser.jpg))
   [credit](http://www.thermo-box.co.uk/fimi-food-transport-and-handling-products/self-levelling-heated-and-unheated-plate-and-tray-systems.html)

*  A tree:
   ![]((tree.jpg))
   [credit](http://free-extras.com/images/tree-569.htm)

*  Coral:
   ![]((coral.jpg))
   [credit](http://blog.enn.com/?p=476)


## Abstract Data Types in computer science

*  The run-time stack of a programming language interpreter.
*  A search tree.
*  An ontology.


## Algebraic Types in Rascal

*  A tree data type:
```rascal
data MyTree = leaf(int n) | tree(str name, MyTree left, MyTree right);
```

#### Benefits

#### Pitfalls

